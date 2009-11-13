package ic.msciproject.minoritygame;

import java.util.Properties;
import java.util.HashSet;

public class MinorityGame {
    public static AbstractMinorityGame construct(Properties properties) {
        if(!properties.containsKey("type")) {
            throw new IllegalArgumentException(
                "Expected properties object to contain a 'type' property."
            );
        }

        String type = properties.getProperty("type");
        HashSet<String> acceptedTypes = new HashSet<String>();
        
        acceptedTypes.add("standard");
        acceptedTypes.add("evolutionary");

        if(!acceptedTypes.contains(type)){
            throw new IllegalArgumentException(
                "Expected properties object to contain a recognized value " +
                "for the 'type' property. 'type' can be one of " +
                acceptedTypes.toString()
            );
        }

        AbstractMinorityGame minorityGame = new AbstractMinorityGame();

        if(type.equals("standard")){
            minorityGame = new StandardMinorityGame(properties);
        } else if(type.equals("evolutionary")){
            minorityGame = new EvolutionaryMinorityGame(properties);
        }

        return minorityGame;
    }
}
