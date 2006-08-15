/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.security.PasswordCredential;
import org.apache.jetspeed.security.spi.CredentialPasswordEncoder;
import org.apache.jetspeed.security.spi.PasswordCredentialProvider;
import org.apache.jetspeed.security.spi.impl.MessageDigestCredentialPasswordEncoder;

import de.ingrid.usermanagement.jetspeed.IngridCredentialHandler;
import de.ingrid.utils.IPlug;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHitDetail;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.PlugDescription;
import de.ingrid.utils.query.FieldQuery;
import de.ingrid.utils.query.IngridQuery;

/**
 * TODO Describe your created type (class, etc.) here.
 *
 * @author joachim@wemove.com
 */
public class ManagementIPlug implements IPlug {

    public static final String DATATYPE_MANAGEMENT = "management";

    public static final String MANAGEMENT_REQUEST_TYPE = "management_request_type";

    /**
     * The logging object
     */
    private static Log log = LogFactory.getLog(ManagementIPlug.class);
    
    /**
     * The <code>PlugDescription</code> object passed at startup
     */
    private PlugDescription fPlugDesc = null;

    /**
     * Workingdirectory of the iPlug instance as absolute path 
     */
    private String fWorkingDir = ".";

    /**
     * Unique Plug-iD
     */
    private String fPlugId = null;
    
    /**
     * Time out for request
     */
    private int fTimeOut = 5000;
    

    private String fLanguage = null;
    
    private static final long serialVersionUID = ManagementIPlug.class.getName().hashCode();

    private static final int MANAGEMENT_AUTHENTICATE = 0;

    private static final int MANAGEMENT_DUMMY_DATA = 815;
    
    
    /**
     * @see de.ingrid.utils.IPlug#configure(de.ingrid.utils.PlugDescription)
     */
    public void configure(PlugDescription plugDescription) throws Exception {
        log.info("Configuring FPN-iPlug...");
        
        this.fPlugDesc = plugDescription;
        this.fPlugId = fPlugDesc.getPlugId();
        this.fWorkingDir = fPlugDesc.getWorkinDirectory().getCanonicalPath();

    }

    /**
     * @see de.ingrid.utils.IPlug#close()
     */
    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @see de.ingrid.utils.ISearcher#search(de.ingrid.utils.query.IngridQuery, int, int)
     */
    public IngridHits search(IngridQuery query, int start, int length) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("incomming query : " + query.toString());
        }
        if (containsManagementDataType(query.getDataTypes())) {
            int type = -1;
            try {
                type = Integer.parseInt(getField(query, MANAGEMENT_REQUEST_TYPE));
            } catch (NumberFormatException e) {}
            final String lang = getQueryLang(query);
            int totalSize = 0;
            try {
                IngridHit[] hitsTemp = null;
                String login = null;
                String digest = null;
                IngridHit hit = null;
                boolean authenticated = false;
                switch (type) {
                // authenticate a user
                case MANAGEMENT_AUTHENTICATE:
                    // get login and passwd from query
                    login = getField(query, "login");
                    digest = getField(query, "digest");
                    hit = new IngridHit(this.fPlugId, 0, 0, 1.0f);
                    
                    // authenticate
                    authenticated = false;
                    if (login != null && digest != null) {
                        IngridCredentialHandler ch = new IngridCredentialHandler();
                        PasswordCredentialProvider pc = ch.getPCProvider();
                        CredentialPasswordEncoder cpe = pc.getEncoder();
                        Set credentials = ch.getPrivateCredentials(login);
                        Iterator it = credentials.iterator();
                        while (it.hasNext()) {
                            PasswordCredential credential =  (PasswordCredential)it.next();
                            if ( credential != null && credential.isEnabled() && !credential.isExpired()) {
                                String password = new String(credential.getPassword());
                                if (password.equals(digest)) {
                                    authenticated = true;
                                } else if (cpe != null && cpe.encode(login, password).equals(digest)) {
                                    authenticated = true;
                                }
                            }
                        }
                    }
                    hit.putBoolean("authenticated", authenticated);
                    
                    // build return value
                    hitsTemp = new IngridHit[1];
                    hitsTemp[0] = hit;
                    break;
                // return some dummy data 
                case MANAGEMENT_DUMMY_DATA:
                    // get login and passwd from query
                    login = getField(query, "login");
                    digest = getField(query, "digest");
                    IngridCredentialHandler ch = new IngridCredentialHandler();
                    PasswordCredentialProvider pc = ch.getPCProvider();
                    CredentialPasswordEncoder cpe = pc.getEncoder();

                    hit = new IngridHit(this.fPlugId, 0, 0, 1.0f);
                    if (login.equalsIgnoreCase("admin_partner") && digest.equals(cpe.encode("admin_partner", "admin"))) {
                        // build return value
                        hitsTemp = new IngridHit[1];
                        // hit authenticated
                        hit.putBoolean("authenticated", true);
                        // hits role
                        hit.put("role", new String("admin_partner"));
                        // hits partners
                        hit.setArray("partner", new String[] {"he", "st"});
                        // hits providers
                        
                        hitsTemp[0] = hit;
                    } else if (login.equalsIgnoreCase("admin_provider") && digest.equals(cpe.encode("admin_provider", "admin"))) {
                        // build return value
                        hitsTemp = new IngridHit[1];
                        // hit authenticated
                        hit.putBoolean("authenticated", true);
                        // hits role
                        hit.put("role", new String("admin_provider"));
                        // hits partners
                        
                        // hits providers
                        hit.setArray("provider", new String[] {"bu_bmu", "bu_uba", "he_hmulv"});
                        hitsTemp[0] = hit;
                    } else if (login.equalsIgnoreCase("admin_themes_measure") && digest.equals(cpe.encode("admin_themes_measure", "admin"))) {
                        // build return value
                        hitsTemp = new IngridHit[2];
                        // hit authenticated
                        hit.putBoolean("authenticated", true);
                        // hits role
                        hit.put("role", new String("admin_themes"));
                        // hits partners
                        
                        // hits providers
                        hit.setArray("provider", new String[] {"bu_bmu", "bu_uba", "he_hmulv"});
                        hitsTemp[0] = hit;
                        
                        
                        hit = new IngridHit(this.fPlugId, 0, 0, 1.0f);                        
                        // hit authenticated
                        hit.putBoolean("authenticated", true);
                        // hits role
                        hit.put("role", new String("admin_measure"));
                        // hits partners
                        
                        // hits providers
                        hit.setArray("provider", new String[] {"bu_bmu", "bu_uba", "he_hmulv"});
                        
                        hitsTemp[1] = hit;
                    } else {
                        // hit authenticated
                        hit.putBoolean("authenticated", false);
                    }
                    break;
                default:
                    log.error("Unknown management request type.");
                    break;
                }
                IngridHit[] hits = new IngridHit[0];
                if (null != hitsTemp) {
                    hits = hitsTemp;
                }

                start = 0;

                int max = Math.min((hits.length - start), length);
                IngridHit[] finalHits = new IngridHit[max];
                System.arraycopy(hits, start, finalHits, 0, max);
                totalSize = max;
                if (log.isDebugEnabled()) {
                    log.debug("hits: " + totalSize);
                }

                if ((0 == totalSize) && (hits.length > 0)) {
                    totalSize = hits.length;
                }
                return new IngridHits(this.fPlugId, totalSize, finalHits, false);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("not correct or unsetted datatype");
            }
        }
        return new IngridHits(this.fPlugId, 0, new IngridHit[0], true);
    }

    private String getField(IngridQuery query, String fieldName) {
        FieldQuery[] fields = query.getFields();
        for (int i=0; i < fields.length; i++) {
            if (fields[i].getFieldName().equalsIgnoreCase(fieldName)) {
                Object obj = fields[i].getFieldValue();
                if (obj instanceof String) {
                    return (String)obj;
                }
                break;
            }
        }
        return null;
    }

    /**
     * @see de.ingrid.utils.IDetailer#getDetail(de.ingrid.utils.IngridHit, de.ingrid.utils.query.IngridQuery, java.lang.String[])
     */
    public IngridHitDetail getDetail(IngridHit hit, IngridQuery query, String[] requestedFields) throws Exception {
        return new IngridHitDetail();
    }

    /**
     * @see de.ingrid.utils.IDetailer#getDetails(de.ingrid.utils.IngridHit[], de.ingrid.utils.query.IngridQuery, java.lang.String[])
     */
    public IngridHitDetail[] getDetails(IngridHit[] hits, IngridQuery query, String[] requestedFields) throws Exception {
        return new IngridHitDetail[0];
    }

    private boolean containsManagementDataType(FieldQuery[] dataTypes) {
        int count = dataTypes.length;
        for (int i = 0; i < count; i++) {
            FieldQuery query = dataTypes[i];
            if (query.getFieldValue().equals(DATATYPE_MANAGEMENT) && !query.isProhibited()) {
                return true;
            }
        }
        return false;
    }

    private String getQueryLang(IngridQuery query) {
        String result = this.fLanguage;

        FieldQuery[] qFields = query.getFields();
        for (int i = 0; i < qFields.length; i++) {
            final String fieldName = qFields[i].getFieldName();
            if (fieldName.equals("lang")) {
                result = qFields[i].getFieldValue();
            }
        }

        return result;
    }
    
    
    
}