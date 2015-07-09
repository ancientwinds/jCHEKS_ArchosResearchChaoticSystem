package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.*;

/**
 *
 * @author jean-francois
 */
public class RuleSet implements Cloneable {

    private int level;
    private int selfImpact;
    private ArrayList<Rule> rules;

    public int getLevel() {
        return this.level;
    }

    public int getSelfImpact() {
        return this.selfImpact;
    }

    public ArrayList<Rule> getRules() {
        return this.rules;
    }

    /*public RuleSet(int level, int maxImpact, int ruleCount, int agentCount, Random random) {
        this.initializeLists();

        this.level = level;
        this.selfImpact = Utils.GetRandomInt(maxImpact, random);
        //TODO Avoid 0 in random generation.
        if (this.selfImpact == 0) {
            this.selfImpact++;
        }
        if (Utils.QuarterShot(random)) {
            this.selfImpact *= -1;
        }

        // Ajouter les relations
        for (int x = 0; x < ruleCount; x++) {
            this.rules.add(new Rule(Utils.GetRandomInt(agentCount, random), maxImpact, random));
        }
    }*/
    
    public RuleSet(int level, Range impactRange, Range delayRange, int ruleCount, int agentCount, Random random) {
        this.initializeLists();

        this.level = level;
        this.selfImpact = Utils.GetRandomIntAvoidingZero(impactRange, random);
        
        for (int x = 0; x < ruleCount; x++) {
            this.rules.add(new Rule(Utils.GetRandomInt(agentCount, random), impactRange, delayRange, random));
        }
    }

    public RuleSet(String serialization) {
        this.initializeLists();

        String[] values = serialization.split("/");

        this.selfImpact = Integer.parseInt(values[0]);
        this.level = Integer.parseInt(values[1]);

        String[] ruleValues = values[2].substring(1).split("R");

        for (String s : ruleValues) {
            this.rules.add(new Rule(s));
        }
    }

    @Override
    public RuleSet clone() throws CloneNotSupportedException {
        RuleSet ruleSetClone = (RuleSet) super.clone();
        ruleSetClone.rules = new ArrayList();
        for (int i = 0; i < this.rules.size(); i++) {
            ruleSetClone.rules.add(this.rules.get(i).clone());
        }
        return ruleSetClone;
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.valueOf(this.selfImpact));
        sb.append("/");
        sb.append(String.valueOf(this.level));
        sb.append("/");

        this.rules.stream().forEach((r) -> {
            sb.append("R");
            sb.append(r.serialize());
        });

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.level;
        hash = 61 * hash + this.selfImpact;
        hash = 61 * hash + Objects.hashCode(this.rules);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RuleSet other = (RuleSet) obj;
        if (this.level != other.level) {
            return false;
        }
        if (this.selfImpact != other.selfImpact) {
            return false;
        }
        return Objects.equals(this.rules, other.rules);
    }

    private void initializeLists() {
        this.rules = new ArrayList<>();
    }
}
