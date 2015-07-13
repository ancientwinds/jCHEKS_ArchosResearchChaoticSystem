package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author jean-francois
 */
public class RuleSet implements Cloneable {

    private int level;
    private int selfImpact;
    private ArrayList<Rule> rules;
    
    private static final String XML_RULESET_NAME = "rs";
    private static final String XML_SELFIMPACT_NAME = "si";
    private static final String XML_LEVEL_NAME = "l";
    private static final String XML_RULES_NAME = "rules";


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
    
    public RuleSet(Element element) {
        this.initializeLists();
        
        this.selfImpact = Integer.parseInt(element.getElementsByTagName(XML_SELFIMPACT_NAME).item(0).getTextContent());
        this.level = Integer.parseInt(element.getElementsByTagName(XML_LEVEL_NAME).item(0).getTextContent());
    
        NodeList rulesList = element.getElementsByTagName(Rule.XML_RULE_NAME);

        for(int i = 0; i < rulesList.getLength(); i++) {
            Element node = (Element) rulesList.item(i);
            this.rules.add(new Rule(node));
        }
    }
    
    public Element serializeXml(Element root) {
        Document doc = root.getOwnerDocument();
        
        Element ruleSetElement = doc.createElement(XML_RULESET_NAME);

        Element selfImpactElement = doc.createElement(XML_SELFIMPACT_NAME);
        selfImpactElement.appendChild(doc.createTextNode(Integer.toString(this.selfImpact)));
        ruleSetElement.appendChild(selfImpactElement);

        Element levelElement = doc.createElement(XML_LEVEL_NAME);
        levelElement.appendChild(doc.createTextNode(Integer.toString(this.level)));
        ruleSetElement.appendChild(levelElement);

        Element rulesElement = doc.createElement(XML_RULES_NAME);

        for (Rule rule : this.rules) {
            rulesElement.appendChild(rule.serializeXml(rulesElement));
        }

        ruleSetElement.appendChild(rulesElement);
        
        return ruleSetElement;
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
