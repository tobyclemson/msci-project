package msci.mg;

import java.util.HashMap;
import java.util.Set;
import java.util.List;
import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

/**
 * Instances of the StrategySpace class represents the space of all possible
 * Strategy objects of a particular key length. Such instances can be used to
 * generate and return strategies at random from the space.
 * @author tobyclemson
 */
public class StrategySpace {
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
         * integers as a seed
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
     * An integer holding the length of the keys in the strategies in this
     * strategy space. This is returned by the {@link #getKeyLength} method.
     */
    private int keyLength;

    /**
     * A Set of Strings representing the keys required for a strategy generated
     * by the StrategySpace to be valid.
     */
    private Set<List<Choice>> requiredKeys;

    /**
     * An array of the possible choices in the game.
     */
    private static final Choice choiceSet[] = {Choice.A, Choice.B};

    /**
     * Constructs a StrategySpace for keys of the specified length.
     * @param keyLength The length of keys for strategies in this space.
     */
    public StrategySpace(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = ChoiceListPermutator.generateAll(keyLength);
    }

    /**
     * Returns an integer representing the key length of strategies in the
     * strategy space.
     * @return The key length associated with this strategy space.
     */
    public int getKeyLength() {
        return keyLength;
    }

    /**
     * Sets the key length associated with this StrategySpace to the supplied
     * value.
     * @param keyLength The required key length.
     */
    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = ChoiceListPermutator.generateAll(keyLength);
    }

    /**
     * Returns a strategy at random from the space. Each strategy has an equal
     * chance of being chosen.
     * @return A strategy at random from the space.
     */
    public Strategy generateStrategy() {
        // declare required variables
        HashMap<List<Choice>, Choice> mappings =
            new HashMap<List<Choice>, Choice>();

        // for each key of the specified length, add an entry to the map with
        // a random outcome.
        for(List<Choice> key : requiredKeys) {
            mappings.put(
                key,
                choiceSet[randomNumberGenerator.nextInt()]
            );
        }

        // create a Strategy object using the generated mappings.
        Strategy strategy = new Strategy(mappings);

        // return the strategy.
        return strategy;
    }
}
