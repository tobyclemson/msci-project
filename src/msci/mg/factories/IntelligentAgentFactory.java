package msci.mg.factories;

import msci.mg.Choice;
import org.apache.commons.collections15.Factory;

import java.util.List;

public abstract class IntelligentAgentFactory extends AgentFactory {
    private Factory<Integer> memoryCapacityFactory;
    private Factory<Integer> numberOfStrategiesFactory;
    private List<Choice> initialChoiceMemory;

    public IntelligentAgentFactory(
            Factory<Integer> memoryCapacityFactory,
            Factory<Integer> numberOfStrategiesFactory,
            List<Choice> initialChoiceMemory) {
        this.memoryCapacityFactory = memoryCapacityFactory;
        this.numberOfStrategiesFactory = numberOfStrategiesFactory;
        this.initialChoiceMemory = initialChoiceMemory;
    }

    public Factory<Integer> getMemoryCapacityFactory() {
        return this.memoryCapacityFactory;
    }

    public Factory<Integer> getNumberOfStrategiesFactory() {
        return this.numberOfStrategiesFactory;
    }

    public List<Choice> getInitialChoiceMemory() {
        return this.initialChoiceMemory;
    }

    public void setMemoryCapacityFactory(Factory<Integer> memoryCapacityFactory) {
        this.memoryCapacityFactory = memoryCapacityFactory;
    }

    public void setNumberOfStrategiesFactory(Factory<Integer> numberOfStrategiesFactory) {
        this.numberOfStrategiesFactory = numberOfStrategiesFactory;
    }

    public void setInitialChoiceMemory(List<Choice> initialChoiceMemory) {
        this.initialChoiceMemory = initialChoiceMemory;
    }

}
