package ic.msciproject.minoritygame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.AbstractMap;
import java.util.Map;

public class Strategy extends AbstractMap{
    private HashMap<String, String> mappings;
    private HashSet<String> fullKeySet;
    private int keyLength;

    public Strategy(Map<String, String> mappings) {
        Set<String> keySet = mappings.keySet();
        Iterator<String> keyIterator = keySet.iterator();

        if(mappings.isEmpty()) {
            throw new IllegalArgumentException();
        }

        String currentKey = "";
        int currentKeyLength = 0;
        int previousKeyLength = 0;
        Set<String> foundKeySet = new HashSet<String>();

        do {
            currentKey = keyIterator.next();
            currentKeyLength = currentKey.length();
            foundKeySet.add(currentKey);

            if(previousKeyLength == 0) {
                previousKeyLength = currentKeyLength;
            } else if(currentKeyLength != previousKeyLength) {
                throw new IllegalArgumentException();
            }
        } while(keyIterator.hasNext());

        keyLength = currentKeyLength;
        fullKeySet = StringPermutator.generateAll(keyLength);

        if(!fullKeySet.equals(foundKeySet)) {
            throw new IllegalArgumentException();
        }

        this.mappings = (HashMap<String,String>) mappings;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return mappings.entrySet();
    }

    public String get(String key) {
        if(!fullKeySet.contains(key)) {
            throw new IllegalArgumentException();
        }
        return (String) super.get(key);
    }
}
