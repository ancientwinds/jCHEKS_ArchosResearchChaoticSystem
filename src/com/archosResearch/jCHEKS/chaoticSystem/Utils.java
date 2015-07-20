package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author jean-francois
 */
public class Utils {
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    public static void main(String[] args) throws Exception {
        for(int i = 0; i < 1000; i++) {
            System.out.println(GetRandInt(new Range(-128, 127), new Random()));
        }
    }
    public static int GetRandomInt(int bound, Random random) {
        int result = random.nextInt(bound + 1);
        
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
        return random.nextInt((range.getMax() - range.getMin()) + 1) + range.getMin();
    }

    public static boolean QuarterShot(Random random) {
        int result = random.nextInt(2);
        
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
    
    public static int adjustNumber(Range range, int number) {
        
        int rangeLength = range.getMax() - range.getMin() + 1;

        if (number > range.getMax()) {  
            number = range.getMin() + (number + Math.abs(range.getMin()) - (rangeLength * ((number + Math.abs(range.getMin())) / rangeLength)));
                    
        }
        if (number < range.getMin()) {            
            number =  range.getMax() + (number - range.getMax() - (rangeLength * ((number - range.getMax()) / rangeLength)));
        }

        return number;
    }
}
