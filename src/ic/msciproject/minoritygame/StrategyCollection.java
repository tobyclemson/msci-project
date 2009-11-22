package ic.msciproject.minoritygame;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * The StrategyCollection class holds a collection of Strategies extending the
 * collection with strategy related functionality.
 * @author tobyclemson
 */
public class StrategyCollection extends AbstractCollection {

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
    public Iterator iterator() {
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
    public boolean add(Strategy strategy) {
        return storage.add(strategy);
    }   
}
