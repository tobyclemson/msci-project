package msci.mg.agents;

import java.util.List;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

/**
 * The {@code AbstractIntelligentAgent} class provides a partial implementation
 * of the {@code IntelligentAgent} interface easing the burden of implementing
 * an intelligent agent.
 *
 * @author Toby Clemson
 */
public abstract class AbstractIntelligentAgent
    extends AbstractAgent
    implements IntelligentAgent {

    protected StrategyManager strategyManager;
    protected ChoiceMemory memory;

    /**
     * Constructs an {@code AbstractNetworkedAgent} setting the strategy
     * manager and memory attributes to the supplied {@code StrategyManager}
     * and {@code ChoiceMemory} instances. If the supplied strategy manager
     * contains strategies with a key length not equal to the memory capacity,
     * an {@code IllegalArgumentException} is thrown.
     *
     * @param strategyManager A {@code StrategyManager} instance containing
     * this agent's strategies.
     * @param choiceMemory A {@code ChoiceMemory} instance representing this
     * agent's memory of past minority choices.
     */
    public AbstractIntelligentAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super();

        int strategyKeyLength = strategyManager.getStrategyKeyLength();
        int memoryCapacity = choiceMemory.getCapacity();

        if(strategyKeyLength != memoryCapacity) {
            throw new IllegalArgumentException(
                "The Strategy key length for the supplied StrategyManager " +
                "does not match the capacity of the supplied ChoiceMemory."
            );
        }

        this.strategyManager = strategyManager;
        this.memory = choiceMemory;
    }

    public List<Strategy> getStrategies() {
        return strategyManager.getStrategies();
    }

    public StrategyManager getStrategyManager() {
        return this.strategyManager;
    }

    public ChoiceMemory getMemory() {
        return memory;
    }

}
