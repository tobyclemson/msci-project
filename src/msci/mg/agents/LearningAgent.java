package msci.mg.agents;

import msci.mg.ChoiceMemory;
import msci.mg.StrategyManager;

/**
 * The {@code LearningAgent} class represents an agent that can evolve its
 * strategies by removing the worst performing strategy and replacing it with
 * another at random and resetting its score to zero.
 *
 * @author Toby Clemson
 */
public class LearningAgent extends AbstractIntelligentAgent {
    /**
     * Constructs a {@code LearningAgent} setting the strategy manager and
     * memory attributes to the supplied {@code StrategyManager} and
     * {@code ChoiceMemory} instances.
     *
     * @param strategyManager A {@code StrategyManager} instance representing
     * this agent's strategies.
     * @param choiceMemory A {@code ChoiceMemory} instance representing the
     * agent's memory of past minority choices.
     */
    public LearningAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    public void choose() {
        
    }
}