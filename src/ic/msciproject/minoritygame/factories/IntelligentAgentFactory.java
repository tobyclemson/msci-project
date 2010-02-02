package ic.msciproject.minoritygame.factories;

import ic.msciproject.minoritygame.Choice;
import java.util.List;
import org.apache.commons.collections15.Factory;

/**
 * The IntelligentAgentFactory class declares methods to construct and
 * initialise dependencies for a factory that creates agents with some level
 * of intelligence. It is an abstract class and subclasses must override the
 * {@link #create} method.
 * @author tobyclemson
 */
public abstract class IntelligentAgentFactory extends AgentFactory {

    /**
     * A Factory that returns an Integer representing the required agent memory
     * capacity.
     */
    private Factory<Integer> memoryCapacityFactory;

    /**
     * A Factory that returns an Integer representing the number of strategies
     * each agent should have.
     */
    private Factory<Integer> numberOfStrategiesFactory;

    /**
     * A List of Choice enumerations representing the inital choice memory with
     * which each agent should be populated.
     */
    private List<Choice> initialChoiceMemory;

    /**
     * Constructs an IntelligentAgentFactory which creates agents with a memory
     * capacity and strategy count corresponding to the values returned by the
     * supplied value factories along with an initial choice memory populated
     * with the supplied list of choices.
     * @param memoryCapacityFactory A factory returning a value representing the
     * memory capacity each agent should have.
     * @param numberOfStrategiesFactory A factory returning a value representing
     * the number of strategies each agent should have.
     * @param initialChoiceMemory A list of choices representing the initial
     * choice memory each agent should be populated with.
     */
    public IntelligentAgentFactory(
        Factory<Integer> memoryCapacityFactory,
        Factory<Integer> numberOfStrategiesFactory,
        List<Choice> initialChoiceMemory
    ) {
        this.memoryCapacityFactory = memoryCapacityFactory;
        this.numberOfStrategiesFactory = numberOfStrategiesFactory;
        this.initialChoiceMemory = initialChoiceMemory;
    }

    /**
     * Returns the factory which provides memory capacities to this agent
     * factory.
     * @return The associated memory capacity factory.
     */
    public Factory<Integer> getMemoryCapacityFactory() {
        return this.memoryCapacityFactory;
    }

    /**
     * Returns the factory which provides the number of strategies required by
     * agents constructed by this agent factory.
     * @return The associated number of strategies factory.
     */
    public Factory<Integer> getNumberOfStrategiesFactory() {
        return this.numberOfStrategiesFactory;
    }

    /**
     * Returns the list of choices representing the initial choice memory each
     * agent should be populated with.
     * @return The initial choice memory that agent's created by this factory
     * will be populated with.
     */
    public List<Choice> getInitialChoiceMemory() {
        return this.initialChoiceMemory;
    }

    /**
     * Sets the memory capacity factory to the supplied factory.
     * @param memoryCapacityFactory A factory returning a value representing the
     * memory capacity each agent should have.
     */
    public void setMemoryCapacityFactory(
        Factory<Integer> memoryCapacityFactory
    ) {
        this.memoryCapacityFactory = memoryCapacityFactory;
    }

    /**
     * Sets the number of strategies factory to the supplied factory.
     * @param numberOfStrategiesFactory A factory returning a value representing
     * the number of strategies each agent should have.
     */
    public void setNumberOfStrategiesFactory(
        Factory<Integer> numberOfStrategiesFactory
    ) {
        this.numberOfStrategiesFactory = numberOfStrategiesFactory;
    }

}
