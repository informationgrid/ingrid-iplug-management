/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
            int[] totalSize = new int[1];
            totalSize[0] = 0;
            try {
                IngridHit[] hitsTemp = null;
                switch (type) {
                // authenticate a user
                case MANAGEMENT_AUTHENTICATE:
                    
                    // get login and passwd from query
                    final String login = getField(query, "login");
                    final String passwd = getField(query, "password");
                    IngridHit hit = new IngridHit(this.fPlugId, 0, 0, 1.0f);
                    
                    // authenticate
                    String authenticated = "0";
                    if (login != null && passwd != null) {
                        IngridCredentialHandler ch = new IngridCredentialHandler();
                        if (ch.authenticate(login, passwd)) {
                            authenticated = "1";
                        }
                    }
                    hit.put("authenticated", authenticated);
                    
                    // build return value
                    hitsTemp = new IngridHit[1];
                    hitsTemp[0] = hit;
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
                if (log.isDebugEnabled()) {
                    log.debug("hits: " + totalSize[0]);
                }

                if ((0 == totalSize[0]) && (hits.length > 0)) {
                    totalSize[0] = hits.length;
                }
                return new IngridHits(this.fPlugId, totalSize[0], finalHits, false);
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
