package com.archosResearch.jCHEKS.chaoticSystem.exception;

import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class CloningException extends ChaoticSystemException {

    public CloningException(String msg) {
        super(msg);
    }
    
    public CloningException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
}
