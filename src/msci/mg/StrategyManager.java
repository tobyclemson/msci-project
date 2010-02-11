package msci.mg;

import java.util.List;
import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import java.util.ArrayList;

/**
 * The StrategyManager class holds a collection of Strategy objects extending
 * the collection with strategy related functionality.
 * @author tobyclemson
 */
public class StrategyManager {

    /**
     * An AbstractDistribution, containing a random number generator that
     * implements the RandomGenerator interface, which returns random numbers
     * according to a particular distribution. This is used to populate
     * strategies with random predictions. By default it is initialised with a
     * Uniform distribution backed by a MersenneTwister random number engine
     * initialised with a random integer seed.
     */
    private static AbstractDistribution randomNumberGenerator;

    // initialise the random number generator
    static {
        /*
         * generate an integer at random spanning the entire range of possible
         * integers (except the endpoints) to be used as a seed
         */
        int randomSeed = (int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE);
        
        /*
         * construct a uniform distribution backed by a MersenneTwister random
         * number generator using the random seed and set the static random
         * number generator to the constructed object.
         */
        randomNumberGenerator = new Uniform(new MersenneTwister(randomSeed));
    }

    /**
     * A List of Strategy instances representing the strategies held by the
     * StrategyManager.
     */
    private List<Strategy> strategyStorage;

    /**
     * An integer representing the key size of the Strategy objects held by the
     * StrategyManager.
     */
    private int strategyKeyLength;

    /**
     * Constructs a StrategyManager instance to act on the supplied strategies.
     * @param strategies The Strategy objects to be managed by the
     * StrategyManager.
     */
    public StrategyManager(List<Strategy> strategies) {
        this.strategyStorage = strategies;
        this.strategyKeyLength = strategies.get(0).getKeyLength();
    }

    /**
     * Returns an integer representing the number of Strategy instances managed
     * by the StrategyManager.
     * @return The number of strategies managed by the strategy manager.
     */
    public int getNumberOfStrategies() {
        return strategyStorage.size();
    }

    /**
     * Returns an integer representing the key length of the Strategy instances
     * managed by the StrategyManager.
     * @return The key length of strategies managed by the strategy manager.
     */
    public int getStrategyKeyLength() {
        return strategyKeyLength;
    }

    /**
     * Returns a List of Strategy instances representing the strategies
     * employed by the agent to make choices.
     * @return The agent's strategies.
     */
    public List<Strategy> getStrategies() {
        return strategyStorage;
    }

    /**
     * Returns a strategy at random from the stored collection. The strategy is
     * chosen using a uniform probability distribution so each strategy is as
     * likely to be chosen as any other.
     * @return A random strategy from the stored collection.
     */
    public Strategy getRandomStrategy() {
        return strategyStorage.get(getRandomIndex(strategyStorage.size()));
    }

    /**
     * Returns the strategy in the stored collection with the highest score. If
     * more than one strategy has the same score and that score is the highest
     * then one of them is returned at random. The probability distribution used
     * in this case is uniform so each of the highest scoring strategies has
     * the same chance of being returned.
     * @return A strategy with the highest score from the stored collection.
     */
    public Strategy getHighestScoringStrategy() {
        // fetch the first strategy from the strategy store
        Strategy firstStrategy = strategyStorage.get(0);

        // declare required variables
        List<Strategy> highestScoringStrategies = new ArrayList<Strategy>();
        int highestScore;

        // temporarily set the first strategy as the only highest scoring one
        highestScoringStrategies.add(firstStrategy);
        highestScore = firstStrategy.getScore();

        // iterate through the strategies replacing the highest strategy if the
        // current strategy has a higher score or adding the strategy to the
        // array of highest scoring strategies if it has the same score.
        for(
            Strategy currentStrategy : strategyStorage.subList(
                1, strategyStorage.size()
            )
        ) {
            if(currentStrategy.getScore() > highestScore) {
                highestScoringStrategies.clear();
                highestScoringStrategies.add(currentStrategy);
                highestScore = currentStrategy.getScore();
            } else if(
               currentStrategy.getScore() == highestScore
            ) {
               highestScoringStrategies.add(currentStrategy);
            }
        }

        // return a strategy at random from the array of highes scoring
        // strategies.
        return highestScoringStrategies.get(
            getRandomIndex(highestScoringStrategies.size())
        );
    }

    /**
     * Calls {@link Strategy#incrementScore} on any strategy for which the
     * prediction for the supplied choice history is equal to the supplied
     * minority choice.
     * @param choiceHistory The choice history of which to check the prediction.
     * @param minorityChoice The current minority choice.
     */
    public void incrementScores(
        List<Choice> choiceHistory, Choice minorityChoice
    ) {
        // declare required variables
        Choice prediction;

        // increment the score of each strategy that gives the correct
        // prediction for the supplied choice history
        for(Strategy currentStrategy : strategyStorage) {
            prediction = currentStrategy.predictMinorityChoice(choiceHistory);
            if(prediction.equals(minorityChoice)) {
                currentStrategy.incrementScore();
            }
        }
    }

    /**
     * Returns a random integer in the range [0,arraySize[ representing a
     * random array index.
     * @param arraySize The number of elements in the array for which the index
     * is required.
     * @return An integer in the range [0,arraySize[
     */
    private int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
    }
}
