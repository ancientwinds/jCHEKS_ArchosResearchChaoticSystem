package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.chaoticSystem.exception.*;
import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;
import java.io.*;
import java.security.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.InputSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 *
 * @author jean-francois
 */
public class ChaoticSystem extends AbstractChaoticSystem implements Cloneable {

    private HashMap<Integer, Agent> agents = new HashMap();
    private int lastGeneratedKeyIndex;
    private AbstractChaoticSystem currentClone;
    private byte[] toGenerateKey;
    private int toGenerateKeyIndex;

    public HashMap<Integer, Agent> getAgents() {
        return this.agents;
    }

    protected ChaoticSystem() {}
    
    public ChaoticSystem(int keyLength) throws ChaoticSystemException {
        super(keyLength);
        try {
            this.generateSystem(this.keyLength, SecureRandom.getInstance("SHA1PRNG"));
        } catch (NoSuchAlgorithmException ex) {
            throw new ChaoticSystemException("Can't not use the secure random.", ex);
        }    }
    
    public ChaoticSystem(int keyLength, String systemId) throws ChaoticSystemException {
        super(keyLength, systemId);
        try {
            this.generateSystem(this.keyLength, SecureRandom.getInstance("SHA1PRNG"));
        } catch (NoSuchAlgorithmException ex) {
            throw new ChaoticSystemException("Can't not use the secure random.", ex);
        }
    }
    
    public ChaoticSystem(int keyLength, Random random) throws ChaoticSystemException {
        super(keyLength, UUID.nameUUIDFromBytes(Integer.toString(random.nextInt()).getBytes()).toString()); 
        this.generateSystem(this.keyLength, random);
    }

    @Override
    public void evolveSystem(int factor) {
        
        this.agents.entrySet().stream().forEach((a) -> {
            ((Agent) a.getValue()).sendImpacts(this);
        });

        this.agents.entrySet().stream().forEach((a) -> {
            ((Agent) a.getValue()).evolve(factor, this.maxImpact);
        });
        
        this.buildKey();
        this.currentClone = null;
        this.lastGeneratedKeyIndex = 0;

    }

    @Override
    public byte[] getKey(int requiredBitLength) throws KeyLenghtException, KeyGenerationException{
        if (requiredBitLength % Byte.SIZE == 0) {
            return generateByteKey(requiredBitLength / Byte.SIZE);
        }
        throw new KeyLenghtException("Invalid key length. Must be a multiple of 8.");
    }
    
    @Override
    public void resetSystem() {
        // TODO : Demander à François ce qu'il voyait là-dedans!
        //TODO FG: I think the idea is to revert to the system before cloning...
    }

    @Override
    public ChaoticSystem clone() throws CloneNotSupportedException {
        ChaoticSystem chaoticSystemClone = (ChaoticSystem) super.clone();
        
        chaoticSystemClone.agents = new HashMap();
        for (Map.Entry<Integer, Agent> entrySet : this.agents.entrySet()) {
            Integer key = entrySet.getKey();
            Agent value = entrySet.getValue();
            chaoticSystemClone.agents.put(key, (Agent) value.clone());
        }
        return chaoticSystemClone;
    }

    @Override
    public ChaoticSystem cloneSystem() throws CloningException {
        try {
            return this.clone();
            
            /* Other way to do it.
            ChaoticSystem system = new ChaoticSystem(this.keyLength);
            system.deserialize(this.serialize());
            return system;
            */
        } catch (CloneNotSupportedException ex) {
            throw new CloningException("Unable to clone system.", ex);
        }

    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.systemId);
        sb.append("!");
        sb.append(String.valueOf(this.keyLength));
        sb.append("!");
        sb.append(Utils.ByteArrayToString(this.lastGeneratedKey));
        sb.append("!");
        this.agents.entrySet().forEach((a) -> {
            sb.append("A");
            sb.append(((Agent) a.getValue()).serialize());
        });

        return sb.toString();
    }

    @Override
    public void deserialize(String serialization) {
        String[] values = serialization.split("!");

        this.systemId = values[0];
        this.keyLength = Integer.parseInt(values[1]);
        this.lastGeneratedKey = Utils.StringToByteArray(values[2]);

        this.agents = new HashMap();
        String[] agentValues = values[3].substring(1).split("A");
        for (String agentString : agentValues) {
            Agent tempAgent = new Agent(agentString);
            this.agents.put(tempAgent.getAgentId(), tempAgent);
        }
    }

    public static ChaoticSystem deserializeXML(String xml) throws Exception {

        ChaoticSystem system = new ChaoticSystem();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = dBuilder.parse(is);
        
        doc.getDocumentElement().normalize();
        system.systemId = doc.getElementsByTagName("systemId").item(0).getTextContent();
        system.keyLength = Integer.parseInt(doc.getElementsByTagName("keyLength").item(0).getTextContent());

        system.lastGeneratedKey = Utils.StringToByteArray(doc.getElementsByTagName("lastKey").item(0).getTextContent());

        NodeList nList = doc.getElementsByTagName("agent");
        system.agents = new HashMap();

        for(int i = 0; i < nList.getLength(); i++) {
            Node element = nList.item(i);
            Agent tempAgent = new Agent(element.getTextContent());
            system.agents.put(tempAgent.getAgentId(), tempAgent);
        }
        
        return system;
    }

    public String serializeXML() throws TransformerConfigurationException, TransformerException, Exception {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("chaoticSystem");
            doc.appendChild(rootElement);

            Element systemIdElement = doc.createElement("systemId");
            systemIdElement.appendChild(doc.createTextNode(this.systemId));
            rootElement.appendChild(systemIdElement);

            Element keyLengthElement = doc.createElement("keyLength");
            keyLengthElement.appendChild(doc.createTextNode(Integer.toString(this.keyLength)));
            rootElement.appendChild(keyLengthElement);

            Element lastKey = doc.createElement("lastKey");
            lastKey.appendChild(doc.createTextNode(Utils.ByteArrayToString(this.lastGeneratedKey)));
            rootElement.appendChild(lastKey);

            Element agentsElement = doc.createElement("agents");

            this.agents.entrySet().forEach((a) -> {
                Element agent = doc.createElement("agent");
                agent.appendChild(doc.createTextNode(((Agent) a.getValue()).serialize()));
                agentsElement.appendChild(agent);
            });

            rootElement.appendChild(agentsElement);
            
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            
            return writer.toString();
        } catch (ParserConfigurationException ex) {
            throw new XMLSerializationException("Error serializing XML", ex);
        }
    }

    @Override
    public final void generateSystem(int keyLength, Random random) throws KeyLenghtException{
        this.keyLength = keyLength;

        if ((this.keyLength % 128) != 0) {
            throw new KeyLenghtException("Invalid key length. Must be a multiple of 128.");
        }

        //TODO We might want another extra for the cipherCheck
        int numberOfAgents = this.keyLength / Byte.SIZE;
        for (int i = 0; i < numberOfAgents; i++) {
            this.agents.put(i, new Agent(i, this.maxImpact, numberOfAgents, numberOfAgents - 1, random));
        }

        this.buildKey();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.agents);
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
        final ChaoticSystem other = (ChaoticSystem) obj;
        if (!Objects.equals(this.agents, other.agents)) {
            return false;
        }
        return true;
    }
    
    private byte[] generateByteKey(int requiredByteLength) throws KeyGenerationException {
        try {
            this.toGenerateKey = new byte[requiredByteLength];
            if (requiredByteLength >= this.lastGeneratedKey.length - this.lastGeneratedKeyIndex) {

                this.toGenerateKeyIndex = 0;
                setClone();
                pickCloneKey();
                while (this.toGenerateKeyIndex < requiredByteLength) {
                    fillKey();
                    if (this.toGenerateKeyIndex < requiredByteLength - 1) {
                        evolveClone();
                    }
                }

            } else {
                copyBytesFromLastToNextKey(requiredByteLength);
                this.lastGeneratedKeyIndex += requiredByteLength;
            }
            return this.toGenerateKey;
        } catch (CloningException ex) {
            throw new KeyGenerationException("Error in key generation process.", ex);
        }
    }

    private void copyBytesFromLastToNextKey(int numberOfBytesToCopy) {
        System.arraycopy(this.lastGeneratedKey, lastGeneratedKeyIndex, this.toGenerateKey, this.toGenerateKeyIndex, numberOfBytesToCopy);
    }

    private void fillKey() {
        int numberOfByteToCopy = getNumberOfByteToCopy(this.toGenerateKey.length, this.toGenerateKeyIndex);
        copyBytesFromLastToNextKey(numberOfByteToCopy);
        this.toGenerateKeyIndex += numberOfByteToCopy;
        this.lastGeneratedKeyIndex += numberOfByteToCopy;
    }

    private int getNumberOfByteToCopy(int requiredLength, int fullKeyIndex) {
        int numberOfMissingBytes = requiredLength - fullKeyIndex;
        return (this.lastGeneratedKey.length - this.lastGeneratedKeyIndex > numberOfMissingBytes) ? numberOfMissingBytes : this.lastGeneratedKey.length - lastGeneratedKeyIndex;
    }

    private void evolveClone() {
        this.currentClone.evolveSystem();
        this.lastGeneratedKey = this.currentClone.getKey();
        this.lastGeneratedKeyIndex = 0;
    }

    private void pickCloneKey() {
        this.lastGeneratedKey = this.currentClone.getKey();
    }

    private void setClone() throws CloningException {
        this.currentClone = (this.currentClone == null) ? cloneSystem() : this.currentClone;
    }
    
    private void buildKey() {
        this.lastGeneratedKey = new byte[(this.keyLength / Byte.SIZE)];

        for (int i = 0; i < (this.keyLength / Byte.SIZE); i++) {
            this.lastGeneratedKey[i] = ((Agent) this.agents.get(i)).getKeyPart();
        }
    }
}
