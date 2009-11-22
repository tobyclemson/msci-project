package ic.msciproject.minoritygame;

/**
 * The AbstractAgent class implements the basic functionality of an agent in the
 * minority game and all specific agents should extend it.
 * @author tobyclemson
 */
public class AbstractAgent {
    /**
     * A StrategyCollection instance holding the agent's strategies. This is
     * returned by the {@link #getStrategies} method.
     */
    private StrategyCollection strategies;

    /**
     * Constructs an AbstractAgent instance setting the strategies attribute to
     * the supplied StrategyCollection instance.
     * @param strategyCollection A StrategyCollection instance representing
     * the agent's strategies.
     */
    public AbstractAgent(StrategyCollection strategyCollection){
        this.strategies = strategyCollection;
    }

    /**
     * Returns a StrategyCollection containing the strategies associated with
     * this agent.
     * @return A StrategyCollection containing the agent's strategies.
     */
    public StrategyCollection getStrategies() {
        return strategies;
    }
}
