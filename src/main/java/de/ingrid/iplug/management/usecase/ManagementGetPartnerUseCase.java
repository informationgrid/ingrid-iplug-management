/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.iplug.management.usecase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;

import de.ingrid.iplug.management.om.IngridPartner;
import de.ingrid.iplug.management.om.IngridProvider;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.query.IngridQuery;

/**
 * Delivers all partners and providers in the following structure. (ArrayList)
 * partners (HashMap)partner {partnerid="bund", providers=(ArrayList)}
 * (ArrayList) providers (HashMap) provider {providerid="bu_bfn"}
 * 
 * @author joachim@wemove.com
 */
public class ManagementGetPartnerUseCase implements ManagementUseCase {

    private static final Log log = LogFactory.getLog(ManagementGetPartnerUseCase.class);

    PersistenceBroker broker = null;

    /**
     * 
     */
    public ManagementGetPartnerUseCase() {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    }

    /**
     * @see de.ingrid.iplug.management.usecase.ManagementUseCase#execute(de.ingrid.utils.query.IngridQuery,
     *      int, int, java.lang.String)
     */
    public IngridHit[] execute(IngridQuery query, int start, int length, String plugId) {

        IngridHit[] result = null;
        ArrayList partnerList = new ArrayList();

        Criteria queryCriteria = new Criteria();
        QueryByCriteria q = QueryFactory.newQuery(IngridPartner.class, queryCriteria);
        q.addOrderByAscending("sortkey");

        Iterator partners = broker.getIteratorByQuery(q);
        while (partners.hasNext()) {
            IngridPartner partner = (IngridPartner) partners.next();

            if (log.isDebugEnabled()) {
                log.debug("Partner: " + partner.getIdent() + ":" + partner.getName());
            }

            // create a partner hash for each partner
            HashMap partnerHash = new HashMap();
            // add the partner id to the partnerhash
            partnerHash.put("partnerid", partner.getIdent());
            partnerHash.put("name", partner.getName());

            // get providers
            ArrayList providerList = new ArrayList();
            Criteria queryCriteriaProvider = new Criteria();
            if (partner.getIdent().equals("bund")) {
                queryCriteriaProvider.addLike("ident", "bu_%");
            } else {
                queryCriteriaProvider.addLike("ident", partner.getIdent() + "_%");
            }
            QueryByCriteria qProvider = QueryFactory.newQuery(IngridProvider.class, queryCriteriaProvider);
            qProvider.addOrderByAscending("sortkey");
            Iterator providers = broker.getIteratorByQuery(qProvider);

            while (providers.hasNext()) {
                IngridProvider provider = (IngridProvider) providers.next();

                if (log.isDebugEnabled()) {
                    log.debug("Provider: " + provider.getIdent() + ":" + provider.getName());
                }

                HashMap providerHash = new HashMap();
                providerHash.put("providerid", provider.getIdent());
                providerHash.put("name", provider.getName());
                providerHash.put("url", provider.getUrl());

                providerList.add(providerHash);
            }
            partnerHash.put("providers", providerList);

            partnerList.add(partnerHash);
        }

        IngridHit hit = new IngridHit(plugId, 0, 0, 1.0f);
        hit.put("partner", partnerList);
        result = new IngridHit[1];
        result[0] = hit;
        if (broker != null) {
            broker.close();
        }

        return result;
    }

}
