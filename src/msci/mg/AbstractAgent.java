package msci.mg;

import java.util.List;
import edu.uci.ics.jung.graph.Graph;

/**
 * The AbstractAgent class implements the basic functionality of an agent in the
 * minority game and all specific agents should extend it.
 * @author tobyclemson
 */
public abstract class AbstractAgent implements Comparable<AbstractAgent> {

    /**
     * An integer representing the identification number to
     * assign to the next constructed agent also doubling up as a count of the
     * number of agents that have been constructed so far.
     */
    private static int nextAgentIdentificationNumber = 0;
    
    /**
     * A StrategyManager instance holding this agent's strategies. This is
     * returned by the {@link #getStrategyManager} method.
     */
    protected StrategyManager strategyManager;

    /**
     * An integer representing this agent's identification number. This is used
     * to sort collections of agents.
     */
    protected int identificationNumber;

    /**
     * An integer representing this agent's score. This is returned by the
     * {@link #getScore} method and incremented by the {@link #incrementScore}
     * method.
     */
    protected int score = 0;

    /**
     * An integer representing the number of times this agent has predicted
     * the correct minority choice. This is returned by the {@link
     * #getCorrectPredictionCount} method and incremented by the {@link
     * #incrementCorrectPredictionCount} method.
     */
    protected int correctPredictionCount = 0;

    /**
     * An instance of ChoiceMemory representing this agent's memory. This is
     * returned by the {@link #getMemory} method.
     */
    protected ChoiceMemory memory;

    /**
     * An instance of Neighbourhood representing this agent's neighbourhood of
     * friends. This is returned by the {@link #getNeighbourhood} method.
     */
    protected Neighbourhood neighbourhood;

    /**
     * An AbstractAgent instance representing this agent's best friend, i.e.,
     * the agent whose prediction this agent most recently followed. This is
     * returned by the {@link #getBestFriend} method.
     */
    protected AbstractAgent bestFriend;

    /**
     * A graph representing this agents social network of friends. This is
     * returned by the {@link #getSocialNetwork} method.
     */
    protected Graph<AbstractAgent, Friendship> socialNetwork;

    /**
     * A Choice instance representing this agent's prediction for the minority
     * choice in this time step. This is returned by the {@link #getPrediction}
     * method.
     */
    protected Choice prediction = null;

    /**
     * A Choice instance representing this agent's current choice. This is
     * returned by the {@link #getChoice} method.
     */
    protected Choice choice = null;

    /**
     * Constructs an instance of AbstractAgent setting the strategy manager and
     * memory attributes to the supplied StrategyManager and ChoiceMemroy
     * instances.
     * @param strategyManager A StrategyManager instance containing this agent's
     * strategies.
     * @param choiceMemory A ChoiceMemory instance representing this agent's
     * memory of past minority choices.
     */
    public AbstractAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ){
        // ensure the strategy manager and choice memory match
        if(!(strategyManager.getStrategyKeyLength() ==
            choiceMemory.getCapacity())
        ) {
            throw new IllegalArgumentException(
                "The Strategy key length for the supplied StrategyManager " +
                "does not match the capacity of the supplied ChoiceMemory."
            );
        }

        // set the strategy manager and memory to the supplied objects
        this.strategyManager = strategyManager;
        this.memory = choiceMemory;

        // set the agent's identification number to the next available integer
        this.identificationNumber = nextAgentIdentificationNumber;
        nextAgentIdentificationNumber += 1;
    }

    /**
     * Constructs an instance of AbstractAgent setting the strategy manager and
     * memory attributes to null. It is intended that this will become
     * unnecessary after the next refactor.
     * TODO: sort out the interface to this class!
     */
    public AbstractAgent() {
        // set the strategy manager and memory to null
        this.strategyManager = null;
        this.memory = null;

        // set the agent's identification number to the next available integer
        this.identificationNumber = nextAgentIdentificationNumber;
        nextAgentIdentificationNumber += 1;
    }

    /**
     * Sets this agents neighbourhood to the supplied graph.
     * @param neighbourhood The neighbourhood to be set.
     */
    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * Returns this agent's identification number. No two agents will have the
     * same identification number and one pool of identification numbers is
     * shared amongst all subclasses.
     * @return This agent's identification number.
     */
    public int getIdentificationNumber() {
        return this.identificationNumber;
    }

    /**
     * Returns a List of Strategy instances representing the strategies
     * associated with this agent.
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
     * Returns the current score of this agent as an integer.
     * @return This agent's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the number of times this agent has predicted the correct minority
     * choice.
     * @return This agent's correct prediction count.
     */
    public int getCorrectPredictionCount() {
        return correctPredictionCount;
    }

    /**
     * Returns the choice made by this agent, either Choice.A or Choice.B.
     * If the {@link #choose} method has not yet been called, an
     * IllegalStateException is thrown.
     * @return The choice made by this agent.
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
     * Returns the prediction made by this agent, either Choice.A or Choice.B.
     * If no prediction has been made yet, an IllegalStateException is thrown.
     * @return The prediction for the minority choice this turn as made by this
     * agent.
     */
    public Choice getPrediction() {
        if(prediction == null) {
            throw new IllegalStateException(
                "No prediction has been made yet"
            );
        }
        return prediction;
    }

    /**
     * Returns the ChoiceMemory instance associated with this agent.
     * @return This agent's memory.
     */
    public ChoiceMemory getMemory() {
        return memory;
    }

    /**
     * Returns an instance of Neighbourhood representing this agent's
     * neighbourhood of friends.
     * @return This agent's neighbourhood.
     */
    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Returns a List of Agents that are friends with this agent.
     * @return a list of this agent's friends.
     */
    public List<AbstractAgent> getFriends() {
        if(this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }

        return this.neighbourhood.getFriends();
    }

    /**
     * Returns this agent's best friend, i.e., the agent whose prediction this
     * agent most recently followed. In the default implementation, since
     * agents have no knowledge of each other, a reference to this agent is
     * returned since this agent always follows its own predictions.
     * @return This agent's best friend.
     */
    public AbstractAgent getBestFriend() {
        return this;
    }

    /**
     * Returns the social network associated with this agent. This network will
     * always be a star network centered on this agent with edges joined to each
     * of this agent's friends.
     * @return A graph representing this agent's social network.
     */
    public Graph<AbstractAgent, Friendship> getSocialNetwork() {
        if(this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }

        return neighbourhood.getSocialNetwork();
    }

    /**
     * Compares this agent to the supplied agent. If this agent's identification
     * number is less than the other agents, the method returns a negative
     * number; if both agent's identification numbers are the same, the method
     * returns 0; if the other agents identification number is greater than
     * this agent's, the method returns a positive number.
     * @param otherAgent The agent to compare to.
     * @return An integer whose sign depends on the comparison of each agent's
     * identification number.
     */
    public int compareTo(AbstractAgent otherAgent) {
        if(otherAgent == null) {
            throw new NullPointerException();
        }

        if(this.identificationNumber < otherAgent.getIdentificationNumber()) {
            return -1;
        } else if(
            this.identificationNumber == otherAgent.getIdentificationNumber()
        ) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Increments the agent's score. The default implementation adds 1 to the
     * current score.
     */
    public void incrementScore() {
        score += 1;
    }

    /**
     * Increments the agent's correct prediction count. The default
     * implementation adds 1 to the correct prediction count.
     */
    public void incrementCorrectPredictionCount() {
        correctPredictionCount += 1;
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
     * current choice and adds the minority choice to this agent's memory.
     * @param minorityChoice The current minority choice.
     */
    public void update(Choice minorityChoice) {
        if(choice == minorityChoice) {
            incrementScore();
        }
    }

}
