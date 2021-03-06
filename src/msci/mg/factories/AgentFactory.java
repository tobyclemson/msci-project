package msci.mg.factories;

import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

public abstract class AgentFactory implements Factory<Agent> {
    public abstract Agent create();
}
