package ic.msciproject.minoritygame;

import java.util.Properties;
import java.util.HashSet;

/**
 * The MinorityGameFactory class constructs a minority game with the required
 * parameters as specified in the Properties object passed to the static
 * construct method.
 * @author tobyclemson
 */
public class MinorityGameFactory {
    /**
     * Constructs a minority game with the specified parameters.
     * <p>
     * The supplied Properties object must contain the following properties:
     *
     * <ul>
     *  <li>"type": the type of minority game to construct, currently can be
     *      one of "standard" or "evolutionary".
     *  <li>"agent-memory-size": the number of past minority choices each agent
     *      can remember as a string, e.g., "5".
     *  <li>"number-of-agents": the number of agents required in the game as a
     *      string, e.g., "10000".
     *  <li>"agent-type": the type of agents required in the game, currently
     *      can be one of "basic" or "learning".
     *  <li>"number-of-strategies-per-agent": the number of strategies required
     *      for each agent in the game as a string, e.g., "2".
     * </ul>
     * <p>
     * If the Properties object passed to the method does not contain any one
     * of these properties or the value supplied for any of these properties is
     * not valid, then an IllegalArgumentException is thrown.
     *
     * @param properties A Properties object containing the parameters of the
     * minority game to construct.
     * @return An instance of a subclass of AbstractMinorityGame initialised
     * as requested.
     * @throws IllegalArgumentException
     */
    public static AbstractMinorityGame construct(Properties properties) {
        // check the supplied properties are satisfactory, if not, throw an
        // IllegalArgumentException.
        assertPropertiesAreValid(properties);

        // declare required variables
        Integer agentMemorySize,
                numberOfAgents,
                numberOfStrategiesPerAgent;
        String  agentType,
                type;
        ChoiceHistory choiceHistory;
        AgentCollection agents;
        StrategySpace strategySpace;
        StrategyCollection strategyCollection;
        AbstractMinorityGame minorityGame = null;
        AbstractAgent agent = null;

        // initialise a ChoiceHistory instance with an initial length equal to
        // the .
        agentMemorySize = Integer.parseInt(
            properties.getProperty("agent-memory-size")
        );
        choiceHistory = new ChoiceHistory(agentMemorySize);

        // build a collection of agents of the specified type with the required
        // number of strategies.
        agents = new AgentCollection();
        numberOfAgents = Integer.parseInt(
            properties.getProperty("number-of-agents")
        );
        numberOfStrategiesPerAgent = Integer.parseInt(
            properties.getProperty("number-of-strategies-per-agent")
        );
        agentType = properties.getProperty("agent-type");

        for(int i = 0; i < numberOfAgents; i++){
            // add the required number of strategies to each agent
            strategySpace = new StrategySpace(agentMemorySize);
            strategyCollection = new StrategyCollection();

            for(int j = 0; j < numberOfStrategiesPerAgent; j++) {
                strategyCollection.add(strategySpace.generateStrategy());
            }

            if(agentType.equals("basic")){
                agent = new BasicAgent(strategyCollection);
            } else if(agentType.equals("learning")){
                agent = new LearningAgent(strategyCollection);
            }

            // add the agents to the collection
            agents.add(agent);
        }

        // build an minority game instance passing the agents and history
        // string.
        type = properties.getProperty("type");

        if(type.equals("standard")){
            minorityGame = new StandardMinorityGame(
                agents, choiceHistory, agentMemorySize
            );
        } else if(type.equals("evolutionary")){
            minorityGame = new EvolutionaryMinorityGame(
                agents, choiceHistory, agentMemorySize
            );
        }

        return minorityGame;
    }

    /**
     * Checks that the contents of the supplied Properties object are valid,
     * if not then an IllegalArgumentException is thrown.
     * @param properties The Properties object to check.
     * @throws IllegalArgumentException
     */
    private static void assertPropertiesAreValid(Properties properties){
        assertPropertyExists(properties, "type");
        assertPropertyExists(properties, "agent-memory-size");
        assertPropertyExists(properties, "number-of-agents");
        assertPropertyExists(properties, "agent-type");
        assertPropertyExists(properties, "number-of-strategies-per-agent");

        HashSet<String> acceptedTypes = new HashSet<String>();
        acceptedTypes.add("standard");
        acceptedTypes.add("evolutionary");

        assertPropertyInSet(properties, "type", acceptedTypes);

        assertPropertyIsNumeric(properties, "agent-memory-size");
        assertPropertyIsNumeric(properties, "number-of-agents");

        HashSet<String> acceptedAgentTypes = new HashSet<String>();
        acceptedAgentTypes.add("basic");
        acceptedAgentTypes.add("learning");

        assertPropertyInSet(properties, "agent-type", acceptedAgentTypes);
        assertPropertyIsNumeric(properties, "number-of-strategies-per-agent");
    }

    /**
     * Checks that the supplied Properties objects contains the property
     * specified, throwing an IllegalArgumentException if it doesn't.
     * @param properties The Properties object to check.
     * @param property The name of the property to check for.
     * @throws IllegalArgumentException
     */
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

    /**
     * Checks that the supplied Properties object has a value for the specified
     * propety that is in the specified set.
     * @param properties The Properties object to check.
     * @param property The name of the property of which to check the value.
     * @param set The set of allowed values for the specified property.
     * @throws IllegalArgumentException
     */
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

    /**
     * Checks that the supplied Properties object has a value containing a
     * string representing a number for the specified property.
     * @param properties The Properties object to check.
     * @param property The name of the property of which to check the value.
     */
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
