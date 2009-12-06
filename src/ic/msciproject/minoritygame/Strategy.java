package ic.msciproject.minoritygame;

import java.util.AbstractMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * The Strategy class represents a strategy employed by an agent in predicting
 * the next outcome in the minority game. A Strategy instance is initialised
 * with a map of choice permutations to predicted outcomes and when an agent has
 * to choose an outcome, the choice history is used to retrieve the predicted
 * outcome for that choice permurarion from the Strategy.
 * @author tobyclemson
 */
public class Strategy extends AbstractMap<List<Choice>,Choice>{

    /**
     * A Map of List instances containing Choice objects to Choices. The
     * entries in the map represent predictions of the next minority choice 
     * for all possible permutations of choice histories of the length
     * associated with this strategy.
     */
    private Map<List<Choice>, Choice> mappings;

    /**
     * An integer representing the key length in the mapping of the
     * Strategy. This is returned by the {@link #getKeyLength} method.
     */
    private int keyLength;

    /**
     * An integer representing the score of the Strategy. The score of a
     * Strategy is incremented if in a given turn it would have resulted in
     * a correct prediction for the minority choice. The score is incremented
     * using the {@link #incrementScore} method and returned by the
     * {@link #getScore} method.
     */
    private int score = 0;

    /**
     * A Set containing all of the possible permutations of choices accepted
     * as keys by this strategy.
     */
    private Set<List<Choice>> acceptedKeys;

    /**
     * Constructs a Strategy instance using the supplied history string to
     * outcome mappings.
     * <p>
     * The supplied mappings must all have the same key length and must
     * represent every possible mapping for that key lengthIf this is not true,
     * an IllegalArgumentException is thrown.
     * 
     * @param mappings A map of all possible input keys of the required length
     * to predictions for the minority choice given that input key.
     */
    public Strategy(Map<List<Choice>, Choice> mappings) {
        // throw an IllegalArgumentException if the supplied map contains no
        // entries
        if(mappings.isEmpty()) {
            throw new IllegalArgumentException("Mappings is empty");
        }

        // declare required variables
        Set<List<Choice>> keySet;
        Iterator<List<Choice>> keyIterator;
        List<Choice> currentKey;

        EnumSet<Choice> validValueSet;
        Iterator<Choice> valueIterator;
        Collection<Choice> valueCollection;
        Choice currentValue;

        int previousKeyLength, currentKeyLength;

        // get an iterator for the set of keys contained in the supplied
        // mapping
        keySet = mappings.keySet();
        keyIterator = keySet.iterator();

        // initialise variables
        currentKey = null;
        currentKeyLength = 0;
        previousKeyLength = 0;

        // check that all keys in the mapping are the same length, throwing
        // an IllegalArgumentException if any differ.
        do {
            currentKey = keyIterator.next();
            currentKeyLength = currentKey.size();

            if(previousKeyLength == 0) {
                previousKeyLength = currentKeyLength;
            } else if(currentKeyLength != previousKeyLength) {
                throw new IllegalArgumentException("Different key lengths");
            }
        } while(keyIterator.hasNext());

        // set the key length to that found in the supplied mappings
        keyLength = currentKeyLength;

        // if the supplied mapping does not contain all of the required keys
        // throw an IllegalArgumentException.
        acceptedKeys = ChoiceListPermutator.generateAll(keyLength);

        if(!keySet.equals(acceptedKeys)) {
            throw new IllegalArgumentException("Incorrect keys");
        }

        // if all is well, set the mappings to the supplied mappings object.
        this.mappings = mappings;
    }

    /**
     * Returns the length of the keys contained in the mappings in this
     * strategy.
     * @return An integer representing the key length.
     */
    public int getKeyLength() {
        return keyLength;
    }

    /**
     * Returns a Set view of the mappings contained in the strategy.
     *
     * @return The mappings representing this strategy.
     */
    public Set<Map.Entry<List<Choice>, Choice>> entrySet() {
        return mappings.entrySet();
    }

    public Map<List<Choice>, Choice> getMap() {
        return mappings;
    }

    /**
     * Returns the outcome corresponding to the supplied history
     * string predicted by the Strategy.
     * @param key A fixed size list of past minority choices (most recent has
     * highest index) with the size corresponding to the length of keys in the
     * strategy.
     * @return The predicted outcome as a Choice enumeration.
     */
    @Override
    public Choice get(Object key) {
        if(!acceptedKeys.contains((List<Choice>) key)) {
            throw new IllegalArgumentException();
        }
        return mappings.get((List<Choice>) key);
    }

    /**
     * Increments the Strategy's score by 1.
     */
    public void incrementScore() {
        score += 1;
    }

    /**
     * Returns the score of the Strategy. The score represents the number of
     * steps in the game so far for which the strategy has made correct
     * predictions.
     * @return The score of the strategy.
     */
    public int getScore() {
        return score;
    }
}
