/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import org.apache.jetspeed.security.spi.impl.MessageDigestCredentialPasswordEncoder;

import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;
import junit.framework.TestCase;

public class ManagementIPlugAuthenticateTestLocal extends TestCase {

    /*
     * Test method for 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int, int)'
     */
    public void testSearch() throws Exception {
        
        ManagementIPlug iplug = new ManagementIPlug();
        MessageDigestCredentialPasswordEncoder penc = new MessageDigestCredentialPasswordEncoder();
        
        // test encrypted digest login
        String digest = penc.encode("user", "user");
        IngridQuery q = QueryStringParser.parse("datatype:management login:user digest:"+ digest + " management_request_type:0");
        IngridHits hits = iplug.search(q, 0, 1);
        IngridHit hit = hits.getHits()[0];
        boolean result = hit.getBoolean("authenticated");
        assertEquals(result, true);

        digest = penc.encode("admin", "admin");
        q = QueryStringParser.parse("datatype:management login:admin digest:"+ digest + " management_request_type:0");
        hits = iplug.search(q, 0, 1);
        hit = hits.getHits()[0];
        result = hit.getBoolean("authenticated");
        assertEquals(result, true);
        
        
        // test wrong digest
        digest = penc.encode("admin", "wrong");
        q = QueryStringParser.parse("datatype:management login:admin digest:"+ digest + " management_request_type:0");
        hits = iplug.search(q, 0, 1);
        hit = hits.getHits()[0];
        result = hit.getBoolean("authenticated");
        assertEquals(result, false);

        // test clear text digest
        q = QueryStringParser.parse("datatype:management login:admin digest:admin management_request_type:0");
        hits = iplug.search(q, 0, 1);
        hit = hits.getHits()[0];
        result = hit.getBoolean("authenticated");
        assertEquals(result, true);
        
    }
    
}
