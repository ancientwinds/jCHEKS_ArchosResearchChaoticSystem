package com.archosResearch.jCHEKS.chaoticSystem.exception;

import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class KeyGenerationException extends ChaoticSystemException {

    public KeyGenerationException(String msg) {
        super(msg);
    }
    
    public KeyGenerationException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
}
