package com.archosResearch.jCHEKS.chaoticSystem;

import java.util.Random;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class AgentTest {
    
    @Test
    public void agent_keyPart_should_not_change_if_there_is_no_pending_impacts() {
        Agent agent = new Agent(1, 100, 0, 0, new Random("test".hashCode()));
        int keyBeforeEvolve = agent.getKeyPart();
        agent.evolve(0, 0);
        assertEquals(keyBeforeEvolve, (int) agent.getKeyPart());
        
    }
    
}