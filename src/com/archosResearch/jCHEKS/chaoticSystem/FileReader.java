/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class FileReader {
    
    public ChaoticSystem readChaoticSystem(String fileName) throws Exception {
        ChaoticSystem system = new ChaoticSystem("", 128);
        String extension = this.getFileExtension(fileName);
        if(extension.equals("xml")) {            
            system.deserializeXML(new File(fileName));
            return system;
        } else if(extension.equals("sre")) {
            system.Deserialize(readFile(fileName, null));
            return system;  
        }        
        throw new Exception("File not compatible");
    }
    
    public void saveChaoticSystem(String fileName, ChaoticSystem system) {
        try {
            String extension = this.getFileExtension(fileName);
            BufferedWriter writer = new BufferedWriter( new FileWriter(fileName));
            if(extension.equals("xml")) {
                system.serializeXML();
            } else if(extension.equals("sre")) {
                writer.write(system.Serialize());
            }
        } catch (IOException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
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
