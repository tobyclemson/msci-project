package ic.msciproject.minoritygame;

import java.util.List;

/**
 * The AbstractAgent class implements the basic functionality of an agent in the
 * minority game and all specific agents should extend it.
 * @author tobyclemson
 */
public class AbstractAgent {
    
    /**
     * A StrategyManager instance holding the agent's strategyManager. This is
     * returned by the {@link #getStrategies} method.
     */
    private StrategyManager strategyManager;

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
     * Constructs an AbstractAgent instance setting the strategyManager 
     * attribute to the supplied StrategyManager instance.
     * @param strategyManager A StrategyManager instance representing
     * the agent's strategyManager.
     */
    public AbstractAgent(StrategyManager strategyManager){
        this.strategyManager = strategyManager;
    }

    /**
     * Returns a StrategyManager containing the strategyManager associated with
     * this agent.
     * @return A StrategyManager containing the agent's strategyManager.
     */
    public List<Strategy> getStrategies() {
        return strategyManager.getStrategies();
    }

    /**
     * Returns the StrategyManager instance associated with this agent
     * representing the strategies the agent employs to make choices.
     * @return The StrategyManager instance associates with this agent.
     */
    public StrategyManager getStrategyManager() {
        return this.strategyManager;
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
     * Calculates this agent's choices based on its strategyManager and returns
     * a Choice.A or Choice.B representing the outcome of the choice.
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
        
        // use the chosen strategy to calculate the choice and set the last
        // choice attribute.
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
