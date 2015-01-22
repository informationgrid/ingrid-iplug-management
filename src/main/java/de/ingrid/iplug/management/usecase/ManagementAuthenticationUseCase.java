/*
 * **************************************************-
 * Ingrid Management iPlug
 * ==================================================
 * Copyright (C) 2014 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.iplug.management.usecase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.ingrid.usermanagement.jetspeed.IngridCredentialHandler;
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
        String onlyRoles = null;
        IngridCredentialHandler ch = null;
        IngridHit[] result = null;

        log.error("Authenticate Method DISABLED in Management iPlug. NOT used anymore ! Access to Jetspeed has to be adapted to Jetspeed 2.3 (ingrid-jetspeed-portal jar) !");

/*
        try {
            // get login and passwd from query
            login = ManagementUtils.getField(query, "login");
            digest = ManagementUtils.getField(query, "digest");
            onlyRoles = ManagementUtils.getField(query, "onlyRoles");
            
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

                    if (log.isDebugEnabled()) {
                        log.debug("check authentication on credential.userName: " + credential.getUserName());
                    }

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
                ch.destroy();
            }

            if (log.isDebugEnabled()) {
                log.debug("authenticated: " + authenticated);
            }

            ArrayList hits = new ArrayList();
            if (authenticated || onlyRoles != null && onlyRoles.equals("true")) {
                // get permissions for the user
                IngridPermissionManager pm = new IngridPermissionManager();
                IngridRoleManager rm = new IngridRoleManager();
                IngridUserSecurityHandler sh = new IngridUserSecurityHandler();
                Principal principal = sh.getUserPrincipal(login);
                Permissions principalPermissions = SecurityHelper.getMergedPermissions(principal, pm, rm);
                // accumulate the partners and providers for the permissions
                Enumeration e = principalPermissions.elements();
                ArrayList permissions = new ArrayList();
                ArrayList permissionsPartners = new ArrayList();
                ArrayList permissionsProviders = new ArrayList();

                while (e.hasMoreElements()) {
                    Permission permission = (Permission) e.nextElement();

                    if (log.isDebugEnabled()) {
                        log.debug("Permission: " + permission.getName());
                    }

                    if (permission instanceof IngridPartnerPermission) {
                        IngridPartnerPermission partnerPermission = (IngridPartnerPermission) permission;
                        permissionsPartners.add(partnerPermission.getPartner());
                    } else if (permission instanceof IngridProviderPermission) {
                        IngridProviderPermission providerPermission = (IngridProviderPermission) permission;
                        permissionsProviders.add(providerPermission.getProvider());
                    } else if (permission instanceof IngridPortalPermission) {
                        IngridPortalPermission portalPermission = (IngridPortalPermission) permission;
                        permissions.add(portalPermission.getName());
                    }
                }
                // add accumulated permissions to the result
                Iterator it = permissions.iterator();
                while (it.hasNext()) {
                    String permissionName = (String) it.next();
                    hit = new IngridHit(plugId, 0, 0, 1.0f);
                    hit.putBoolean("authenticated", authenticated);
                    hit.put("permission", permissionName);
                    if (!permissionsPartners.isEmpty()) {
                        hit.put("partner", (String[]) permissionsPartners.toArray(new String[] {}));
                    }
                    if (!permissionsProviders.isEmpty()) {
                        hit.put("provider", (String[]) permissionsProviders.toArray(new String[] {}));
                    }
                    hits.add(hit);
                }
                pm.destroy();
                rm.destroy();
                sh.destroy();
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
*/
        return result;
    }

}
