package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.chaoticSystem.exception.KeyLenghtException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *
 * @author etudiant
 */
public class TempMain {
    public static void main(String [] args) throws KeyLenghtException, NoSuchAlgorithmException, Exception
    {
        ChaoticSystem system = new CryptoChaoticSystem(24, new Random("d".hashCode()));
        system.evolveSystem();
        FileReader.saveChaoticSystem("system.xml", system);
        ChaoticSystem system2 = FileReader.readChaoticSystem("system.xml");
        FileReader.saveChaoticSystem("system2.xml", system2);
        System.out.println(system2.equals(system));
    }
}
