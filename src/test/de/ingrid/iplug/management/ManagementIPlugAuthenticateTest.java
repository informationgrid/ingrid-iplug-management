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

public class ManagementIPlugAuthenticateTest extends TestCase {

    /*
     * Test method for 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int, int)'
     */
    public void testSearch() throws Exception {
        
        ManagementIPlug iplug = new ManagementIPlug();
        MessageDigestCredentialPasswordEncoder penc = new MessageDigestCredentialPasswordEncoder();
        String digest = penc.encode("admin", "admin");
        IngridQuery q = QueryStringParser.parse("datatype:management login:admin digest:"+ digest + " management_request_type:0");
        IngridHits hits = iplug.search(q, 0, 1);
        IngridHit hit = hits.getHits()[0];
        boolean result = hit.getBoolean("authenticated");
        assertEquals(result, true);
        digest = penc.encode("admin", "wrong");
        q = QueryStringParser.parse("datatype:management login:admin digest:"+ digest + " management_request_type:0");
        hits = iplug.search(q, 0, 1);
        hit = hits.getHits()[0];
        result = hit.getBoolean("authenticated");
        assertEquals(result, false);
    }

    
    /*
     * Test method for 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int, int)'
     * 
     * test the dummy data return functionality
     * 
     */
    public void testSearchDummies() throws Exception {
        
        ManagementIPlug iplug = new ManagementIPlug();
        MessageDigestCredentialPasswordEncoder penc = new MessageDigestCredentialPasswordEncoder();
        String digest = penc.encode("admin_partner", "admin");
        IngridQuery q = QueryStringParser.parse("datatype:management login:admin_partner digest:"+ digest + " management_request_type:815");
        IngridHits hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 1L);
        IngridHit hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String)hit.get("role"), "admin_partner");
        String[] partner = new String[] {"he", "st"};
        for (int i=0; i<partner.length; i++ ) {
            assertEquals(partner[i], (String)hit.getArray("partner")[i]);
        }
        assertNull(hit.getArray("provider"));
        
        digest = penc.encode("admin_provider", "admin");
        q = QueryStringParser.parse("datatype:management login:admin_provider digest:"+ digest + " management_request_type:815");
        hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 1L);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String)hit.get("role"), "admin_provider");
        assertNull(hit.getArray("partner"));
        String[] provider = new String[] {"bu_bmu", "bu_uba", "he_hmulv"};
        for (int i=0; i<provider.length; i++ ) {
            assertEquals(provider[i], (String)hit.getArray("provider")[i]);
        }

        digest = penc.encode("admin_themes_measure", "admin");
        q = QueryStringParser.parse("datatype:management login:admin_themes_measure digest:"+ digest + " management_request_type:815");
        hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 2L);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String)hit.get("role"), "admin_themes");
        assertNull(hit.getArray("partner"));
        provider = new String[] {"bu_bmu", "bu_uba", "he_hmulv"};
        for (int i=0; i<provider.length; i++ ) {
            assertEquals(provider[i], (String)hit.getArray("provider")[i]);
        }
        hit = hits.getHits()[1];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String)hit.get("role"), "admin_measure");
        assertNull(hit.getArray("partner"));
        provider = new String[] {"bu_bmu", "bu_uba", "he_hmulv"};
        for (int i=0; i<provider.length; i++ ) {
            assertEquals(provider[i], (String)hit.getArray("provider")[i]);
        }

        
    }
    
    
}
