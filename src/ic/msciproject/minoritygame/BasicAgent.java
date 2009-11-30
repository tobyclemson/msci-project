package ic.msciproject.minoritygame;

/**
 * The BasicAgent class represents the simplest type of agent to be used in the
 * minority game simulation. It cannot evolve its strategies and chooses a
 * strategy to use at each turn using only the global choice history with the
 * chosen strategy being that with the highest score over all of the preceding
 * time steps.
 * @author tobyclemson
 */
public class BasicAgent extends AbstractAgent {

    /**
     * Constructs an instance of BasicAgent setting the strategies attribute
     * to the supplied StrategyCollection instance.
     * @param strategyCollection A StrategyCollection instance representing the
     * agent's strategies.
     */
    public BasicAgent(StrategyCollection strategyCollection) {
        super(strategyCollection);
    }

}
