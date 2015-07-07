import org.junit.Test;
import static org.junit.Assert.*;
import com.archosResearch.jCHEKS.chaoticSystem.*;

/**
 *
 * @author jean-francois
 */
public class ChaoticSystemTest {
    @Test
    public void ChaoticSystemMockTest() throws Exception{
        ChaoticSystemMock systemMock1 = new ChaoticSystemMock();
        ChaoticSystemMock systemMock2 = new ChaoticSystemMock();
        
        systemMock2.deserialize(systemMock1.serialize());
        
        assertTrue("Serialization and Deserialization of mock system", systemMock1.serialize().equals(systemMock2.serialize()));
        }
    
    @Test
    public void ChaoticSystemTest() throws Exception {
        ChaoticSystem system1 = new ChaoticSystem(128);
        ChaoticSystem system2 = new ChaoticSystem(128);
        
        system2.deserialize(system1.serialize());
        
        assertTrue("Serialization and Deserialization of system", system1.serialize().equals(system2.serialize()));
    }
    
    @Test
    public void get_a_small_key_test() throws Exception {
        ChaoticSystem system = new ChaoticSystem(128);
        assertEquals(system.getKey(80).length, 10);
    }
    
    @Test
    public void get_a_big_key_test() throws Exception {
        ChaoticSystem system = new ChaoticSystem(128);
        assertEquals(system.getKey(800).length, 100);
    }
    
    @Test
    public void get_two_keys_test() throws Exception {
        ChaoticSystem system = new ChaoticSystem(128);
        assertEquals(system.getKey(8).length, 1);
        assertEquals(system.getKey(80).length, 10);
        assertEquals(system.getKey(800).length, 100);
    }
}