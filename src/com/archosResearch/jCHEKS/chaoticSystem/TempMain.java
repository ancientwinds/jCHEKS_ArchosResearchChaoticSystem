/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.chaoticSystem.exception.KeyLenghtException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author etudiant
 */
public class TempMain {
    public static void main(String [] args) throws KeyLenghtException, NoSuchAlgorithmException, Exception
    {
        CryptoChaoticSystem system = new CryptoChaoticSystem(256, "test");
        system.evolveSystem();
        FileReader.saveChaoticSystem("newXml.xml", system);
    }
}
