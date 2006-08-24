/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.iplug.management.usecase;

import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.security.PasswordCredential;
import org.apache.jetspeed.security.spi.CredentialPasswordEncoder;
import org.apache.jetspeed.security.spi.PasswordCredentialProvider;

import de.ingrid.iplug.management.util.ManagementUtils;
import de.ingrid.portal.security.permission.IngridPartnerPermission;
import de.ingrid.portal.security.permission.IngridPortalPermission;
import de.ingrid.portal.security.permission.IngridProviderPermission;
import de.ingrid.usermanagement.jetspeed.IngridCredentialHandler;
import de.ingrid.usermanagement.jetspeed.IngridPermissionManager;
import de.ingrid.usermanagement.jetspeed.IngridUserSecurityHandler;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.query.IngridQuery;

/**
 * Authenticates user and delivers all ingrid specific permissions.
 * 
 * @author joachim@wemove.com
 */
public class ManagementAuthenticationUseCase implements ManagementUseCase {

    /**
     * The logging object
     */
    private static Log log = LogFactory.getLog(ManagementAuthenticationUseCase.class);

    /**
     * @see de.ingrid.iplug.management.usecase.ManagementUseCase#execute(de.ingrid.utils.query.IngridQuery,
     *      int, int, java.lang.String)
     */
    public IngridHit[] execute(IngridQuery query, int start, int length, String plugId) {

        IngridHit hit = null;
        String login = null;
        String digest = null;
        IngridCredentialHandler ch = null;
        IngridHit[] result = null;

        try {
            // get login and passwd from query
            login = ManagementUtils.getField(query, "login");
            digest = ManagementUtils.getField(query, "digest");
            hit = new IngridHit(plugId, 0, 0, 1.0f);

            // authenticate
            boolean authenticated = false;
            if (login != null && digest != null) {
                ch = new IngridCredentialHandler();
                PasswordCredentialProvider pc = ch.getPCProvider();
                CredentialPasswordEncoder cpe = pc.getEncoder();
                Set credentials = ch.getPrivateCredentials(login);
                Iterator it = credentials.iterator();
                while (it.hasNext()) {
                    PasswordCredential credential = (PasswordCredential) it.next();
                    if (credential != null && credential.isEnabled() && !credential.isExpired()) {
                        String password = new String(credential.getPassword());
                        // check for PortalPassword == digest
                        if (password.equals(digest)) {
                            authenticated = true;
                            break;
                            // check for encrypted PortalPassword == digest
                        } else if (cpe != null && cpe.encode(login, password).equals(digest)) {
                            authenticated = true;
                            break;
                            // check for PortalPassword == encrypted digest
                        } else if (cpe != null && password.equals(cpe.encode(login, digest))) {
                            authenticated = true;
                            break;
                        }
                    }
                }
            }

            ArrayList hits = new ArrayList();
            if (authenticated) {
                // get permissions for the user
                IngridPermissionManager pm = new IngridPermissionManager();
                IngridUserSecurityHandler sh = new IngridUserSecurityHandler();
                Principal principal = sh.getUserPrincipal(login);
                Permissions principalPermissions = pm.getPermissions(principal);
                // accumulate the partners and providers for the permissions
                Enumeration e = principalPermissions.elements();
                HashMap permissions = new HashMap();
                while (e.hasMoreElements()) {
                    Permission permission = (Permission) e.nextElement();
                    if (permission instanceof IngridPartnerPermission) {
                        IngridPartnerPermission partnerPermission = (IngridPartnerPermission) permission;
                        // accumulate partner for this permission
                        if (!permissions.containsKey(partnerPermission.getName())) {
                            permissions.put(partnerPermission.getName(), new HashMap());
                        }
                        HashMap permissionHash = (HashMap) permissions.get(partnerPermission.getName());
                        if (!permissionHash.containsKey("partner")) {
                            permissionHash.put("partner", new ArrayList());
                        }
                        ArrayList dst = ((ArrayList) permissionHash.get("partner"));
                        ArrayList src = partnerPermission.getPartners();
                        for (int i = 0; i < src.size(); i++) {
                            if (!dst.contains(src.get(i))) {
                                dst.add(src.get(i));
                            }
                        }
                    } else if (permission instanceof IngridProviderPermission) {
                        IngridProviderPermission providerPermission = (IngridProviderPermission) permission;
                        // accumulate partner for this permission
                        if (!permissions.containsKey(providerPermission.getName())) {
                            permissions.put(providerPermission.getName(), new HashMap());
                        }
                        HashMap permissionHash = (HashMap) permissions.get(providerPermission.getName());
                        if (!permissionHash.containsKey("provider")) {
                            permissionHash.put("provider", new ArrayList());
                        }
                        ArrayList dst = ((ArrayList) permissionHash.get("provider"));
                        ArrayList src = providerPermission.getProviders();
                        for (int i = 0; i < src.size(); i++) {
                            if (!dst.contains(src.get(i))) {
                                dst.add(src.get(i));
                            }
                        }
                    } else if (permission instanceof IngridPortalPermission) {
                        if (!permissions.containsKey(permission.getName())) {
                            permissions.put(permission.getName(), new HashMap());
                        }
                    }
                }
                // add accumulated permissions to the result
                Iterator it = permissions.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String permissionName = (String) entry.getKey();
                    hit = new IngridHit(plugId, 0, 0, 1.0f);
                    hit.putBoolean("authenticated", authenticated);
                    hit.put("permission", permissionName);
                    HashMap permissionHash = (HashMap) permissions.get(permissionName);
                    if (permissionHash.containsKey("partner")) {
                        hit.put("partner", (String[]) ((ArrayList) permissionHash.get("partner")).toArray());
                    }
                    if (permissionHash.containsKey("provider")) {
                        hit.put("provider", (String[]) ((ArrayList) permissionHash.get("provider")).toArray());
                    }
                    hits.add(hit);
                }
            }
            // make sure we have at least one valid hit
            if (hits.size() == 0) {
                hit = new IngridHit(plugId, 0, 0, 1.0f);
                hit.putBoolean("authenticated", authenticated);
                hits.add(hit);
            }

            // build return value
            result = (IngridHit[]) hits.toArray(new IngridHit[] {});
        } catch (Throwable t) {
            log.error("Error executing " + this.getClass().getName() + "!", t);
        }
        return result;
    }

}
