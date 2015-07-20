package com.archosResearch.jCHEKS.chaoticSystem;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author etudiant
 */
public class UtilsTest {
    
    private static final String bytesInString = "[65, 108, 108, 111]";
    private static final byte[] bytes = {65, 108, 108, 111};
    
    @Test
    public void testByteArrayToString() {
        String result = Utils.ByteArrayToString(bytes);
        assertEquals(result, bytesInString);
    }

    @Test
    public void testStringToByteArray() {
        byte[] result = Utils.StringToByteArray(bytesInString);
        assertArrayEquals(result, bytes);
    }
    
    @Test
    public void adjust_test_1(){
        Range range = new Range(-128, 127);
        
        assertEquals(-128, Utils.adjustNumber(range, -128));
        assertEquals(127, Utils.adjustNumber(range, 127));
        assertEquals(0, Utils.adjustNumber(range, 0));
        assertEquals(57, Utils.adjustNumber(range, 57));
        assertEquals(127, Utils.adjustNumber(range, -129));
        assertEquals(-128, Utils.adjustNumber(range, 128));
        assertEquals(-128, Utils.adjustNumber(range, 384));
        assertEquals(-127, Utils.adjustNumber(range, 385));        
        assertEquals(0, Utils.adjustNumber(range, 256));
        assertEquals(56, Utils.adjustNumber(range, -200));
        assertEquals(44, Utils.adjustNumber(range, 300));
        assertEquals(4, Utils.adjustNumber(range, 1028));
        assertEquals(127, Utils.adjustNumber(range, 127));
        assertEquals(-128, Utils.adjustNumber(range, 128));
        assertEquals(0, Utils.adjustNumber(range, 256));
        assertEquals(-2, Utils.adjustNumber(range, 254));
        assertEquals(-128, Utils.adjustNumber(range, -384));        

    }
    
    @Test
    public void adjust_test_2() {
        Range range = new Range(-4, 2);
        
        assertEquals(2, Utils.adjustNumber(range, 2));
        assertEquals(0, Utils.adjustNumber(range, 7));
        assertEquals(-4, Utils.adjustNumber(range, 3));
        assertEquals(0, Utils.adjustNumber(range, 14));
        assertEquals(0, Utils.adjustNumber(range, 21));        
        assertEquals(2, Utils.adjustNumber(range, -5));
        assertEquals(1, Utils.adjustNumber(range, -6));
        assertEquals(-1, Utils.adjustNumber(range, -15));
    }
    
}
