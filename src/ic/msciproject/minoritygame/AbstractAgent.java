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
     * An instance of ChoiceMemory representing the agent's memory. This is
     * returned by the {@link #getMemory} method.
     */
    protected ChoiceMemory memory;

    /**
     * A Choice instance representing the last choice made by this agent. This
     * is returned by the {@link #getLastChoice} method.
     */
    protected Choice lastChoice = null;

    /**
     * Constructs an instance of AbstractAgent setting the strategy manager and
     * memory attributes to the supplied StrategyManager and ChoiceMemroy
     * instances.
     * @param strategyManager A StrategyManager instance containing the agent's
     * strategies.
     * @param choiceMemory A ChoiceMemory instance representing the agent's
     * memory of past minority choices.
     */
    public AbstractAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ){
        this.strategyManager = strategyManager;
        this.memory = choiceMemory;
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
     * Returns the ChoiceMemory instance associated with the agent.
     * @return The agent's memory.
     */
    public ChoiceMemory getMemory() {
        return memory;
    }

    /**
     * Tells the agent to choose between Choice.A and Choice.B.
     */
    public abstract void choose();

    /**
     * Increments the agent's score.
     */
    public abstract void incrementScore();

    /**
     * Tells the agent to update its internal state given that the minority
     * choice was as specified in the last time step.
     * @param minorityChoice The minority choice in the last step.
     */
    public abstract void update(Choice minorityChoice);

}
