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
     * A Choice instance representing the agent's current choice. This is
     * returned by the {@link #getChoice} method.
     */
    protected Choice choice = null;

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
        if(!(strategyManager.getStrategyKeyLength() ==
            choiceMemory.getCapacity())
        ) {
            throw new IllegalArgumentException(
                "The Strategy key length for the supplied StrategyManager " +
                "does not match the capacity of the supplied ChoiceMemory."
            );
        }
        
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
     * Returns the choice made by the agent, either Choice.A or Choice.B.
     * If the {@link #choose} method has not yet been called, an
     * IllegalStateException is thrown.
     * @return The choice made by the agent.
     */
    public Choice getChoice() {
        if(choice == null) {
            throw new IllegalStateException(
                "No choice has been made yet."
            );
        }
        return choice;
    }

    /**
     * Returns the ChoiceMemory instance associated with the agent.
     * @return The agent's memory.
     */
    public ChoiceMemory getMemory() {
        return memory;
    }

    /**
     * Increments the agent's score. The default implementation adds 1 to the
     * current score.
     */
    public void incrementScore() {
        score += 1;
    }

    /**
     * Tells the agent to prepare itself for the next choice making cycle. The
     * default implementation does nothing.
     */
    public void prepare() {}

    /**
     * Tells the agent to choose between Choice.A and Choice.B.
     */
    public abstract void choose();

    /**
     * Tells the agent to update its internal state given that the current 
     * minority choice is as specified. The default implementation calls 
     * {@link incrementScore} if the supplied minorityChoice is equal to the
     * current choice.
     * @param minorityChoice The current minority choice.
     */
    public void update(Choice minorityChoice) {
        if(choice == minorityChoice) {
            incrementScore();
        }
    }

}
