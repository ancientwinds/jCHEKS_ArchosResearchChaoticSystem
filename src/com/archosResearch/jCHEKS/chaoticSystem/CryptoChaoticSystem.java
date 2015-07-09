package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.chaoticSystem.exception.KeyLenghtException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;


/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class CryptoChaoticSystem extends ChaoticSystem{
    
    private final static int MIN_IMPACT = 0;
    private final static int MAX_IMPACT = 32;
    private final static int MIN_KEY_PART = -128;
    private final static int MAX_KEY_PART = 127;
    private final static int MAX_DELAY = 4;
    
    public CryptoChaoticSystem(int keyLength, String systemId) throws KeyLenghtException, NoSuchAlgorithmException {
        super(keyLength, systemId, MIN_IMPACT, MAX_IMPACT, MIN_KEY_PART, MAX_KEY_PART, MAX_DELAY, SecureRandom.getInstance("SHA1PRNG"));
    }
    
    public CryptoChaoticSystem(int keyLength, Random random) throws KeyLenghtException {
        super(keyLength, UUID.nameUUIDFromBytes(Integer.toString(random.nextInt()).getBytes()).toString(), MIN_IMPACT, MAX_IMPACT, MIN_KEY_PART, MAX_KEY_PART, MAX_DELAY, random);

    }
}
