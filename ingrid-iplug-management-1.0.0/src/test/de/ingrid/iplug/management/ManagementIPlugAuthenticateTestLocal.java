/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.iplug.management;

import junit.framework.TestCase;

import org.apache.jetspeed.security.spi.impl.MessageDigestCredentialPasswordEncoder;

import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;

public class ManagementIPlugAuthenticateTestLocal extends TestCase {

    /*
     * Test method for
     * 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int,
     * int)'
     */
    public void testSearch() throws Exception {

        ManagementIPlug iplug = new ManagementIPlug();
        MessageDigestCredentialPasswordEncoder penc = new MessageDigestCredentialPasswordEncoder();

        // test encrypted digest login
        String digest = penc.encode("user", "user");
        IngridQuery q = QueryStringParser.parse("datatype:management login:user digest:" + digest
                + " management_request_type:0");
        IngridHits hits = iplug.search(q, 0, 1);
        IngridHit hit = hits.getHits()[0];
        boolean result = hit.getBoolean("authenticated");
        assertEquals(result, true);

        digest = penc.encode("admin", "admin");
        q = QueryStringParser.parse("datatype:management login:admin digest:" + digest + " management_request_type:0");
        hits = iplug.search(q, 0, 1);
        hit = hits.getHits()[0];
        result = hit.getBoolean("authenticated");
        assertEquals(result, true);

        // test wrong digest
        digest = penc.encode("admin", "wrong");
        q = QueryStringParser.parse("datatype:management login:admin digest:" + digest + " management_request_type:0");
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

        // test partner returns
        q = QueryStringParser
                .parse("datatype:management login:adminpartner digest:adminpartner management_request_type:0");
        hits = iplug.search(q, 0, 100);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner");
        String[] testArray = new String[] { "he" };
        for (int i = 0; i < hit.getArray("partner").length; i++) {
            assertEquals(hit.getArray("partner")[i], testArray[i]);
        }

        // test portal admin returns
        q = QueryStringParser
                .parse("datatype:management login:adminportal digest:adminportal management_request_type:0");
        hits = iplug.search(q, 0, 100);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal");

        // test provider returns
        q = QueryStringParser
                .parse("datatype:management login:adminprovider digest:adminprovider management_request_type:0");
        hits = iplug.search(q, 0, 100);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.index");
        testArray = new String[] { "he" };
        for (int i = 0; i < hit.getArray("partner").length; i++) {
            assertEquals(hit.getArray("partner")[i], testArray[i]);
        }
        testArray = new String[] { "he_hmulv", "he_hlug" };
        for (int i = 0; i < hit.getArray("provider").length; i++) {
            assertEquals(hit.getArray("provider")[i], testArray[i]);
        }

        hit = hits.getHits()[1];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.catalog");
        testArray = new String[] { "he" };
        for (int i = 0; i < hit.getArray("partner").length; i++) {
            assertEquals(hit.getArray("partner")[i], testArray[i]);
        }
        testArray = new String[] { "he_hmulv", "he_hlug" };
        for (int i = 0; i < hit.getArray("provider").length; i++) {
            assertEquals(hit.getArray("provider")[i], testArray[i]);
        }

        // stress test
        for (int i = 0; i < 500; i++) {
            q = QueryStringParser
                    .parse("datatype:management login:adminportal digest:adminportal management_request_type:0");
            System.out.println(i);
            hits = iplug.search(q, 0, 100);
            hit = hits.getHits()[0];
            assertEquals(hit.getBoolean("authenticated"), true);
            assertEquals((String) hit.get("permission"), "admin.portal");

            q = QueryStringParser
                    .parse("datatype:management login:adminprovider digest:adminprovider management_request_type:0");
            hits = iplug.search(q, 0, 100);
            hit = hits.getHits()[0];
            assertEquals(hit.getBoolean("authenticated"), true);
            assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.index");
            testArray = new String[] { "he" };
            for (int j = 0; j < hit.getArray("partner").length; j++) {
                assertEquals(hit.getArray("partner")[j], testArray[j]);
            }
            testArray = new String[] { "he_hmulv", "he_hlug" };
            for (int j = 0; j < hit.getArray("provider").length; j++) {
                assertEquals(hit.getArray("provider")[j], testArray[j]);
            }

            hit = hits.getHits()[1];
            assertEquals(hit.getBoolean("authenticated"), true);
            assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.catalog");
            testArray = new String[] { "he" };
            for (int j = 0; j < hit.getArray("partner").length; j++) {
                assertEquals(hit.getArray("partner")[j], testArray[j]);
            }
            testArray = new String[] { "he_hmulv", "he_hlug" };
            for (int j = 0; j < hit.getArray("provider").length; j++) {
                assertEquals(hit.getArray("provider")[j], testArray[j]);
            }

        }

    }

}
