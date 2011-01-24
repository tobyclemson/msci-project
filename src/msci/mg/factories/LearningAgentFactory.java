package msci.mg.factories;

import msci.mg.*;
import msci.mg.agents.LearningAgent;
import org.apache.commons.collections15.Factory;

import java.util.ArrayList;
import java.util.List;

public class LearningAgentFactory extends IntelligentAgentFactory {
    public LearningAgentFactory(
            Factory<Integer> memoryCapacityFactory,
            Factory<Integer> numberOfStrategiesFactory,
            List<Choice> initialChoiceMemory) {
        super(memoryCapacityFactory, numberOfStrategiesFactory, initialChoiceMemory);
    }

    @Override
    public LearningAgent create() {
        int requiredMemoryCapacity = getMemoryCapacityFactory().create();
        int requiredNumberOfStrategies = getNumberOfStrategiesFactory().create();
        List<Choice> requiredInitialChoiceMemory = getInitialChoiceMemory();

        StrategySpace strategySpace = new StrategySpace(requiredMemoryCapacity);

        ArrayList<Strategy> strategies = new ArrayList<Strategy>();

        for (int j = 0; j < requiredNumberOfStrategies; j++) {
            strategies.add(strategySpace.generateStrategy());
        }

        StrategyManager strategyManager = new StrategyManager(strategies);
        ChoiceMemory choiceMemory = new ChoiceMemory(requiredMemoryCapacity, requiredInitialChoiceMemory);

        return new LearningAgent(strategyManager, choiceMemory);
    }

}
