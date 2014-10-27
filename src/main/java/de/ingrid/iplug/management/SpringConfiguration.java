package de.ingrid.iplug.management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.comm.HttpCLCommunication;
import de.ingrid.codelists.comm.ICodeListCommunication;
import de.ingrid.codelists.comm.IngridCLCommunication;

@Configuration
public class SpringConfiguration {

    // <bean id="httpCommunication"
    // class="de.ingrid.codelists.comm.HttpCLCommunication">
    // <property name="requestUrl"
    // value="http://localhost:8089/rest/getCodelists" />
    // <property name="username" value="admin" />
    // <property name="password" value="admin" />
    // </bean>
    // <bean id="codeListService" class="de.ingrid.codelists.CodeListService">
    // <!-- <property name="persistencies"> -->
    // <!-- <list> -->
    // <!-- <ref bean="xmlPersistency" /> -->
    // <!-- </list> -->
    // <!-- </property> -->
    // <property name="defaultPersistency" value="-1" />
    // </bean>

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
