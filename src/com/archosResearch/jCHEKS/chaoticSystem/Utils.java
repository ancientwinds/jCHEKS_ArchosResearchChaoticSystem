package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author jean-francois
 */
public class Utils {
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    public static int GetRandomInt(int bound, Random random) {
        int result = 0;

        result = random.nextInt(bound + 1);
        
        return result;
    }
    
    public static int GetRandomInt(Range range, Random random) {        
        return GetRandInt(range, random);
    }
    
    public static int GetRandomIntAvoidingZero(Range range, Random random) {
        int result = GetRandInt(range, random);
        
        while(result == 0) { 
            result = GetRandInt(range, random);
        }
        return result;
    }
    
    private static int GetRandInt(Range range, Random random) {
        return random.nextInt((range.max - range.min) + 1) + range.min;
    }

    public static boolean QuarterShot(Random random) {
        int result = 0;

        result = random.nextInt(2);

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
