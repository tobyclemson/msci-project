package msci.mg.agents;

import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

import java.util.List;

public abstract class AbstractIntelligentAgent extends AbstractAgent implements IntelligentAgent {
    protected StrategyManager strategyManager;
    protected ChoiceMemory memory;

    public AbstractIntelligentAgent(StrategyManager strategyManager, ChoiceMemory choiceMemory) {
        super();

        int strategyKeyLength = strategyManager.getStrategyKeyLength();
        int memoryCapacity = choiceMemory.getCapacity();

        if (strategyKeyLength != memoryCapacity) {
            throw new IllegalArgumentException(
                    "The Strategy key length for the supplied StrategyManager " +
                    "does not match the capacity of the supplied ChoiceMemory.");
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
