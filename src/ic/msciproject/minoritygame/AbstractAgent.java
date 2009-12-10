package ic.msciproject.minoritygame;

import java.util.List;

/**
 * The AbstractAgent class implements the basic functionality of an agent in the
 * minority game and all specific agents should extend it.
 * @author tobyclemson
 */
public abstract class AbstractAgent {
    
    /**
     * A StrategyManager instance holding the agent's strategies. This is
     * returned by the {@link #getStrategyManager} method.
     */
    protected StrategyManager strategyManager;

    /**
     * An integer representing the agent's score. This is returned by the
     * {@link #getScore} method.
     */
    protected int score = 0;

    /**
     * A Choice instance representing the last choice made by this agent. This
     * is returned by the {@link #getLastChoice} method.
     */
    protected Choice lastChoice = null;

    /**
     * Constructs an AbstractAgent instance setting the strategyManager 
     * attribute to the supplied StrategyManager instance.
     * @param strategyManager A StrategyManager instance containing the agent's
     * strategies.
     */
    public AbstractAgent(StrategyManager strategyManager){
        this.strategyManager = strategyManager;
    }

    /**
     * Returns a List of Strategy instances representing the strategies
     * associated with the agent.
     * @return The agent's strategies.
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
     * Tells the agent to choose between Choice.A and Choice.B dependent on the
     * choice history supplied.
     * @param choiceHistory A List of past Choice outcomes.
     */
    public abstract void choose(List<Choice> choiceHistory);

    /**
     * Increments the agent's score.
     */
    public abstract void incrementScore();

    /**
     * Tells the agent to update its internal state given that the minority
     * choice was as specified for the specified choice history.
     * @param minorityChoice The minority choice in the last step.
     * @param choiceHistory The choice history that was used as input to each
     * agent at the start of the step.
     */
    public abstract void update(
        Choice minorityChoice,
        List<Choice> choiceHistory
    );

}
