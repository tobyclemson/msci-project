package msci.mg.agents;

import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

/**
 * The {@code BasicAgent} class represents an agent that makes a choice at each
 * turn by using one of a number of strategies associated with it which map
 * a fixed size list of past correct choices to a prediction for the next
 * correct choice.
 *
 * @author Toby Clemson
 */
public class BasicAgent extends AbstractIntelligentAgent {
    /**
     * Constructs a {@code BasicAgent} setting the strategy manager and
     * memory attributes to the supplied {@code StrategyManager} and
     * {@code ChoiceMemory} instances.
     *
     * @param strategyManager A {@code StrategyManager} instance representing
     * this agent's strategies.
     * @param choiceMemory A {@code ChoiceMemory} instance representing this
     * agent's memory of past minority choices.
     */
    public BasicAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    /**
     * Chooses using global information of the past correct choices.
     */
    public void choose() {
        Strategy chosenStrategy;

        if(choice == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        }
        else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }
        
        choice = chosenStrategy.predictMinorityChoice(memory.fetch());
    }

    /**
     * @param correctChoice The correct choice for the current turn.
     */
    @Override public void update(
        Choice correctChoice
    ) {
        super.update(correctChoice);
        strategyManager.incrementScores(memory.fetch(), correctChoice);
        memory.add(correctChoice);
    }
}
