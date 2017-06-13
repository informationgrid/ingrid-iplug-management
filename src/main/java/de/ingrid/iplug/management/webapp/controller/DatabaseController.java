/*
 * **************************************************-
 * Ingrid Management iPlug
 * ==================================================
 * Copyright (C) 2014 - 2017 wemove digital solutions GmbH
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
package de.ingrid.iplug.management.webapp.controller;

import org.apache.ojb.broker.metadata.JdbcConnectionDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.management.Configuration;
import de.ingrid.iplug.management.ManagementIPlug;
import de.ingrid.iplug.management.webapp.object.DbSettings;

@Controller
@SessionAttributes("plugDescription")
public class DatabaseController {
    

    @RequestMapping(value = { "/iplug-pages/welcome.html", "/iplug-pages/dbSettings.html" }, method = RequestMethod.GET)
    public String get(final ModelMap modelMap, @ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugDescription) {
        
        Configuration conf = ManagementIPlug.conf;
        DbSettings dbSettings = new DbSettings();
        dbSettings.setDbUrl( conf.portalDbUrl );
        dbSettings.setDbUsername( conf.portalDbUsername );
        dbSettings.setDbPassword( conf.portalDbPassword );
        
        // write object into session
        modelMap.addAttribute("dbSettings", dbSettings);
        
        return "/iplug-pages/dbSettings";
    }

    @RequestMapping(value = "/iplug-pages/dbSettings.html", method = RequestMethod.POST)
    public String post(@ModelAttribute("dbSettings") final DbSettings dbSettings,
            @ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugDescription) {
        
        reloadDatabaseChanges(dbSettings);
        
        Configuration conf = ManagementIPlug.conf;
        conf.portalDbUrl = dbSettings.getDbUrl();
        conf.portalDbUsername = dbSettings.getDbUsername();
        conf.portalDbPassword = dbSettings.getDbPassword();
        
        return "redirect:/base/save.html";
    }

    private void reloadDatabaseChanges(DbSettings settings) {
        // get MetadataManager instance
        MetadataManager mm = MetadataManager.getInstance();
        
        JdbcConnectionDescriptor descriptor = mm.connectionRepository().getDescriptor(mm.getDefaultPBKey());
        descriptor.setDbAlias(settings.getDbUrl());
        descriptor.setUserName(settings.getDbUsername());
        descriptor.setPassWord(settings.getDbPassword());
        
    }
    
}
