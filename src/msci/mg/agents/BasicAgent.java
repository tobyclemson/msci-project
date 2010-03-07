package msci.mg.agents;

import java.util.List;
import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

/**
 * The BasicAgent class represents the simplest type of agent to be used in the
 * minority game simulation. It cannot evolve its strategies and chooses a
 * strategy to use at each turn using only the global choice history with the
 * chosen strategy being that with the highest score over all of the preceding
 * time steps.
 * @author tobyclemson
 */
public class BasicAgent extends AbstractIntelligentAgent {
    /**
     * Constructs an instance of BasicAgent setting the strategy manager and
     * memory attributes to the supplied StrategyManager and ChoiceMemory
     * instances.
     * @param strategyManager A StrategyManager instance representing this
     * agent's strategies.
     * @param choiceMemory A ChoiceMemory instance representing this agent's
     * memory of past minority choices.
     */
    public BasicAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    /**
     * Calculates this agent's choice based on its strategies and sets the
     * choice attribute to the resulting choice, either Choice.A or
     * Choice.B.
     */
    public void choose() {
        // declare required variables
        Strategy chosenStrategy;

        // if no choice exists, get a strategy at random, otherwise fetch
        // the highest scoring strategy
        if(choice == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        }
        else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }

        // use the chosen strategy to calculate the choice
        choice = chosenStrategy.predictMinorityChoice(memory.fetch());
    }

    /**
     * Updates the agent's local information with respect to the current 
     * minority choice.
     * @param minorityChoice The current minority choice.
     */
    @Override
    public void update(
        Choice minorityChoice
    ) {
        super.update(minorityChoice);
        strategyManager.incrementScores(memory.fetch(), minorityChoice);
        memory.add(minorityChoice);
    }

}
