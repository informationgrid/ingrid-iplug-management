/*
 * **************************************************-
 * Ingrid Management iPlug
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        List<Map<String, Object>> providerList = new ArrayList<Map<String, Object>>();

        Criteria queryCriteria = new Criteria();
        QueryByCriteria q = QueryFactory.newQuery(IngridProvider.class, queryCriteria);
        q.addOrderByAscending("sortkey");
        q.addOrderByAscending("sortkey");

        @SuppressWarnings("unchecked")
        Iterator<IngridProvider> providers = broker.getIteratorByQuery(q);
        while (providers.hasNext()) {
            IngridProvider provider = providers.next();

            if (log.isDebugEnabled()) {
                log.debug("Provider: " + provider.getIdent() + ":" + provider.getName());
            }

            Map<String, Object> providerHash = new HashMap<String, Object>();
            providerHash.put("providerid", provider.getIdent());
            providerHash.put("name", provider.getName());
            providerHash.put("url", provider.getUrl());

            providerList.add(providerHash);
        }

        IngridHit hit = new IngridHit(plugId, "0", 0, 1.0f);
        hit.put("provider", providerList);
        result = new IngridHit[1];
        result[0] = hit;
        if (broker != null) {
            broker.close();
        }

        return result;
    }

}
