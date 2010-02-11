package msci.mg;

/**
 * The NetworkedAgent class represents an agent that utilises local information
 * as well as global information when making a decision about which choice
 * to make at each step in the minority game. A NetworkedAgent has access
 * to a fixed set of strategies and a fixed size memory of past minority choices
 * as well as access to information from a number of other agents also involved
 * in the game. At each time step, an agent makes a choice based on its peers
 * suggestions and past success.
 * @author tobyclemson
 */
public class NetworkedAgent extends AbstractAgent {

    /**
     * Constructs an instance of BasicAgent setting the strategy manager and
     * memory attributes to the supplied StrategyManager and ChoiceMemory
     * instances.
     * @param strategyManager A StrategyManager instance representing this
     * agent's strategies.
     * @param choiceMemory A ChoiceMemory instance representing this agent's
     * memory of past minority choices.
     */
    public NetworkedAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    /**
     * Returns the agent that this agent has most recently followed. If no
     * choice has been made yet then an IllegalStateException is thrown.
     * @return This agent's best friend.
     */
    @Override
    public AbstractAgent getBestFriend() {
        if(bestFriend == null) {
            throw new IllegalStateException(
                "No choice has been made so this agent does not have a best " +
                "friend yet."
            );
        } else {
            return bestFriend;
        }
    }

    /**
     * Prepares this agent by calculating its prediction using this agent's
     * strategies to be publicised to the other agents in the social network.
     */
    @Override
    public void prepare() {
        // declare required variables
        Strategy chosenStrategy;

        // if no choice exists, get a strategy at random, otherwise fetch
        // the highest scoring strategy
        if(prediction == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        }
        else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }

        // use the chosen strategy to calculate the choice
        prediction = chosenStrategy.predictMinorityChoice(memory.fetch());
    }

    /**
     * Calculates this agent's choice based on its strategies and the
     * predictions of its friends and sets the choice attribute to the resulting
     * choice, either Choice.A or Choice.B.
     */
    @Override
    public void choose() {
        AbstractAgent newBestFriend =
            neighbourhood.getMostSuccessfulPredictor();

        this.choice = newBestFriend.getPrediction();
        this.bestFriend = newBestFriend;
    }

    /**
     * Updates the agent's local information with respect to the current
     * minority choice.
     * @param minorityChoice The current minority choice.
     */
    @Override
    public void update(
        Choice minorityChoice
    ) {
        super.update(minorityChoice);

        if(minorityChoice == prediction) {
            incrementCorrectPredictionCount();
        }

        strategyManager.incrementScores(memory.fetch(), minorityChoice);
        memory.add(minorityChoice);
    }

}
