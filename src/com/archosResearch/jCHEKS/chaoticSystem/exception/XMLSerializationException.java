package com.archosResearch.jCHEKS.chaoticSystem.exception;

import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;


/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class XMLSerializationException extends ChaoticSystemException {

    public XMLSerializationException(String msg) {
        super(msg);
    }
    
    public XMLSerializationException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
}
