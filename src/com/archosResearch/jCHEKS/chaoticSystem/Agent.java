package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.*;
import java.util.Map.Entry;
import org.w3c.dom.*;

/**
 *
 * @author jean-francois
 */
public class Agent implements Cloneable {

    private int agentId;
    private int keyPart;
    
    private Range keyPartRange;
    
    private HashMap<Integer, Integer> pendingImpacts = new HashMap<>();
    private HashMap<Integer, RuleSet> ruleSets = new HashMap<>();
    
    public static final String XML_AGENT_NAME = "a";
    private static final String XML_AGENTID_NAME = "ai";
    private static final String XML_KEYPART_NAME = "kp";
    private static final String XML_KEYPART_RANGE_NAME = "kpr";
    private static final String XML_KEYPART_MIN_NAME = "min";
    private static final String XML_KEYPART_MAX_NAME = "max";
    private static final String XML_RULESETS_NAME = "rss";
    private static final String XML_RULESET_NAME = "ruleset";
    private static final String XML_RULETOKEY_NAME = "rsk";
    private static final String XML_PENDINGIMPACTS_NAME = "pis";
    private static final String XML_PENDINGIMPACT_NAME = "pi";
    private static final String XML_IMPACT_NAME = "im";
    private static final String XML_DELAY_NAME = "de";
      
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
   
    public Agent(int agentId, Range impactRange, Range keyPartRange, Range delayRange, int ruleCount, int agentCount, Random random) {
        
        this.keyPartRange = keyPartRange;
        
        this.agentId = agentId;
        this.keyPart = Utils.GetRandomInt(keyPart, random);

        for (int i = this.keyPartRange.getMin(); i < (this.keyPartRange.getMax() + 1); i++) {
            this.ruleSets.put(i, new RuleSet(i, impactRange, delayRange, ruleCount, agentCount, random));
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
    
    public Agent(Element element) {
        this.agentId = Integer.parseInt(element.getElementsByTagName(XML_AGENTID_NAME).item(0).getTextContent());
        this.keyPart = Integer.parseInt(element.getElementsByTagName(XML_KEYPART_NAME).item(0).getTextContent());
    
        int min = Integer.parseInt(element.getElementsByTagName(XML_KEYPART_MIN_NAME).item(0).getTextContent());
        int max = Integer.parseInt(element.getElementsByTagName(XML_KEYPART_MAX_NAME).item(0).getTextContent());
        this.keyPartRange = new Range(min, max);
        
        NodeList ruleSetList = element.getElementsByTagName(XML_RULESET_NAME);
        this.ruleSets = new HashMap();

        for(int i = 0; i < ruleSetList.getLength(); i++) {
            Element node = (Element) ruleSetList.item(i);
            
            int ruleToKey = Integer.parseInt(node.getElementsByTagName(XML_RULETOKEY_NAME).item(0).getTextContent());
            RuleSet ruleSet = new RuleSet(node);
            this.ruleSets.put(ruleToKey, ruleSet);
        }
        
        NodeList pendingImpactsList = element.getElementsByTagName(XML_PENDINGIMPACT_NAME);
        this.pendingImpacts = new HashMap();

        for(int i = 0; i < pendingImpactsList.getLength(); i++) {
            Element node = (Element) pendingImpactsList.item(i);
            
            int impact = Integer.parseInt(node.getElementsByTagName(XML_IMPACT_NAME).item(0).getTextContent());
            int delay = Integer.parseInt(node.getElementsByTagName(XML_DELAY_NAME).item(0).getTextContent());

            this.pendingImpacts.put(delay, impact);
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
        return this.ruleSets.get(this.keyPart);
    }

    private int adjustKeyPart(int keyPart) {
        if (keyPart > this.keyPartRange.getMax()) {
            keyPart = this.keyPartRange.getMin() + ((keyPart) % 127);
        }
        if (keyPart < this.keyPartRange.getMin()) {
            keyPart = this.keyPartRange.getMax() - ((keyPart * -1) % 128);
        }
        return keyPart;
    }

    private void registerImpact(int impact, int delay) {
        if (!this.pendingImpacts.containsKey(delay)) {
            this.pendingImpacts.put(delay, 0);
        }

        this.pendingImpacts.put(delay, this.pendingImpacts.get(delay) + impact);
    }

    public void sendImpacts(ChaoticSystem system) {
        this.getCurrentRuleSet().getRules().stream().forEach((r) -> {
                system.getAgents().get(r.getDestination()).registerImpact(r.getImpact(), r.getDelay());
        });

        this.keyPart += this.getCurrentRuleSet().getSelfImpact();
    }

    public void evolve(int factor, int maxImpact) {
        if (this.pendingImpacts.containsKey(0)) {
            this.keyPart += this.pendingImpacts.get(0);
            this.pendingImpacts.remove(0);
        }
        if (factor != 0) {
            this.keyPart += Math.floor(maxImpact * Math.sin(this.keyPart * factor));
        }
        HashMap<Integer, Integer> tempImpacts = new HashMap<>();

        this.pendingImpacts.entrySet().stream().forEach((i) -> {
            tempImpacts.put(i.getKey() - 1, i.getValue());
        });

        this.pendingImpacts = tempImpacts;
        this.keyPart = this.adjustKeyPart(this.keyPart);
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.valueOf(this.agentId));
        sb.append("@");
        sb.append(String.valueOf(this.keyPart));

        sb.append("@");
        this.ruleSets.entrySet().stream().forEach((rs) -> {
            sb.append("S");
            sb.append(rs.getValue().serialize());
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
    
    public Element serializeXml(Element root) {
  
        Document doc = root.getOwnerDocument();
        Element rootElement = doc.createElement(XML_AGENT_NAME);

        Element systemIdElement = doc.createElement(XML_AGENTID_NAME);
        systemIdElement.appendChild(doc.createTextNode(Integer.toString(this.agentId)));
        rootElement.appendChild(systemIdElement);

        Element keyLengthElement = doc.createElement(XML_KEYPART_NAME);
        keyLengthElement.appendChild(doc.createTextNode(Integer.toString(this.keyPart)));
        rootElement.appendChild(keyLengthElement);

        Element keyPartRangeElement = doc.createElement(XML_KEYPART_RANGE_NAME);
        Element min = doc.createElement(XML_KEYPART_MIN_NAME);
        Element max = doc.createElement(XML_KEYPART_MAX_NAME);
        min.appendChild(doc.createTextNode(Integer.toString(this.keyPartRange.getMin())));
        max.appendChild(doc.createTextNode(Integer.toString(this.keyPartRange.getMax())));
        keyPartRangeElement.appendChild(min);
        keyPartRangeElement.appendChild(max);        
        rootElement.appendChild(keyPartRangeElement);

        Element ruleSetElement = doc.createElement(XML_RULESETS_NAME);
        Iterator it = this.ruleSets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, RuleSet> pair = (Map.Entry)it.next();
            Element ruleSet = doc.createElement(XML_RULESET_NAME);
            Element ruleSetKeyPart = doc.createElement(XML_RULETOKEY_NAME);            
            ruleSetKeyPart.appendChild(doc.createTextNode(Integer.toString(pair.getKey())));
            ruleSet.appendChild(pair.getValue().serializeXml(ruleSetElement));
            ruleSet.appendChild(ruleSetKeyPart);
            ruleSetElement.appendChild(ruleSet);
        }
        rootElement.appendChild(ruleSetElement);
        
        Element pendingImpactsElement = doc.createElement(XML_PENDINGIMPACTS_NAME);
        Iterator pending = this.pendingImpacts.entrySet().iterator();
        while (pending.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry)pending.next();
            Element pendingImpact = doc.createElement(XML_PENDINGIMPACT_NAME);
            Element impact = doc.createElement(XML_IMPACT_NAME);
            Element delay = doc.createElement(XML_DELAY_NAME);
            impact.appendChild(doc.createTextNode(Integer.toString(pair.getValue())));
            delay.appendChild(doc.createTextNode(Integer.toString(pair.getKey())));
            pendingImpact.appendChild(impact);
            pendingImpact.appendChild(delay);
            pendingImpactsElement.appendChild(pendingImpact);
        } 
        rootElement.appendChild(pendingImpactsElement);

        return rootElement;
    }
}
