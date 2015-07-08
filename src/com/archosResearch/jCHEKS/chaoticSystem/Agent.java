package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author jean-francois
 */
public class Agent implements Cloneable {

    private int agentId;
    private int keyPart;
    private HashMap<Integer, Integer> pendingImpacts = new HashMap<>();
    private HashMap<Integer, RuleSet> ruleSets = new HashMap<>();

    public int getAgentId() {
        return this.agentId;
    }

    public byte getKeyPart() {
        return (byte) this.keyPart;
    }

    public HashMap<Integer, Integer> getPendingImpacts() {
        return this.pendingImpacts;
    }

    public HashMap<Integer, RuleSet> getRuleSets() {
        return this.ruleSets;
    }

    public Agent(int agentId, int maxImpact, int ruleCount, int agentCount, Random random) {
        this.agentId = agentId;
        this.keyPart = Utils.GetRandomInt(Byte.MAX_VALUE, random);
        if (Utils.QuarterShot(random)) {
            this.keyPart *= -1;
        }

        for (int i = Byte.MIN_VALUE; i < (Byte.MAX_VALUE + 1); i++) {
            this.ruleSets.put(i, new RuleSet(i, maxImpact, ruleCount, agentCount, random));
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

    @Override
    public Agent clone() throws CloneNotSupportedException {
        Agent agentClone = (Agent) super.clone();
        
        agentClone.pendingImpacts = new HashMap();
        this.pendingImpacts.entrySet().stream().forEach((entrySet) -> {
            Integer key = entrySet.getKey();
            Integer value = entrySet.getValue();
            agentClone.pendingImpacts.put(key, value);
        });
        
        agentClone.ruleSets = new HashMap();
        for (Entry<Integer, RuleSet> entrySet : ruleSets.entrySet()) {
            Integer key = entrySet.getKey();
            RuleSet value = entrySet.getValue();
            agentClone.ruleSets.put(key, value.clone());
        }

        return agentClone;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agent other = (Agent) obj;
        if (this.agentId != other.agentId) {
            return false;
        }
        if (this.keyPart != other.keyPart) {
            return false;
        }
        if (!Objects.equals(this.pendingImpacts, other.pendingImpacts)) {
            return false;
        }
        if (!Objects.equals(this.ruleSets, other.ruleSets)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.agentId;
        hash = 37 * hash + this.keyPart;
        hash = 37 * hash + Objects.hashCode(this.pendingImpacts);
        hash = 37 * hash + Objects.hashCode(this.ruleSets);
        return hash;
    }

    public RuleSet getCurrentRuleSet() {
        return (RuleSet) this.ruleSets.get(this.keyPart);
    }

    public void registerImpact(int impact, int delay) {
        if (!this.pendingImpacts.containsKey(delay)) {
            this.pendingImpacts.put(delay, 0);
        }

        this.pendingImpacts.put(delay, (int) this.pendingImpacts.get(delay) + impact);
    }

    public void sendImpacts(ChaoticSystem system) {
        this.getCurrentRuleSet().getRules().stream().forEach((r) -> {
            try {
                ((Agent) system.getAgents().get(r.getDestination())).registerImpact(r.getImpact(), r.getDelay());
            } catch (Exception e) {
                //TODO Create new Exception
            }
        });

        this.keyPart += this.getCurrentRuleSet().getSelfImpact();
    }

    public void evolve(int factor, int maxImpact) {
        if(!this.pendingImpacts.isEmpty()){
            this.keyPart += (int)this.pendingImpacts.get(0);
        }
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

    public String serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.valueOf(this.agentId));
        sb.append("@");
        sb.append(String.valueOf(this.keyPart));

        sb.append("@");
        this.ruleSets.entrySet().stream().forEach((rs) -> {
            sb.append("S");
            sb.append(((RuleSet) rs.getValue()).serialize());
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
}
