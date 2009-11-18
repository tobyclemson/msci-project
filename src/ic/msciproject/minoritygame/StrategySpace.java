package ic.msciproject.minoritygame;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Random;

public class StrategySpace {
    private int keyLength;
    private Set<String> requiredKeys;

    public StrategySpace(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = StringPermutator.generateAll(keyLength);
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = StringPermutator.generateAll(keyLength);
    }

    public Strategy generateStrategy() {
        HashMap<String, String> mappings = new HashMap<String, String>();
        Iterator<String> keyIterator = requiredKeys.iterator();
        Random randomNumberGenerator = new Random();

        while(keyIterator.hasNext()) {
            mappings.put(
                keyIterator.next(),
                String.valueOf(randomNumberGenerator.nextInt(2))
            );
        }

        Strategy strategy = new Strategy(mappings);
        return strategy;
    }
}
