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

import junit.framework.TestCase;

import org.apache.jetspeed.security.spi.impl.MessageDigestCredentialPasswordEncoder;

import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;

public class ManagementIPlugAuthenticateTest extends TestCase {

    /*
     * Test method for
     * 'de.ingrid.iplug.management.ManagementIPlug.search(IngridQuery, int,
     * int)'
     * 
     * test the dummy data return functionality
     * 
     */
    public void testSearchDummies() throws Exception {

        ManagementIPlug iplug = new ManagementIPlug();
        MessageDigestCredentialPasswordEncoder penc = new MessageDigestCredentialPasswordEncoder();
        String digest = penc.encode("admin_partner", "admin");
        IngridQuery q = QueryStringParser.parse("datatype:management login:admin_partner digest:" + digest
                + " management_request_type:815");
        IngridHits hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 1L);
        IngridHit hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner");
        String[] partner = new String[] { "he", "st" };
        for (int i = 0; i < partner.length; i++) {
            assertEquals(partner[i], (String) hit.getArray("partner")[i]);
        }
        assertNull(hit.getArray("provider"));

        digest = penc.encode("admin_provider", "admin");
        q = QueryStringParser.parse("datatype:management login:admin_provider digest:" + digest
                + " management_request_type:815");
        hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 1L);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.index");
        partner = new String[] { "bund" };
        for (int i = 0; i < partner.length; i++) {
            assertEquals(partner[i], (String) hit.getArray("partner")[i]);
        }
        String[] provider = new String[] { "bu_bmu", "bu_uba", "he_hmulv" };
        for (int i = 0; i < provider.length; i++) {
            assertEquals(provider[i], (String) hit.getArray("provider")[i]);
        }

        digest = penc.encode("admin_themes_measure", "admin");
        q = QueryStringParser.parse("datatype:management login:admin_themes_measure digest:" + digest
                + " management_request_type:815");
        hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 2L);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.catalog");
        partner = new String[] { "bund" };
        for (int i = 0; i < partner.length; i++) {
            assertEquals(partner[i], (String) hit.getArray("partner")[i]);
        }
        provider = new String[] { "bu_bmu", "bu_uba", "he_hmulv" };
        for (int i = 0; i < provider.length; i++) {
            assertEquals(provider[i], (String) hit.getArray("provider")[i]);
        }
        hit = hits.getHits()[1];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.index");
        partner = new String[] { "bund" };
        for (int i = 0; i < partner.length; i++) {
            assertEquals(partner[i], (String) hit.getArray("partner")[i]);
        }
        provider = new String[] { "bu_bmu", "bu_uba", "he_hmulv" };
        for (int i = 0; i < provider.length; i++) {
            assertEquals(provider[i], (String) hit.getArray("provider")[i]);
        }

        digest = penc.encode("admin_index", "admin");
        q = QueryStringParser.parse("datatype:management login:admin_index digest:" + digest
                + " management_request_type:815");
        hits = iplug.search(q, 0, 100);
        assertEquals(hits.length(), 1L);
        hit = hits.getHits()[0];
        assertEquals(hit.getBoolean("authenticated"), true);
        assertEquals((String) hit.get("permission"), "admin.portal.partner.provider.index");
        partner = new String[] { "bund" };
        for (int i = 0; i < partner.length; i++) {
            assertEquals(partner[i], (String) hit.getArray("partner")[i]);
        }
        provider = new String[] { "bu_bmu", "bu_uba" };
        for (int i = 0; i < provider.length; i++) {
            assertEquals(provider[i], (String) hit.getArray("provider")[i]);
        }

    }

}
