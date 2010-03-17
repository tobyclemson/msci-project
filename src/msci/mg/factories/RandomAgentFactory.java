package msci.mg.factories;

import msci.mg.agents.RandomAgent;

/**
 * The {@code RandomAgentFactory} creates random choice agents with no memory or
 * strategies set.
 *
 * @author Toby Clemson
 */
public class RandomAgentFactory extends AgentFactory{
    /**
     * Constructs a RandomAgent which has no memory or strategies set since
     * it makes its choices unintelligently.
     * @return An instance of the RandomAgent class.
     */
    public RandomAgent create() {
        return new RandomAgent();
    }
}
