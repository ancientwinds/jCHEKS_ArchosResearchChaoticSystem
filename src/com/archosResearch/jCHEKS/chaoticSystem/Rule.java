package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.chaoticSystem.exception.XMLSerializationException;
import java.io.IOException;
import java.io.*;
import java.util.Random;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 *
 * @author jean-francois
 */
public class Rule implements Cloneable{

    private int destination;
    private int impact;
    private int delay;
    
    private static final String XML_RULE_NAME = "r";
    private static final String XML_DESTINATION_NAME = "d";
    private static final String XML_DELAY_NAME = "dl";
    private static final String XML_IMPACT_NAME = "i";

    
    public int getDestination() {
        return this.destination;
    }
    
    public int getImpact() {
        return this.impact;
    }
    
    public int getDelay() {
        return this.delay;
    }
    
    /*public Rule(int destination, int maxImpact, Random random)
    {
            this.destination = destination;            
            
            this.impact = Utils.GetRandomInt(maxImpact, random);
            //TODO Avoid 0 in random.
            if (this.impact == 0) this.impact++;
            if (Utils.QuarterShot(random)) {
                    this.impact *= -1;
            }
            this.delay = Utils.QuarterShot(random) ? 0 : Utils.GetRandomInt(3, random) + 1;
    }*/
    
    public Rule(int destination, Range impactRange, Range delayRange, Random random) {
        this.destination = destination;
        this.impact = Utils.GetRandomIntAvoidingZero(impactRange, random);
        this.delay = Utils.GetRandomInt(delayRange, random);
    }
    
    private Rule() {}

    /// <summary>
    /// <para>Constructor building a relation from the specified xml node.</para>
    /// </summary>
    /// <param name="relationNode">Node containing the relation definition.</param>
    public Rule(String serialization)
    {
        String[] values = serialization.split("%");
        
        this.destination = Integer.parseInt(values[0]);
        this.impact = Integer.parseInt(values[1]);
        this.delay = Integer.parseInt(values[2]);
    }
    
    public static Rule desirialize(String xmlString) throws XMLSerializationException {
        try {
            Rule rule = new Rule();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            Document doc = dBuilder.parse(is);
            
            doc.getDocumentElement().normalize();
            rule.destination = Integer.parseInt(doc.getElementsByTagName("destination").item(0).getTextContent());
            rule.impact = Integer.parseInt(doc.getElementsByTagName("impact").item(0).getTextContent());
            rule.delay = Integer.parseInt(doc.getElementsByTagName("delay").item(0).getTextContent());
            
            return rule;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new XMLSerializationException("Error while desirealizing a rule", ex);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rule other = (Rule) obj;
        if (this.destination != other.destination) {
            return false;
        }
        if (this.impact != other.impact) {
            return false;
        }
        if (this.delay != other.delay) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.destination;
        hash = 47 * hash + this.impact;
        hash = 47 * hash + this.delay;
        return hash;
    }
    
    @Override
    public Rule clone() throws CloneNotSupportedException{
        return (Rule)super.clone();
    }
    
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.valueOf(this.destination));
        sb.append("%");
        sb.append(String.valueOf(this.impact));
        sb.append("%");
        sb.append(String.valueOf(this.delay));
        
        return sb.toString();
    }
    
    public Element serializeXml(Element root) {
            
        Document doc = root.getOwnerDocument();
        Element rootElement = doc.createElement(XML_RULE_NAME);

        Element systemIdElement = doc.createElement(XML_DESTINATION_NAME);
        systemIdElement.appendChild(doc.createTextNode(Integer.toString(this.destination)));
        rootElement.appendChild(systemIdElement);

        Element keyLengthElement = doc.createElement(XML_IMPACT_NAME);
        keyLengthElement.appendChild(doc.createTextNode(Integer.toString(this.impact)));
        rootElement.appendChild(keyLengthElement);

        Element lastKey = doc.createElement(XML_DELAY_NAME);
        lastKey.appendChild(doc.createTextNode(Integer.toString(this.delay)));
        rootElement.appendChild(lastKey);

        return rootElement;
    }
}
