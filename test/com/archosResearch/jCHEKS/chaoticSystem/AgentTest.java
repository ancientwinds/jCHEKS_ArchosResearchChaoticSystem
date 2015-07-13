package com.archosResearch.jCHEKS.chaoticSystem;

import com.archosResearch.jCHEKS.chaoticSystem.exception.XMLSerializationException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class AgentTest {
    
    @Test
    public void agent_keyPart_should_not_change_if_there_is_no_pending_impacts() {
        Range impactRange = new Range(0, 32);
        Range keyPartRange = new Range(-128, 127);
        Range delayRange = new Range(0 ,4);

        Agent agent = new Agent(1, impactRange, keyPartRange, delayRange, 0, 0, new Random("test".hashCode()));
        int keyBeforeEvolve = agent.getKeyPart();
        agent.evolve(0, 0);
        assertEquals(keyBeforeEvolve, (int) agent.getKeyPart());
    }
    
}