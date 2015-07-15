package com.archosResearch.jCHEKS.chaoticSystem;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class Range {
    private int min = 0;
    private int max = 0;
    
    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getMax() {
        return this.max;
    }
}
