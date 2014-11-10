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
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import java.util.ArrayList;

import junit.framework.TestCase;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;

public class ManagementIPlugGetPartnerTestLocal extends TestCase {

    /*
     * Test method for
     * 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int,
     * int)'
     */
    public void testSearch() throws Exception {

        ManagementIPlug iplug = new ManagementIPlug();

        IngridQuery q = QueryStringParser.parse("datatype:management management_request_type:1");
        IngridHits hits = iplug.search(q, 0, 1);
        IngridHit hit = hits.getHits()[0];
        ArrayList partners = hit.getArrayList("partner");
        assertEquals(partners.size() > 0, true);

    }

}
