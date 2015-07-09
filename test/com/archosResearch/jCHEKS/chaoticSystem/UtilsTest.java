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
    
}
