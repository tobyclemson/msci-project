package ic.msciproject.minoritygame;

import java.util.List;

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
     * to the supplied StrategyManager instance.
     * @param strategyManager A StrategyManager instance representing the
     * agent's strategies.
     */
    public BasicAgent(StrategyManager strategyManager) {
        super(strategyManager);
    }

    /**
     * Calculates this agent's choice based on its strategyManager and sets the
     * lastChoice attribute to the resulting choice, either Choice.A or
     * Choice.B.
     * @param choiceHistory A List of Choice instances representing a fixed
     * number of past minority choices in the game.
     */
    public void choose(List<Choice> choiceHistory) {
        // declare required variables
        Strategy chosenStrategy;

        // if no lastChoice exists, get a strategy at random, otherwise fetch
        // the highest scoring strategy
        if(lastChoice == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        }
        else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }

        // use the chosen strategy to calculate the choice
        lastChoice = chosenStrategy.predictMinorityChoice(
            choiceHistory
        );
    }

    /**
     * Increments this agents score by 1.
     */
    public void incrementScore() {
        score += 1;
    }

    /**
     * Updates the agent's local information with respect to the minority
     * choice and choice history for the last time step.
     * @param minorityChoice The minority choice for the last time step.
     * @param choiceHistory The choice history at the start of the last time
     * step.
     */
    public void update(
        Choice minorityChoice,
        List<Choice> choiceHistory
    ) {
        strategyManager.incrementScores(choiceHistory, minorityChoice);
    }

}
