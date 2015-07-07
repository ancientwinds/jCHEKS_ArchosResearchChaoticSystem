package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">
import java.util.Arrays;
import java.util.Random;
//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class Utils {
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static Random generator = new Random(System.currentTimeMillis());
    
    public static int GetRandomInt(int bound) {
        int result = 0;
        
        //result = SecureRandom.getInstance("SHA1PRNG").nextInt(bound+1);
        result = generator.nextInt(bound + 1);
        
        return result;
    }
    
    public static void setSeed(String seed) {      
        generator = new Random(seed.hashCode());
    }
    
    public static void resetSeed() {
        generator = new Random(System.currentTimeMillis());
    }
    
    public static boolean QuarterShot() {
        int result = 0;

        //result = SecureRandom.getInstance("SHA1PRNG").nextInt(2);
        result = generator.nextInt(2);

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
