/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;
import junit.framework.TestCase;

public class ManagementIPlugAuthenticateTest extends TestCase {

    /*
     * Test method for 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int, int)'
     */
    public void testSearch() throws Exception {
        
        ManagementIPlug iplug = new ManagementIPlug();
        IngridQuery q = QueryStringParser.parse("datatype:management login:admin password:admin");
        q.putInt(ManagementIPlug.MANAGEMENT_REQUEST_TYPE, 0);
        IngridHits hits = iplug.search(q, 0, 1);
        IngridHit hit = hits.getHits()[0];
        String result = (String)hit.get("authorized");
        assertEquals(result, "1");
    }

}
