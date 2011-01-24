package msci.mg.factories;

import msci.mg.agents.RandomAgent;

public class RandomAgentFactory extends AgentFactory {
    public RandomAgent create() {
        return new RandomAgent();
    }
}
