package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">
import java.util.HashMap;
import java.util.Map.Entry;
//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class Agent {
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private int agentId;
    private int keyPart;
    private HashMap<Integer, Integer> pendingImpacts = new HashMap<>();
    private HashMap<Integer, Object> ruleSets = new HashMap<>();
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    public int getAgentId() {
        return this.agentId;
    }
    
    public byte getKeyPart() {
        return (byte)this.keyPart;
    }
    
    public HashMap<Integer, Integer> getPendingImpacts() {
        return this.pendingImpacts;
    }
    
    public HashMap<Integer, Object> getRuleSets() {
        return this.ruleSets;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public Agent(int agentId, int maxImpact, int ruleCount, int agentCount) {
        this.agentId = agentId;
        this.keyPart = Utils.GetRandomInt(Byte.MAX_VALUE);
        if (Utils.QuarterShot()) this.keyPart *= -1;
        
        for (int i = Byte.MIN_VALUE; i < (Byte.MAX_VALUE + 1); i++) {
            this.ruleSets.put(i, new RuleSet(i, maxImpact, ruleCount, agentCount));
        }
    }
    
    public Agent(String serialization) {
        String[] values = serialization.split("@");
        
        this.agentId = Integer.parseInt(values[0]);
        this.keyPart = Integer.parseInt(values[1]);
        
        String[] ruleSetValues = values[2].substring(1).split("S");
        for (String ruleSetString : ruleSetValues) {
            RuleSet rs = new RuleSet(ruleSetString);
            this.ruleSets.put(rs.getLevel(), rs);
        }
        
        if (values.length == 4) {
            String[] pendingImpactValues = values[3].substring(1).split("P");
            for (String pendingImpactString : pendingImpactValues) {
                String[] pi = pendingImpactString.split(",");
                this.pendingImpacts.put(Integer.parseInt(pi[0]), Integer.parseInt(pi[1]));
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    public RuleSet getCurrentRuleSet() {
        return (RuleSet)this.ruleSets.get(this.keyPart);
    }
    
    public void RegisterImpact(int impact, int delay){
        if (!this.pendingImpacts.containsKey(delay)) {
            this.pendingImpacts.put(delay, 0);
        }
        
        this.pendingImpacts.put(delay, (int)this.pendingImpacts.get(delay) + impact);
    }
    
    public void SendImpacts(ChaoticSystem system) {
        this.getCurrentRuleSet().getRules().stream().forEach((r) -> {
            try {
                ((Agent)system.getAgents().get(r.getDestination())).RegisterImpact(r.getImpact(), r.getDelay());
            } catch (Exception e) {
                //
            }
        });
        
        this.keyPart += this.getCurrentRuleSet().getSelfImpact();
    }
    
    public void Evolve(int factor, int maxImpact) {
        this.keyPart += (int)this.pendingImpacts.get(0);
        
        if (factor != 0) {
            this.keyPart += Math.floor(maxImpact * Math.sin(this.keyPart * factor));
        }
        
        this.pendingImpacts.remove(0);
        HashMap<Integer, Integer> tempImpacts = new HashMap<>();
        
        this.pendingImpacts.entrySet().stream().forEach((i) -> {
            tempImpacts.put(i.getKey() - 1, i.getValue());
        });
        
        this.pendingImpacts = tempImpacts;
        
        if (this.keyPart > Byte.MAX_VALUE) {
            this.keyPart = Byte.MIN_VALUE + ((this.keyPart) % 255);
        }
        if (this.keyPart < Byte.MIN_VALUE) {
            this.keyPart = Byte.MAX_VALUE - ((this.keyPart * -1) % 255);
        }
    }
    
    public String Serialize() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.valueOf(this.agentId));
        sb.append("@");
        sb.append(String.valueOf(this.keyPart));
        
        sb.append("@");       
        this.ruleSets.entrySet().stream().forEach((rs) -> {
            sb.append("S");
            sb.append(((RuleSet)rs.getValue()).Serialize());
        });
        
        sb.append("@");
        this.pendingImpacts.entrySet().stream().forEach((pi) -> {
            sb.append("P");
            sb.append(pi.getKey().toString());
            sb.append(",");
            sb.append(pi.getValue().toString());
        });
        
        return sb.toString();
    }
    //</editor-fold>
}
