package ic.msciproject.minoritygame;

import java.util.List;

/**
 * The AbstractAgent class implements the basic functionality of an agent in the
 * minority game and all specific agents should extend it.
 * @author tobyclemson
 */
public class AbstractAgent {
    /**
     * A StrategyCollection instance holding the agent's strategies. This is
     * returned by the {@link #getStrategies} method.
     */
    private StrategyCollection strategies;

    /**
     * An integer representing the agent's score. This is returned by the
     * {@link #getScore} method.
     */
    private int score = 0;

    /**
     * A Choice instance representing the last choice made by this agent. This
     * is returned by the {@link #getLastChoice} method.
     */
    protected Choice lastChoice = null;

    /**
     * Constructs an AbstractAgent instance setting the strategies attribute to
     * the supplied StrategyCollection instance.
     * @param strategyCollection A StrategyCollection instance representing
     * the agent's strategies.
     */
    public AbstractAgent(StrategyCollection strategyCollection){
        this.strategies = strategyCollection;
    }

    /**
     * Returns a StrategyCollection containing the strategies associated with
     * this agent.
     * @return A StrategyCollection containing the agent's strategies.
     */
    public StrategyCollection getStrategies() {
        return strategies;
    }

    /**
     * Returns the current score of the agent as an integer.
     * @return The agent's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the last choice made by the agent, either Choice.A or Choice.B.
     * If the {@link #choose} method has not yet been called, an
     * IllegalStateException is thrown.
     * @return The last choice made by the agent.
     */
    public Choice getLastChoice() {
        if(lastChoice == null) {
            throw new IllegalStateException(
                "No choice has been made yet so no last choice exists."
            );
        }
        return lastChoice;
    }

    /**
     * Calculates this agent's choices based on its strategies and returns
     * a Choice.A or Choice.B representing the outcome of the choice.
     * @param choiceHistory A List of Choice instances representing a fixed
     * number of past minority choices in the game.
     * @return The choice made by the agent.
     */
    public Choice choose(List<Choice> choiceHistory) {
        // declare required variables
        Strategy chosenStrategy;
        Choice choice;

        // if no lastChoice exists, get a strategy at random, otherwise fetch
        // the highest scoring strategy
        if(lastChoice == null) {
            chosenStrategy = strategies.getRandomStrategy();
        }
        else {
            chosenStrategy = strategies.getHighestScoringStrategy();
        }
        
        // use the chosen strategy to calculate the choice
        lastChoice = choice = chosenStrategy.get(choiceHistory);

        // return the selected choice
        return choice;
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
        strategies.incrementScoresForChoice(choiceHistory, minorityChoice);
    }
}
