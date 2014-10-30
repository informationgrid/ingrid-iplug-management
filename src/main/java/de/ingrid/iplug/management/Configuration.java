package de.ingrid.iplug.management;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tngtech.configbuilder.annotation.propertyloaderconfiguration.PropertiesFiles;
import com.tngtech.configbuilder.annotation.propertyloaderconfiguration.PropertyLocations;
import com.tngtech.configbuilder.annotation.valueextractor.DefaultValue;
import com.tngtech.configbuilder.annotation.valueextractor.PropertyValue;

import de.ingrid.admin.IConfig;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.utils.PlugDescription;

@PropertiesFiles({ "config" })
@PropertyLocations(directories = { "conf" }, fromClassLoader = true)
public class Configuration implements IConfig {

    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog( Configuration.class );

    @PropertyValue("codelist.requestUrl")
    @DefaultValue("http://localhost:8089/rest/getCodelists")
    public String codelistRequestUrl;

    @PropertyValue("codelist.username")
    public String codelistUsername;

    @PropertyValue("codelist.password")
    public String codelistPassword;

    @PropertyValue("codelist.communicationType")
    @DefaultValue("http")
    public String codelistCommunicationType;

    @PropertyValue("codelist.defaultPersistency")
    @DefaultValue("-1")
    public int codelistDefaultPersistency;

    @Override
    public void initialize() {}

    @Override
    public void addPlugdescriptionValues(PlugdescriptionCommandObject pdObject) {
        pdObject.put( "iPlugClass", "de.ingrid.iplug.management.ManagementIPlug" );
        
        pdObject.removeFromList(PlugDescription.FIELDS, "login");
        pdObject.addField("login");
        pdObject.removeFromList(PlugDescription.FIELDS, "digest");
        pdObject.addField("digest");
        pdObject.removeFromList(PlugDescription.FIELDS, "management_request_type");
        pdObject.addField("management_request_type");
    }

    @Override
    public void setPropertiesFromPlugdescription(Properties props, PlugdescriptionCommandObject pd) {}
}
