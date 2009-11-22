package ic.msciproject.minoritygame;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Random;

/**
 * Instances of the StrategySpace class represents the space of all possible
 * Strategy objects of a particular key length. Such instances can be used to
 * generate and return strategies at random from the space.
 * @author tobyclemson
 */
public class StrategySpace {

    /**
     * An integer holding the length of the keys in the strategies in this
     * strategy space. This is returned by the {@link #getKeyLength} method.
     */
    private int keyLength;

    /**
     * A Set of Strings representing the keys required for a strategy generated
     * by the StrategySpace to be valid.
     */
    private Set<String> requiredKeys;

    /**
     * Constructs a StrategySpace for keys of the specified length.
     * @param keyLength The length of keys for strategies in this space.
     */
    public StrategySpace(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = StringPermutator.generateAll(keyLength);
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
        this.requiredKeys = StringPermutator.generateAll(keyLength);
    }

    /**
     * Returns a strategy at random from the space. Each strategy has an equal
     * chance of being chosen.
     * @return A strategy at random from the space.
     */
    public Strategy generateStrategy() {
        // declare required variables
        HashMap<String, String> mappings;
        Iterator<String> keyIterator;
        Random randomNumberGenerator;

        // create an empty map to hold the history string to outcome
        // predictions.
        mappings = new HashMap<String, String>();
        
        // obtain an iterator over all possible keys for a strategy with the
        // specified key length.
        keyIterator = requiredKeys.iterator();
        
        // create a random number generator to generate outcomes at random.
        randomNumberGenerator = new Random();

        // for each key of the specified length, add an entry to the map with
        // a random outcome.
        while(keyIterator.hasNext()) {
            mappings.put(
                keyIterator.next(),
                String.valueOf(randomNumberGenerator.nextInt(2))
            );
        }

        // create a Strategy object using the generated mappings.
        Strategy strategy = new Strategy(mappings);

        // return the strategy.
        return strategy;
    }
}
