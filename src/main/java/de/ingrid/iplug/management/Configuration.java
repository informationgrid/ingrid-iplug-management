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

@PropertiesFiles( {"config"} )
@PropertyLocations(directories = {"conf"}, fromClassLoader = true)
public class Configuration implements IConfig {
    
    private static Log log = LogFactory.getLog(Configuration.class);
    
    @PropertyValue("plugdescription.fields")
    public String fields;
    
    @PropertyValue("plugdescription.isRecordLoader")
    @DefaultValue("false")
    public boolean recordLoader;
    
    @Override
    public void initialize() {
    }

    @Override
    public void addPlugdescriptionValues( PlugdescriptionCommandObject pdObject ) {
        pdObject.put( "iPlugClass", "de.ingrid.iplug.management.ManagementIPlug" );
        
        if(pdObject.getFields().length == 0){
        	String[] fieldsList = fields.split(",");
    		for(String field : fieldsList){
    			pdObject.addField(field);
    		}
        }
        
        pdObject.setRecordLoader(recordLoader);
    }

    @Override
    public void setPropertiesFromPlugdescription( Properties props, PlugdescriptionCommandObject pd ) {
    }
}
