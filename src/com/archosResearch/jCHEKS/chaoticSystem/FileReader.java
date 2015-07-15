package com.archosResearch.jCHEKS.chaoticSystem;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class FileReader {
    
    private static final String chaoticSystemDir = "system/";
    
    public static ChaoticSystem readChaoticSystem(String fileName) throws Exception {
        File dir = new File(chaoticSystemDir);
        dir.mkdirs();
        String fileToSave = chaoticSystemDir + fileName;
        String extension = getFileExtension(fileName);
        
        ChaoticSystem system;

        if(extension.equals("xml")) {            
            system = ChaoticSystem.deserializeXML(readFile(fileToSave, null));
        } else if(extension.equals("sre")) {
            system = new ChaoticSystem();
            system.deserialize(readFile(fileToSave, null));
        } else {
            throw new Exception("File not compatible");
        }  
        
        return system;  
    }
    
    public static void saveChaoticSystem(String fileName, ChaoticSystem system) throws Exception {
        File dir = new File(chaoticSystemDir);
        dir.mkdirs();
        String extension = getFileExtension(fileName);
        String serialized = "";
        
        if(extension.equals("xml")) {
            serialized = system.serializeXML();
        } else if(extension.equals("sre")) {
            serialized = system.serialize();
        } else {
            throw new Exception("File not compatible");
        } 
        
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(chaoticSystemDir + fileName), "utf-8"))) {
                writer.write(serialized);         
            }
    }
    
    private static String readFile(String path, Charset encoding) throws IOException 
    {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, Charset.defaultCharset());
    }
   
    private static String getFileExtension(String file) {
        String[] name = file.split("\\.");
        return name[name.length-1];     
    }


}
