package msci.mg.factories;

import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

/**
 * The AgentFactory interface defines the method create() which should
 * build and return an object implementing the {@code Agent} interface.
 *
 * @author Toby Clemson
 */
public abstract class AgentFactory implements Factory<Agent>{
    public abstract Agent create();
}
