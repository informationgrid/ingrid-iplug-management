/*
 * **************************************************-
 * Ingrid Management iPlug
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
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

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.ojb.broker.metadata.JdbcConnectionDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.management.webapp.object.DbSettings;

@Controller
@SessionAttributes("plugDescription")
public class DatabaseController {
    

    @RequestMapping(value = { "/iplug-pages/welcome.html", "/iplug-pages/dbSettings.html" }, method = RequestMethod.GET)
    public String get(final ModelMap modelMap, @ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugDescription) {
        
        DbSettings dbSettings = new DbSettings();
        
        mapParamsFromRepositoryFile(dbSettings);       
        
        // write object into session
        modelMap.addAttribute("dbSettings", dbSettings);
        
        return "/iplug-pages/dbSettings";
    }
    
    private void mapParamsFromRepositoryFile(DbSettings dbSettings) {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            ClassPathResource input = new ClassPathResource("repository_database.xml");
            Document document = builder.parse(input.getInputStream());
            Node node = document.getElementsByTagName("jdbc-connection-descriptor").item(0);
            dbSettings.setDbUrl(node.getAttributes().getNamedItem("dbalias").getTextContent());
            dbSettings.setDbUsername(node.getAttributes().getNamedItem("username").getTextContent());
            dbSettings.setDbPassword(node.getAttributes().getNamedItem("password").getTextContent());
            
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/iplug-pages/dbSettings.html", method = RequestMethod.POST)
    public String post(@ModelAttribute("dbSettings") final DbSettings dbSettings,
            @ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugDescription) {
        
        saveChangesInFile(dbSettings);
        
        reloadDatabaseChanges(dbSettings);
        
        return "redirect:/base/save.html";
    }

    private void saveChangesInFile(DbSettings settings) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            ClassPathResource input = new ClassPathResource("repository_database.xml");
            Document document = builder.parse(input.getInputStream());
            Element node = (Element)document.getElementsByTagName("jdbc-connection-descriptor").item(0);
            node.setAttribute("dbalias", settings.getDbUrl());
            node.setAttribute("username", settings.getDbUsername());
            node.setAttribute("password", settings.getDbPassword());
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(input.getURI().getPath()); 
            transformer.transform(source, result);
            
                        
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private void reloadDatabaseChanges(DbSettings settings) {
        // get MetadataManager instance
        MetadataManager mm = MetadataManager.getInstance();
        
        JdbcConnectionDescriptor descriptor = mm.connectionRepository().getDescriptor(mm.getDefaultPBKey());
        descriptor.setDbAlias(settings.getDbUrl());
        descriptor.setUserName(settings.getDbUsername());
        descriptor.setPassWord(settings.getDbPassword());
        
    }
    
    
    
    
//    
//    @Override
//    @RequestMapping(value = IUris.WORKING_DIR, method = RequestMethod.GET)
//    public String getWorkingDir(@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugDescription) {
//        return IViews.WORKING_DIR;
//    }
//
//    @Override
//    @RequestMapping(value = IUris.WORKING_DIR, method = RequestMethod.POST)
//    public String postWorkingDir(@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugDescription, final BindingResult errors) {
//        if (_validator.validateWorkingDir(errors).hasErrors()) {
//            return IViews.WORKING_DIR;
//        }
//        plugDescription.getWorkinDirectory().mkdirs();
//        return redirect(IUris.GENERAL);
//    }
}
