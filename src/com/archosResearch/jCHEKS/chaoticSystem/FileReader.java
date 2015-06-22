/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archosResearch.jCHEKS.chaoticSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class FileReader {
    
    public ChaoticSystem readChaoticSystem(String fileName) throws Exception {
        File dir = new File("system/");
        dir.mkdirs();
        ChaoticSystem system = new ChaoticSystem(128);
        String extension = this.getFileExtension(fileName);
        if(extension.equals("xml")) {            
            system.deserializeXML(new File("system/" + fileName));
            return system;
        } else if(extension.equals("sre")) {
            system.Deserialize(readFile("system/" + fileName, null));
            return system;  
        }        
        throw new Exception("File not compatible");
    }
    
    public void saveChaoticSystem(String fileName, ChaoticSystem system) throws Exception {
        File dir = new File("system/");
        dir.mkdirs();
        String extension = this.getFileExtension(fileName);
        if(extension.equals("xml")) {
            Document doc = system.serializeXML();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("system/" + fileName));
            transformer.transform(source, result);
        } else if(extension.equals("sre")) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("system/" + fileName), "utf-8"))) {
                writer.write(system.Serialize());         
            }
        }
    }
    
    static String readFile(String path, Charset encoding) throws IOException 
    {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, Charset.defaultCharset());
    }
   
    private String getFileExtension(String file) {
        String[] name = file.split("\\.");
        return name[1];     
    }


}
