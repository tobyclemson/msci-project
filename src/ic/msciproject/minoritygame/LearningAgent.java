package ic.msciproject.minoritygame;

/**
 * The LearningAgent class represents the simplest type of agent to be used in
 * the minority game simulation. It can evolve its strategies by removing the
 * worst performing strategy and replacing it with another at random and
 * resetting its score to zero. At each turn the agent chooses a strategy to use
 * at each turn using only the global choice history with the chosen strategy
 * being that with the highest score over all of the preceding time steps.
 * @author tobyclemson
 */
public class LearningAgent extends AbstractAgent {

    /**
     * Constructs an instance of LearningAgent setting the strategies attribute
     * to the supplied StrategyCollection instance.
     * @param strategyCollection A StrategyCollection instance representing this
     * agent's strategies.
     */
    public LearningAgent(StrategyCollection strategyCollection) {
        super(strategyCollection);
    }

}
