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
