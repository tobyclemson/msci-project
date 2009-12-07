package ic.msciproject.minoritygame;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

/**
 * The StrategyCollection class holds a collection of Strategy objects extending
 * the collection with strategy related functionality.
 * @author tobyclemson
 */
public class StrategyCollection extends AbstractCollection<Strategy> {

    /**
     * An AbstractDistribution, containing a random number generator that
     * implements the RandomGenerator interface, which returns random numbers
     * according to a particular distribution. This is used to populate
     * strategies with random predictions. By default it is initialised with a
     * Uniform distribution backed by a MersenneTwister random number engine
     * initialised with a Date object.
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
     * An ArrayList of Strategy instances representing the strategies held
     * by the StrategyCollection.
     */
    private ArrayList<Strategy> storage;

    /**
     * Constructs an empty StrategyCollection. Strategy objects can be later
     * added to the collection using the {@link #add(Strategy)} method.
     */
    public StrategyCollection() {
        super();
        storage = new ArrayList<Strategy>();
    }

    /**
     * Constructs a StrategyCollection instance copying across the strategies
     * from the supplied StrategyCollection instance.
     * @param otherStrategyCollection The StrategyCollection instance to be
     * copied.
     */
    public StrategyCollection(StrategyCollection otherStrategyCollection) {
        ArrayList<Strategy> other_storage = otherStrategyCollection.storage;
        storage = new ArrayList<Strategy>();
        Iterator<Strategy> iterator = other_storage.iterator();
        while(iterator.hasNext()){
            add(iterator.next());
        }
    }

    /**
     * Returns an iterator over the strategies stored in the StrategyCollection.
     * @return An iterator over the strategies.
     */
    public Iterator<Strategy> iterator() {
        return storage.iterator();
    }

    /**
     * Returns an integer representing the number of strategies in the
     * collection.
     * @return The number of strategies in the collection.
     */
    public int size() {
        return storage.size();
    }

    /**
     * Adds a strategy to the collection.
     * @param strategy The strategy to add.
     * @return {@code true} if the collection was modified.
     */
    @Override
    public boolean add(Strategy strategy) {
        return storage.add(strategy);
    }

    /**
     * Returns a strategy at random from the stored collection. The strategy is
     * chosen using a uniform probability distribution so each strategy is as
     * likely to be chosen as any other.
     * @return A random strategy from the stored collection.
     */
    public Strategy getRandomStrategy() {
        return storage.get(getRandomIndex(storage.size()));
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
        // declare required variables
        Strategy currentStrategy = storage.get(0),
                 highestStrategy = storage.get(0);

        // iterate through the strategies replacing the highest strategy if the
        // current strategy has a higher score or randomly replicing if the
        // score is the same
        int max = storage.size();
        for(int i = 1; i < max; i++) {
            currentStrategy = storage.get(i);

            if(currentStrategy.getScore() > highestStrategy.getScore()) {
                highestStrategy = currentStrategy;
            } else if(
               currentStrategy.getScore() == highestStrategy.getScore()
            ) {
                if(randomNumberGenerator.nextInt() == 1) {
                    highestStrategy = currentStrategy;
                }
            }
        }

        // return the strategy with the highest score found
        return highestStrategy;
    }

    public void incrementScoresForChoice(
        List<Choice> choiceHistory, Choice minorityChoice
    ) {
        // declare required variables
        Strategy currentStrategy = null;
        Iterator<Strategy> strategyIterator = storage.iterator();
        Choice prediction;

        // increment the score of each strategy that gives the correct
        // prediction for the supplied choice history
        while(strategyIterator.hasNext()) {
            currentStrategy = strategyIterator.next();
            prediction = currentStrategy.predictMinorityChoice(choiceHistory);
            if(prediction.equals(minorityChoice)) {
                currentStrategy.incrementScore();
            }
        }
    }

    private int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
    }
}
