package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">

import java.util.Arrays;
import java.util.HashMap;


//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class ChaoticSystem extends com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem {
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final HashMap<Integer, Agent> agents = new HashMap();
    //</editor-fold>

    public ChaoticSystem(String uniqueId, int keyLength) throws Exception {
        super(uniqueId, keyLength);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    public HashMap<Integer, Agent> getAgents() {
        return this.agents;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /*public ChaoticSystem(String uniqueId, int keyLength) throws Exception {
        super(uniqueId, keyLength);
        this.Generate(this.keyLength);
    }*/

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Abstract methods implementation">
    @Override
    public void evolveSystem(int factor) {
        this.agents.entrySet().stream().forEach((a) -> {
           ((Agent)a.getValue()).SendImpacts(this);
        });
        
        this.agents.entrySet().stream().forEach((a) -> {
           ((Agent)a.getValue()).Evolve(factor, this.maxImpact);
        });
        
        this.BuildKey();
    }

    @Override
    public byte[] getKey(int requiredLength) throws Exception{
        byte[] fullKey = new byte[0];
        
        try {
            ChaoticSystem clone = this.cloneSystem();
            do {
                byte[] keyPart = clone.getKey();

                fullKey = Arrays.copyOf(fullKey, fullKey.length + keyPart.length);
                System.arraycopy(keyPart, 0, fullKey, fullKey.length-keyPart.length, keyPart.length);

                clone.evolveSystem();
            } while (fullKey.length < requiredLength);
        
            return fullKey;
        } catch (Exception ex) {
            throw new Exception("Error while getting a key", ex);
        }
        

    }

    @Override
    public void resetSystem() {
        // TODO : Demander à François ce qu'il voyait là-dedans!
    }

    @Override
    public ChaoticSystem cloneSystem() throws Exception {
        try {
            ChaoticSystem system = new ChaoticSystem(this.systemId, this.keyLength);
            system.Deserialize(this.Serialize());
            return system;
        } catch (Exception ex) {
            throw new Exception("Error during the cloning process", ex);
        }
        
    }

    @Override
    public String Serialize() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.systemId);
        sb.append("!");
        sb.append(String.valueOf(this.keyLength));
        sb.append("!");
        sb.append(Utils.ByteArrayToString(this.lastGeneratedKey));
        sb.append("!");
        sb.append(Utils.ByteArrayToString(this.lastGeneratedIV));
        sb.append("!");
        
        this.agents.entrySet().forEach((a) -> {
            sb.append("A");
            sb.append(((Agent)a.getValue()).Serialize());
        });

        return sb.toString();
    }

    @Override
    public void Deserialize(String serialization) {
        String[] values = serialization.split("!");
        
        this.systemId = values[0];
        this.keyLength = Integer.parseInt(values[1]);
        this.lastGeneratedKey = Utils.StringToByteArray(values[2]);
        this.lastGeneratedIV = Utils.StringToByteArray(values[3]);
        
        String[] agentValues = values[4].substring(1).split("A");
        for (String agentString : agentValues) {
            Agent tempAgent = new Agent(agentString);
            this.agents.put(tempAgent.getAgentId(), tempAgent);
        }
    }

    @Override
    protected void generateSystem(int keyLength) throws Exception {
        this.keyLength = keyLength;
        
        if ((this.keyLength % 128) != 0) {
            throw new Exception("Invalid key length. Must be a multiple of 128.");
        }
        
        int numberOfAgents = (this.keyLength / 8) + 16;
        for (int i = 0; i < numberOfAgents; i++) {
            this.agents.put(i, new Agent(i, this.maxImpact, numberOfAgents, numberOfAgents-1));
        }
        
        this.BuildKey();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    private void BuildKey() {
        this.lastGeneratedKey = new byte[(this.keyLength / 8)];
        this.lastGeneratedIV= new byte[16];
        
        for (int i = 0; i < (this.keyLength / 8); i++) {
            this.lastGeneratedKey[i] = ((Agent)this.agents.get(i)).getKeyPart();
        }
        
        for (int i = 0; i < 16; i++) {
            this.lastGeneratedIV[i] = ((Agent)this.agents.get((this.agents.size() -1) - i)).getKeyPart();
        }
    }
    //</editor-fold>
}
