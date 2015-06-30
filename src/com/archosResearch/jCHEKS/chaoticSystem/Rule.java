package com.archosResearch.jCHEKS.chaoticSystem;

/**
 *
 * @author jean-francois
 */
public class Rule {
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private int destination;
    private int impact;
    private int delay;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    public int getDestination() {
        return this.destination;
    }
    
    public int getImpact() {
        return this.impact;
    }
    
    public int getDelay() {
        return this.delay;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public Rule(int destination, int maxImpact)
    {
            this.destination = destination;
            
            
            this.impact = Utils.GetRandomInt(maxImpact);
            if (this.impact == 0) this.impact++;
            if (Utils.QuarterShot()) {
                    this.impact *= -1;
            }
            this.delay = Utils.QuarterShot() ? 0 : Utils.GetRandomInt(3) + 1;
    }

    /// <summary>
    /// <para>Constructor building a relation fromh the specified xml node.</para>
    /// </summary>
    /// <param name="relationNode">Node containing the relation definition.</param>
    public Rule(String serialization)
    {
        String[] values = serialization.split("%");
        
        this.destination = Integer.parseInt(values[0]);
        this.impact = Integer.parseInt(values[1]);
        this.delay = Integer.parseInt(values[2]);
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.valueOf(this.destination));
        sb.append("%");
        sb.append(String.valueOf(this.impact));
        sb.append("%");
        sb.append(String.valueOf(this.delay));
        
        return sb.toString();
    }
    //</editor-fold>
}
