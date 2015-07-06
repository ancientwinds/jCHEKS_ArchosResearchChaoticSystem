package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">
import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class ChaoticSystem extends AbstractChaoticSystem {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    private HashMap<Integer, Agent> agents = new HashMap();
    private int lastGeneratedKeyIndex;
    private AbstractChaoticSystem currentClone;
    private byte[] toGenerateKey;
    private int toGenerateKeyIndex;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Accessors">
    public HashMap<Integer, Agent> getAgents() {
        return this.agents;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public ChaoticSystem(int keyLength) throws Exception {
        super(keyLength);
        this.generateSystem(this.keyLength);
    }
    
    public ChaoticSystem(int keyLength, String systemId) throws Exception {
        super(keyLength, systemId);
        this.generateSystem(this.keyLength);
    }
    
    public ChaoticSystem(int keyLength, String systemId, String seed) throws Exception {
        super(keyLength, systemId);
        Utils.setSeed(seed);
        this.generateSystem(this.keyLength);
        Utils.resetSeed();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Abstract methods implementation">
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
    public byte[] getKey(int requiredBitLength) throws Exception {
        if (requiredBitLength % Byte.SIZE == 0) {
            return generateByteKey(requiredBitLength / Byte.SIZE);
        }
        throw new Exception();
    }

    private byte[] generateByteKey(int requiredByteLength) throws Exception {
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

    private void setClone() throws Exception {
        this.currentClone = (this.currentClone == null) ? cloneSystem() : this.currentClone;
    }

    @Override
    public void resetSystem() {
        // TODO : Demander à François ce qu'il voyait là-dedans!
        //TODO FG: I think the idea is to revert to the system before cloning...
    }

    @Override
    public ChaoticSystem cloneSystem() throws Exception {
        try {
            ChaoticSystem system = new ChaoticSystem(this.keyLength);
            system.Deserialize(this.serialize());
            return system;
        } catch (Exception ex) {
            throw new Exception("Error during the cloning process", ex);
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
    public void Deserialize(String serialization) {
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

    public void deserializeXML(File xmlFile) throws Exception {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        this.systemId = doc.getElementsByTagName("systemId").item(0).getTextContent();
        this.keyLength = Integer.parseInt(doc.getElementsByTagName("keyLength").item(0).getTextContent());

        this.lastGeneratedKey = Utils.StringToByteArray(doc.getElementsByTagName("lastKey").item(0).getTextContent());

        NodeList nList = doc.getElementsByTagName("agent");
        this.agents = new HashMap();

        for(int i = 0; i < nList.getLength(); i++) {
            Node element = nList.item(i);
            Agent tempAgent = new Agent(element.getTextContent());
            this.agents.put(tempAgent.getAgentId(), tempAgent);

        }
    }

    public Document serializeXML() throws TransformerConfigurationException, TransformerException, Exception {
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

            return doc;
        } catch (ParserConfigurationException ex) {
            throw new Exception("Error serializing XML", ex);
        }
    }

    @Override
    public final void generateSystem(int keyLength) throws Exception {
        this.keyLength = keyLength;

        if ((this.keyLength % 128) != 0) {
            throw new Exception("Invalid key length. Must be a multiple of 128.");
        }

        //TODO We might want another extra for the cipherCheck
        int numberOfAgents = this.keyLength / 8;
        for (int i = 0; i < numberOfAgents; i++) {
            this.agents.put(i, new Agent(i, this.maxImpact, numberOfAgents, numberOfAgents - 1));
        }

        this.buildKey();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Methods">
    private void buildKey() {
        this.lastGeneratedKey = new byte[(this.keyLength / 8)];

        for (int i = 0; i < (this.keyLength / 8); i++) {
            this.lastGeneratedKey[i] = ((Agent) this.agents.get(i)).getKeyPart();
        }
    }
    //</editor-fold>
}
