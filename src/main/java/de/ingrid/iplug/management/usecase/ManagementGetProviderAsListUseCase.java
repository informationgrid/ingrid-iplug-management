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

import de.ingrid.iplug.management.om.IngridProvider;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.query.IngridQuery;

/**
 * Delivers all providers in the following structure. (ArrayList) partners
 * (HashMap)partner {partnerid="bund", providers=(ArrayList)} (ArrayList)
 * providers (HashMap) provider {providerid="bu_bfn", name="name of provider",
 * url="url of the provider"}
 * 
 * @author joachim@wemove.com
 */
public class ManagementGetProviderAsListUseCase implements ManagementUseCase {

    private static final Log log = LogFactory.getLog(ManagementGetProviderAsListUseCase.class);

    PersistenceBroker broker = null;

    /**
     * 
     */
    public ManagementGetProviderAsListUseCase() {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    }

    /**
     * @see de.ingrid.iplug.management.usecase.ManagementUseCase#execute(de.ingrid.utils.query.IngridQuery,
     *      int, int, java.lang.String)
     */
    public IngridHit[] execute(IngridQuery query, int start, int length, String plugId) {

        IngridHit[] result = null;
        ArrayList providerList = new ArrayList();

        Criteria queryCriteria = new Criteria();
        QueryByCriteria q = QueryFactory.newQuery(IngridProvider.class, queryCriteria);
        q.addOrderByAscending("sortkey");
        q.addOrderByAscending("sortkey");

        Iterator providers = broker.getIteratorByQuery(q);
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

        IngridHit hit = new IngridHit(plugId, 0, 0, 1.0f);
        hit.put("provider", providerList);
        result = new IngridHit[1];
        result[0] = hit;
        if (broker != null) {
            broker.close();
        }

        return result;
    }

}
