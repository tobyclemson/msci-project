package ic.msciproject.minoritygame;

import java.util.List;

/**
 * The LearningAgent class represents the simplest type of evolutionary agent
 * to be used in the minority game simulation. It can evolve its strategies by
 * removing the worst performing strategy and replacing it with another at
 * random and resetting its score to zero. At each turn the agent chooses a
 * strategy to use at using only the global choice history with the chosen
 * strategy being that with the highest score over all of the preceding time
 * steps.
 * @author tobyclemson
 */
public class LearningAgent extends AbstractAgent {

    /**
     * Constructs an instance of LearningAgent setting the strategies attribute
     * to the supplied StrategyManager instance.
     * @param strategyManager A StrategyManager instance representing this
     * agent's strategies.
     */
    public LearningAgent(StrategyManager strategyManager) {
        super(strategyManager);
    }

    /**
     * Increments this agents score by 1.
     */
    public void incrementScore() {
        score += 1;
    }

    /**
     * Calculates this agent's choice based on its strategyManager and sets the
     * lastChoice attribute to the resulting choice, either Choice.A or
     * Choice.B.
     * @param choiceHistory A List of Choice instances representing a fixed
     * number of past minority choices in the game.
     */
    public void choose(List<Choice> choiceHistory) {
        
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

    }

}
