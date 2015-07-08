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
