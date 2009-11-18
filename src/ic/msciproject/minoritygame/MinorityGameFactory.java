package ic.msciproject.minoritygame;

import java.util.ArrayList;
import java.util.Properties;
import java.util.HashSet;

public class MinorityGameFactory {
    public static AbstractMinorityGame construct(Properties properties) {
        // check the supplied properties are satisfactory, if not, throw an
        // IllegalArgumentException.
        assertPropertiesAreValid(properties);

        // build an empty minority game instance.
        AbstractMinorityGame minorityGame = new AbstractMinorityGame();
        String type = properties.getProperty("type");

        if(type.equals("standard")){
            minorityGame = new StandardMinorityGame();
        } else if(type.equals("evolutionary")){
            minorityGame = new EvolutionaryMinorityGame();
        }

        // assign a HistoryString instance of the required length to the
        // minority game instance.
        Integer historyStringLength = Integer.parseInt(
            properties.getProperty("history-string-length")
        );
        HistoryString historyString = new HistoryString(historyStringLength);

        minorityGame.setHistoryString(historyString);

        // build an array of agents and assign it to the minority game instance.
        ArrayList<AbstractAgent> agents = new ArrayList<AbstractAgent>();
        int numberOfAgents = Integer.parseInt(
            properties.getProperty("number-of-agents")
        );
        int numberOfStrategiesPerAgent = Integer.parseInt(
            properties.getProperty("number-of-strategies-per-agent")
        );
        String agentType = properties.getProperty("agent-type");

        for(int i = 0; i < numberOfAgents; i++){
            // add the required number of strategies to each agent
            AbstractAgent agent = new AbstractAgent();

            if(agentType.equals("basic")){
                agent = new BasicAgent();
            } else if(agentType.equals("learning")){
                agent = new LearningAgent();
            }

            StrategySpace strategySpace = new StrategySpace(
                historyStringLength
            );
            StrategyCollection strategyCollection = new StrategyCollection();

            for(int j = 0; j < numberOfStrategiesPerAgent; j++) {
                strategyCollection.add(strategySpace.generateStrategy());
            }

            agent.setStrategies(strategyCollection);

            // add the agents to the collection
            agents.add(agent);
        }

        minorityGame.setAgents(agents);

        return minorityGame;
    }

    private static void assertPropertiesAreValid(Properties properties){
        assertPropertyExists(properties, "type");
        assertPropertyExists(properties, "history-string-length");
        assertPropertyExists(properties, "number-of-agents");
        assertPropertyExists(properties, "agent-type");
        assertPropertyExists(properties, "number-of-strategies-per-agent");

        HashSet<String> acceptedTypes = new HashSet<String>();
        acceptedTypes.add("standard");
        acceptedTypes.add("evolutionary");

        assertPropertyInSet(properties, "type", acceptedTypes);

        assertPropertyIsNumeric(properties, "history-string-length");
        assertPropertyIsNumeric(properties, "number-of-agents");

        HashSet<String> acceptedAgentTypes = new HashSet<String>();
        acceptedAgentTypes.add("basic");
        acceptedAgentTypes.add("learning");

        assertPropertyInSet(properties, "agent-type", acceptedAgentTypes);
        assertPropertyIsNumeric(properties, "number-of-strategies-per-agent");
    }

    private static void assertPropertyExists(
        Properties properties, String property
    ){
        if(!properties.containsKey(property)){
            throw new IllegalArgumentException(
                "Expected properties object to contain a '" + property +
                "' property."
            );
        }
    }

    private static void assertPropertyInSet(
        Properties properties, String property, HashSet<String> set
    ){
        if(!set.contains(properties.getProperty(property))){
            throw new IllegalArgumentException(
                "Expected properties object to contain a recognized value " +
                "for the '" + property + "' property. 'type' can be one of " +
                set.toString()
            );
        }
    }

    private static void assertPropertyIsNumeric(
        Properties properties, String property
    ){
        String value = properties.getProperty(property);
        if(!value.matches("\\d*")){
            throw new IllegalArgumentException(
                "Expected properties object to contain a numeric value for " +
                "the '" + property +
                "' property."
            );
        }
    }
}
