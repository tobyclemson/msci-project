package ic.msciproject.minoritygame;

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
     * A String which contains either "0" or "1" representing the last choice
     * made by this agent.
     */
    private String lastChoice = null;

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
     * Returns the last choice made by the agent, either "0" or "1". If the
     * {@link #makeChoice} method has not yet been called, an
     * IllegalStateException is thrown.
     * @return The last choice made by the agent.
     */
    public String getLastChoice() {
        if(lastChoice == null) {
            throw new IllegalStateException(
                "No choice has been made yet so no last choice exists."
            );
        }
        return lastChoice;
    }

    /**
     * Calculates this agents choice based on its strategies and returns
     * either "0" or "1" depending on the outcome of the choice.
     * @param historyString A string of '0's and '1's representing the minority
     * choice from a fixed number of previous time steps.
     * @return The choice made my the agent.
     */
    public String makeChoice(String historyString) {
        // declare required variables
        Strategy chosenStrategy;
        String choice;

        // if no lastChoice exists, get a strategy at random, otherwise fetch
        // the highest scoring strategy
        if(lastChoice == null)
            chosenStrategy = strategies.getRandomStrategy();
        else
            chosenStrategy = strategies.getHighestScoringStrategy();

        // use the chosen strategy to calculate the choice
        lastChoice = choice = chosenStrategy.get(historyString);

        // return the selected choice
        return choice;
    }

    public void incrementScore() {
        score += 1;
    }

    public void updateForChoice(String minorityChoice, String historyString) {
        strategies.incrementScoresForChoice(minorityChoice, historyString);
    }
}
