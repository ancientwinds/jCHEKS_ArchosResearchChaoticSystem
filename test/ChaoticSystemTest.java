import org.junit.Test;
import static org.junit.Assert.*;
import com.archosResearch.jCHEKS.chaoticSystem.ChaoticSystem;
import com.archosResearch.jCHEKS.chaoticSystem.ChaoticSystemMock;

/**
 *
 * @author jean-francois
 */
public class ChaoticSystemTest {
    @Test
    public void ChaoticSystemMockTest() throws Exception{
        ChaoticSystemMock systemMock1 = new ChaoticSystemMock();
        ChaoticSystemMock systemMock2 = new ChaoticSystemMock();
        
        systemMock2.Deserialize(systemMock1.Serialize());
        
        assertTrue("Serialization and Deserialization of mock system", systemMock1.Serialize().equals(systemMock2.Serialize()));
        }
    
    @Test
    public void ChaoticSystemTest() throws Exception {
        ChaoticSystem system1 = new ChaoticSystem(128);
        ChaoticSystem system2 = new ChaoticSystem(128);
        
        system2.Deserialize(system1.Serialize());
        
        assertTrue("Serialization and Deserialization of system", system1.Serialize().equals(system2.Serialize()));
    }
}