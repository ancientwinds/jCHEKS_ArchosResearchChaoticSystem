package com.archosResearch.jCHEKS.chaoticSystem;

//<editor-fold defaultstate="collapsed" desc="Imports">
import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import java.util.ArrayList;
import java.util.Arrays;
//</editor-fold>

/**
 *
 * @author jean-francois
 */
public class ChaoticSystemMock extends AbstractChaoticSystem {
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private int keyPointer;
    private ArrayList<byte[]> keyList;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public ChaoticSystemMock() {
        super(128);
        
        this.keyPointer = 0;
        
        this.InitializeMockKeys();
        this.generateSystem(128);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Abstract methods implementation">
    @Override
    public void evolveSystem(int factor) {
        this.keyPointer++;
        
        if (this.keyPointer >= this.keyList.size()) {
            this.keyPointer = 0;
        }
        
        this.lastGeneratedKey = this.keyList.get(this.keyPointer);
    }
    
    @Override
    public byte[] getKey(int requiredLength) {
        byte[] fullKey = new byte[0];
        
        ChaoticSystemMock clone = this.cloneSystem();
        
        do {
            byte[] keyPart = clone.getKey();
            
            fullKey = Arrays.copyOf(fullKey, fullKey.length + keyPart.length);
            System.arraycopy(keyPart, 0, fullKey, fullKey.length-keyPart.length, keyPart.length);
            
            clone.evolveSystem();
        } while (fullKey.length < requiredLength);
        
        return fullKey;
    }
    
    @Override
    public void resetSystem() {
        // TODO : Demander à François ce qu'il voyait là-dedans!
    }
    
    @Override
    public ChaoticSystemMock cloneSystem() {
        return new ChaoticSystemMock();
    } 
    
    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.systemId);
        sb.append("!");
        sb.append(String.valueOf(this.keyLength));
        sb.append("!");
        sb.append(Utils.ByteArrayToString(this.lastGeneratedKey));

        return sb.toString();
    }
    
    @Override
    public void Deserialize(String serialization) {
        String[] values = serialization.split("!");
        
        this.systemId = values[0];
        this.keyLength = Integer.parseInt(values[1]);
        this.lastGeneratedKey = Utils.StringToByteArray(values[2]);
    }
    
    @Override
    protected void generateSystem(int keyLength) {
       this.keyLength = 128;
       this.lastGeneratedKey = this.keyList.get(keyPointer);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    private void InitializeMockKeys() {
        this.keyList = new ArrayList<>();
        this.keyList.add(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        this.keyList.add(new byte[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 63, 104, 15, 16});
        this.keyList.add(new byte[]{11, 21, 31, 41, 51, 61, 71, 81, 91, 101, 111, 121, 113, 114, 15, 16});
        this.keyList.add(new byte[]{1, 22, 3, 24, 5, 26, 7, 28, 9, 20, 11, 22, 13, 24, 15, 26});
        this.keyList.add(new byte[]{5,7, 8, 9, 10, 11, 12, 28, 19, 110, 44, 111, 67, 28, 75, 99});
    }
    //</editor-fold>
}
