package ic.msciproject.minoritygame.factories;

import ic.msciproject.minoritygame.BasicAgent;
import ic.msciproject.minoritygame.Choice;
import ic.msciproject.minoritygame.ChoiceMemory;
import ic.msciproject.minoritygame.Strategy;
import ic.msciproject.minoritygame.StrategyManager;
import ic.msciproject.minoritygame.StrategySpace;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections15.Factory;

/**
 * The BasicAgentFactory creates agents with a particular memory length and
 * number of strategies.
 * @author tobyclemson
 */
public class BasicAgentFactory extends IntelligentAgentFactory{

    /**
     * Constructs a BasicAgentFactory which creates agents with a memory
     * capacity and strategy count corresponding to the values returned by the
     * supplied value factories.
     * @param memoryCapacityFactory A factory returning a value representing the
     * memory capacity each agent should have.
     * @param numberOfStrategiesFactory A factory returning a value representing
     * the number of strategies each agent should have.
     * @param initialChoiceMemory A list of choices representing the initial
     * choice memory each agent should be populated with.
     */
    public BasicAgentFactory(
        Factory<Integer> memoryCapacityFactory,
        Factory<Integer> numberOfStrategiesFactory,
        List<Choice> initialChoiceMemory
    ) {
        super(
            memoryCapacityFactory,
            numberOfStrategiesFactory,
            initialChoiceMemory
        );
    }

    /**
     * Creates a basic agent with memory capacity and strategy count
     * corresponding to the values returned by the supplied memory capacity and
     * strategy count factories.
     * @return An instance of BasicAgent initialised according to the
     * supplied memory capacity and strategy count factories.
     */
    @Override
    public BasicAgent create() {
        // fetch the agent requirements
        int requiredMemoryCapacity = getMemoryCapacityFactory().create();
        int requiredNumberOfStrategies =
            getNumberOfStrategiesFactory().create();
        List<Choice> requiredInitialChoiceMemory = getInitialChoiceMemory();

        // create a StrategySpace for strategies corresponding to the specified
        // memory capacity
        StrategySpace strategySpace = new StrategySpace(requiredMemoryCapacity);

        // populate an ArrayList with the number of strategies specified by the
        // supplied strategy count factory
        ArrayList<Strategy> strategies = new ArrayList<Strategy>();
        for(int j = 0; j < requiredNumberOfStrategies; j++) {
            strategies.add(strategySpace.generateStrategy());
        }

        // create StrategyManager and ChoiceMemory instances using the
        // generated list of strategies and the specified memory capacity
        StrategyManager strategyManager = new StrategyManager(strategies);
        ChoiceMemory choiceMemory = new ChoiceMemory(
            requiredMemoryCapacity, requiredInitialChoiceMemory
        );

        // create and return a BasicAgent using the StrategyManager and
        // ChoiceMemory instances
        return new BasicAgent(strategyManager, choiceMemory);
    }

}
