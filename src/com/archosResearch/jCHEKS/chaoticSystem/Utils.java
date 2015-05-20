package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class Utils {
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    public static int GetRandomInt(int bound) {
        int result = 0;
        
        try {
            result = SecureRandom.getInstance("SHA1PRNG").nextInt(bound+1);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
    
    public static boolean QuarterShot() {
        int result = 0;
        
        try {
            result = SecureRandom.getInstance("SHA1PRNG").nextInt(2);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result == 0;
    }
    
    public static String ByteArrayToString(byte[] byteArray) {
        return Arrays.toString(byteArray);
    }
    
    public static byte[] StringToByteArray(String s) {
        String[] byteValues = s.substring(1, s.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for (int i=0, len=bytes.length; i<len; i++) {
           bytes[i] = Byte.parseByte(byteValues[i].trim());     
        }

        return bytes;
    }
}
