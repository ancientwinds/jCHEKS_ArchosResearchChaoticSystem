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

    private final static Range impactRange = new Range(0, 32);
    private final static Range keyPartRange = new Range(-128, 127);
    private final static Range delayRange = new Range(0 ,4);
    
    public CryptoChaoticSystem(int keyLength, String systemId) throws KeyLenghtException, NoSuchAlgorithmException {
        super(keyLength, systemId, impactRange, keyPartRange, delayRange, SecureRandom.getInstance("SHA1PRNG"));
    }
    
    public CryptoChaoticSystem(int keyLength, Random random) throws KeyLenghtException {
        super(keyLength, UUID.nameUUIDFromBytes(Integer.toString(random.nextInt()).getBytes()).toString(), impactRange, keyPartRange, delayRange, random);

    }
}
