package ic.msciproject.minoritygame;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.ArrayList;

public class StrategyCollection extends AbstractCollection {
    
    private ArrayList<Strategy> storage;

    public StrategyCollection() {
        super();
        storage = new ArrayList<Strategy>();
    }

    public StrategyCollection(StrategyCollection otherStrategyCollection) {
        ArrayList<Strategy> other_storage = otherStrategyCollection.storage;
        storage = new ArrayList<Strategy>();
        Iterator<Strategy> iterator = other_storage.iterator();
        while(iterator.hasNext()){
            add(iterator.next());
        }
    }

    public Iterator iterator() {
        return storage.iterator();
    }

    public int size() {
        return storage.size();
    }

    public boolean add(Strategy strategy) {
        storage.add(strategy);
        return true;
    }
    
}
