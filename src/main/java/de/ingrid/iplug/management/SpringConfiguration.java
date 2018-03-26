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
package de.ingrid.iplug.management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.comm.HttpCLCommunication;
import de.ingrid.codelists.comm.ICodeListCommunication;
import de.ingrid.codelists.comm.IngridCLCommunication;

@Configuration
public class SpringConfiguration {

    @Bean
    public ICodeListCommunication httpCommunication() {
        HttpCLCommunication communication = new HttpCLCommunication();
        communication.setRequestUrl( ManagementIPlug.conf.codelistRequestUrl );
        communication.setUsername( ManagementIPlug.conf.codelistUsername );
        communication.setPassword( ManagementIPlug.conf.codelistPassword );
        return communication;
    }

    @Bean
    public CodeListService codeListService() {
        CodeListService service = new CodeListService();

        ICodeListCommunication comm = null;
        if ("ingrid".equals( ManagementIPlug.conf.codelistCommunicationType )) {
            comm = new IngridCLCommunication();
        } else {
            HttpCLCommunication communication = new HttpCLCommunication();
            communication.setRequestUrl( ManagementIPlug.conf.codelistRequestUrl );
            communication.setUsername( ManagementIPlug.conf.codelistUsername );
            communication.setPassword( ManagementIPlug.conf.codelistPassword );
            comm = communication;
        }
        service.setComm( comm );
        service.setDefaultPersistency( ManagementIPlug.conf.codelistDefaultPersistency );
        return service;
    }

}
