package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">
import java.util.ArrayList;
//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class RuleSet {
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private int level;
    private int selfImpact;
    private ArrayList<Rule> rules;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    public int getLevel() {
        return this.level;
    }
    
    public int getSelfImpact() {
        return this.selfImpact;
    }
    
    public ArrayList<Rule> getRules() {
        return this.rules;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public RuleSet(int level, int maxImpact, int ruleCount, int agentCount) {
        this.InitializeLists();
        
        this.level = level;
        this.selfImpact = Utils.GetRandomInt(maxImpact);
        if (this.selfImpact == 0) this.selfImpact++;
        if (Utils.QuarterShot()) {
            this.selfImpact *= -1;
        }

        // Ajouter les relations
        for (int x = 0; x < ruleCount; x++) {
            this.rules.add(new Rule(Utils.GetRandomInt(agentCount), maxImpact));
        }
    }
    
    public RuleSet(String serialization) {
        this.InitializeLists();
        
        String[] values = serialization.split("/");
        
        this.selfImpact = Integer.parseInt(values[0]);
        this.level = Integer.parseInt(values[1]);
        
        String[] ruleValues = values[2].substring(1).split("R");
        
        for (String s : ruleValues) {
            this.rules.add(new Rule(s));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    public String Serialize() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.valueOf(this.selfImpact));
        sb.append("/");
        sb.append(String.valueOf(this.level));
        sb.append("/");
        
        this.rules.stream().forEach((r) -> {
            sb.append("R");
            sb.append(r.Serialize());
        });
        
        return sb.toString();
    }
    
    private void InitializeLists(){
        this.rules = new ArrayList<>();
    }
    //</editor-fold>
}
