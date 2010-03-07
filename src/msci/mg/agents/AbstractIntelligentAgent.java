/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package msci.mg.agents;

import java.util.List;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

/**
 *
 * @author Toby Clemson
 */
public abstract class AbstractIntelligentAgent
    extends AbstractAgent
    implements IntelligentAgent {

    protected StrategyManager strategyManager;
    protected ChoiceMemory memory;

    /**
     * Constructs an instance of {@code AbstractAgent} setting the strategy
     * manager and memory attributes to the supplied {@code StrategyManager} and
     * {@code ChoiceMemory} instances.
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
