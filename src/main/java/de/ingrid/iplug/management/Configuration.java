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

import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
    
    @PropertyValue("portal.db.url")
    @DefaultValue("//localhost/ingrid_portal")
    public String portalDbUrl;

    @PropertyValue("portal.db.username")
    public String portalDbUsername;

    @PropertyValue("portal.db.password")
    public String portalDbPassword;

    @Override
    public void initialize() {
        if (portalDbUsername == null) {
            mapParamsFromRepositoryFile();
        } else {
            saveChangesInFile();
        }
        
    }

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
    public void setPropertiesFromPlugdescription(Properties props, PlugdescriptionCommandObject pd) {
        props.setProperty( "portal.db.url", portalDbUrl);
        props.setProperty( "portal.db.username", portalDbUsername);
        props.setProperty( "portal.db.password", portalDbPassword);
        
        this.saveChangesInFile();
    }
    
    private void saveChangesInFile() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            ClassPathResource input = new ClassPathResource("repository_database.xml");
            Document document = builder.parse(input.getInputStream());
            Element node = (Element)document.getElementsByTagName("jdbc-connection-descriptor").item(0);
            node.setAttribute("dbalias", portalDbUrl);
            node.setAttribute("username", portalDbUsername);
            node.setAttribute("password", portalDbPassword);
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(input.getURI().getPath()); 
            transformer.transform(source, result);
            
                        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
private void mapParamsFromRepositoryFile() {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            ClassPathResource input = new ClassPathResource("repository_database.xml");
            Document document = builder.parse(input.getInputStream());
            Node node = document.getElementsByTagName("jdbc-connection-descriptor").item(0);
            portalDbUrl = node.getAttributes().getNamedItem("dbalias").getTextContent();
            portalDbUsername = node.getAttributes().getNamedItem("username").getTextContent();
            portalDbPassword = node.getAttributes().getNamedItem("password").getTextContent();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
