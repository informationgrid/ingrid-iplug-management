/*
 * **************************************************-
 * Ingrid Management iPlug
 * ==================================================
 * Copyright (C) 2014 - 2018 wemove digital solutions GmbH
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.security.spi.CredentialPasswordEncoder;
import org.apache.jetspeed.security.spi.PasswordCredentialProvider;

import de.ingrid.iplug.management.util.ManagementUtils;
import de.ingrid.usermanagement.jetspeed.IngridCredentialHandler;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.query.IngridQuery;

/**
 * Delivers dummy data for authentication tests.
 * 
 * @author joachim@wemove.com
 */
public class ManagementDummyAuthenticationUseCase implements ManagementUseCase {

    /**
     * The logging object
     */
    private static Log log = LogFactory.getLog(ManagementDummyAuthenticationUseCase.class);

    /**
     * @see de.ingrid.iplug.management.usecase.ManagementUseCase#execute(de.ingrid.utils.query.IngridQuery,
     *      int, int)
     */
    public IngridHit[] execute(IngridQuery query, int start, int length, String plugId) {

        IngridHit hit = null;
        String login = null;
        String digest = null;
        IngridCredentialHandler ch = null;
        IngridHit[] result = null;

        try {
            // get login and passwd from query
            login = ManagementUtils.getField(query, "login");
            digest = ManagementUtils.getField(query, "digest");
            ch = new IngridCredentialHandler();
            PasswordCredentialProvider pc = ch.getPCProvider();
            CredentialPasswordEncoder cpe = pc.getEncoder();

            hit = new IngridHit(plugId, "0", 0, 1.0f);
            if (login.equalsIgnoreCase("admin_partner") && digest.equals(cpe.encode("admin_partner", "admin"))) {
                // build return value
                result = new IngridHit[1];
                // hit authenticated
                hit.putBoolean("authenticated", true);
                // hits permission
                hit.put("permission", new String("admin.portal.partner"));
                // hits partners
                hit.setArray("partner", new String[] { "he", "st" });
                // hits providers

                result[0] = hit;
            } else if (login.equalsIgnoreCase("admin_provider") && digest.equals(cpe.encode("admin_provider", "admin"))) {
                // build return value
                result = new IngridHit[1];
                // hit authenticated
                hit.putBoolean("authenticated", true);
                // hits role
                hit.put("permission", new String("admin.portal.partner.provider.index"));
                // hits partners
                hit.setArray("partner", new String[] { "bund" });
                // hits providers
                hit.setArray("provider", new String[] { "bu_bmu", "bu_uba", "he_hmulv" });
                result[0] = hit;
            } else if (login.equalsIgnoreCase("admin_themes_measure")
                    && digest.equals(cpe.encode("admin_themes_measure", "admin"))) {
                // build return value
                result = new IngridHit[2];
                // hit authenticated
                hit.putBoolean("authenticated", true);
                // hits role
                hit.put("permission", new String("admin.portal.partner.provider.catalog"));
                // hits partners
                hit.setArray("partner", new String[] { "bund" });
                // hits providers
                hit.setArray("provider", new String[] { "bu_bmu", "bu_uba", "he_hmulv" });
                result[0] = hit;

                hit = new IngridHit(plugId, "0", 0, 1.0f);
                // hit authenticated
                hit.putBoolean("authenticated", true);
                // hits role
                hit.put("permission", new String("admin.portal.partner.provider.index"));
                // hits partners
                hit.setArray("partner", new String[] { "bund" });
                // hits providers
                hit.setArray("provider", new String[] { "bu_bmu", "bu_uba", "he_hmulv" });

                result[1] = hit;
            } else if (login.equalsIgnoreCase("admin_index") && digest.equals(cpe.encode("admin_index", "admin"))) {
                // build return value
                result = new IngridHit[1];
                // hit authenticated
                hit.putBoolean("authenticated", true);
                // hits role
                hit.put("permission", new String("admin.portal.partner.provider.index"));
                // hits partners
                hit.setArray("partner", new String[] { "bund" });
                // hits providers
                hit.setArray("provider", new String[] { "bu_bmu", "bu_uba" });
                result[0] = hit;
            } else {
                // hit authenticated
                hit.putBoolean("authenticated", false);
                result = new IngridHit[0];
                result[0] = hit;
            }
        } catch (Throwable t) {
            log.error("Error executing " + this.getClass().getName() + "!", t);
        }
        return result;
    }

}
