/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.ingrid.iplug.management.usecase.ManagementAuthenticationUseCase;
import de.ingrid.iplug.management.usecase.ManagementDummyAuthenticationUseCase;
import de.ingrid.iplug.management.usecase.ManagementGetPartnerUseCase;
import de.ingrid.iplug.management.usecase.ManagementGetProviderAsListUseCase;
import de.ingrid.iplug.management.usecase.ManagementUseCase;
import de.ingrid.iplug.management.util.ManagementUtils;
import de.ingrid.utils.IPlug;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHitDetail;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.PlugDescription;
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

    private static final int MANAGEMENT_GET_PARTNERS = 1;

    private static final int MANAGEMENT_GET_PROVIDERS_AS_LIST = 2;

    private static final int MANAGEMENT_DUMMY_DATA = 815;

    /**
     * @see de.ingrid.utils.IPlug#configure(de.ingrid.utils.PlugDescription)
     */
    public void configure(PlugDescription plugDescription) throws Exception {
        log.info("Configuring Management-iPlug...");

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
     * @see de.ingrid.utils.ISearcher#search(de.ingrid.utils.query.IngridQuery,
     *      int, int)
     */
    public IngridHits search(IngridQuery query, int start, int length) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("incomming query : " + query.toString());
        }
        if (ManagementUtils.containsManagementDataType(query.getDataTypes())) {
            int type = -1;
            try {
                type = Integer.parseInt(ManagementUtils.getField(query, MANAGEMENT_REQUEST_TYPE));
            } catch (NumberFormatException e) {
            }
            int totalSize = 0;
            try {
                IngridHit[] hitsTemp = null;
                ManagementUseCase uc = null;

                switch (type) {
                // authenticate a user
                case MANAGEMENT_AUTHENTICATE:

                    uc = new ManagementAuthenticationUseCase();
                    // execute use case
                    hitsTemp = uc.execute(query, start, length, this.fPlugId);

                    break;
                // return some dummy data
                case MANAGEMENT_DUMMY_DATA:

                    uc = new ManagementDummyAuthenticationUseCase();
                    // execute use case
                    hitsTemp = uc.execute(query, start, length, this.fPlugId);

                    break;
                // return partner / provider hierarchy
                case MANAGEMENT_GET_PARTNERS:

                    uc = new ManagementGetPartnerUseCase();
                    // execute use case
                    hitsTemp = uc.execute(query, start, length, this.fPlugId);

                    break;

                // return provider list
                case MANAGEMENT_GET_PROVIDERS_AS_LIST:

                    uc = new ManagementGetProviderAsListUseCase();
                    // execute use case
                    hitsTemp = uc.execute(query, start, length, this.fPlugId);

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

    /**
     * @see de.ingrid.utils.IDetailer#getDetail(de.ingrid.utils.IngridHit,
     *      de.ingrid.utils.query.IngridQuery, java.lang.String[])
     */
    public IngridHitDetail getDetail(IngridHit hit, IngridQuery query, String[] requestedFields) throws Exception {
        return new IngridHitDetail();
    }

    /**
     * @see de.ingrid.utils.IDetailer#getDetails(de.ingrid.utils.IngridHit[],
     *      de.ingrid.utils.query.IngridQuery, java.lang.String[])
     */
    public IngridHitDetail[] getDetails(IngridHit[] hits, IngridQuery query, String[] requestedFields) throws Exception {
        return new IngridHitDetail[0];
    }

}
