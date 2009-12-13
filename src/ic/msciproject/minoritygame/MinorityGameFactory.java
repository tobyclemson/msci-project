package ic.msciproject.minoritygame;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import java.util.Properties;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MinorityGameFactory class constructs a minority game with the required
 * parameters as specified in the Properties object passed to the static
 * construct method.
 * @author tobyclemson
 */
public class MinorityGameFactory {

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
     * Constructs a minority game with the specified parameters.
     * <p>
     * The supplied Properties object must contain the following properties:
     *
     * <ul>
     *  <li>"type": the type of minority game to construct, currently can only
     *      be "standard".
     *  <li>"agent-memory-size": the number of past minority choices each agent
     *      can remember as a string, e.g., "5".
     *  <li>"number-of-agents": the number of agents required in the game as a
     *      string, e.g., "10000".
     *  <li>"agent-type": the type of agents required in the game, currently
     *      can be one of "basic", "learning" or "random".
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
        int[] agentMemorySizes = null;
        int numberOfAgents,
            numberOfStrategiesPerAgent,
            maximumAgentMemorySize = 0,
            numberOfAgentMemorySizes = 0;
        String  agentType,
                type,
                requiredAgentMemorySize;
        ChoiceHistory choiceHistory;
        ChoiceMemory choiceMemory;
        List<AbstractAgent> agents;
        AgentManager agentManager;
        StrategySpace strategySpace;
        List<Strategy> strategies;
        StrategyManager strategyManager;
        AbstractMinorityGame minorityGame = null;
        AbstractAgent agent = null;

        // deduce the agent memory sizes required
        requiredAgentMemorySize = properties.getProperty("agent-memory-size");
        if(requiredAgentMemorySize.matches("\\d*")) {
            // if the agent-memory-size property is just a number, create a
            // single element array containing that number and set the
            // maximum agent memory size and number of agent memory sizes
            // variables accordingly.
            agentMemorySizes = new int[1];
            agentMemorySizes[0] = Integer.parseInt(
                properties.getProperty("agent-memory-size")
            );
            maximumAgentMemorySize = agentMemorySizes[0];
            numberOfAgentMemorySizes = 1;
        } else if(requiredAgentMemorySize.matches("\\d*\\.\\.\\d*")) {
            // if the agent-memory-size property is a range, create an array
            // of possible memory sizes, set the maximum agent memory size
            // variable to the upper bound of the range and set the number
            // of agent memory sizes variable to the number of elements in the
            // array.
            Pattern rangeBoundPattern = Pattern.compile("(\\d+)\\.\\.(\\d+)");
            Matcher rangeBoundMatcher = rangeBoundPattern.matcher(
                requiredAgentMemorySize
            );

            rangeBoundMatcher.find();

            int lowerBound = Integer.parseInt(rangeBoundMatcher.group(1));
            int upperBound = Integer.parseInt(rangeBoundMatcher.group(2));

            numberOfAgentMemorySizes = upperBound - lowerBound + 1;
            
            agentMemorySizes = new int[numberOfAgentMemorySizes];
            for(int i = 0; i < numberOfAgentMemorySizes; i++) {
                agentMemorySizes[i] = lowerBound + i;
            }

            maximumAgentMemorySize = upperBound;
        }

        // initialise a ChoiceHistory instance with an initial length equal to
        // the requested memory size.
        choiceHistory = new ChoiceHistory(maximumAgentMemorySize);

        // build a collection of agents of the specified type with the required
        // number of strategies and memories of the specified size.
        agents = new ArrayList<AbstractAgent>();
        numberOfAgents = Integer.parseInt(
            properties.getProperty("number-of-agents")
        );
        numberOfStrategiesPerAgent = Integer.parseInt(
            properties.getProperty("number-of-strategies-per-agent")
        );
        agentType = properties.getProperty("agent-type");

        for(int i = 0; i < numberOfAgents; i++){
            int memorySize;

            // if agents with different memory sizes are required, select one
            // at random from the array of possible memory sizes, otherwise use
            // the specified memory size, setting the memory size variable to
            // the selected memory size.
            if(numberOfAgentMemorySizes > 0) {
                memorySize = agentMemorySizes[
                    getRandomIndex(numberOfAgentMemorySizes)
                ];
            } else {
                memorySize = agentMemorySizes[0];
            }

            // add the required number of strategies to each agent
            strategySpace = new StrategySpace(memorySize);
            strategies = new ArrayList<Strategy>();

            for(int j = 0; j < numberOfStrategiesPerAgent; j++) {
                strategies.add(strategySpace.generateStrategy());
            }

            strategyManager = new StrategyManager(strategies);

            // create a memory of the specified size for the agent
            choiceMemory = new ChoiceMemory(choiceHistory, memorySize);

            // build the required agent type based on the agent type property
            if(agentType.equals("basic")) {
                agent = new BasicAgent(strategyManager, choiceMemory);
            } else if(agentType.equals("learning")) {
                agent = new LearningAgent(strategyManager, choiceMemory);
            } else if(agentType.equals("random")) {
                agent = new RandomAgent(strategyManager, choiceMemory);
            }

            // add the agents to the collection
            agents.add(agent);
        }

        // create an agent manager using the generated list of agents.
        agentManager = new AgentManager(agents);

        // build a minority game instance passing the agents and history
        // string.
        type = properties.getProperty("type");

        if(type.equals("standard")) {
            minorityGame = new StandardMinorityGame(
                agentManager, choiceHistory
            );
        }

        // return the minority game
        return minorityGame;
    }

    /**
     * Returns a random integer in the range [0,arraySize[ representing a
     * random array index.
     * @param arraySize The number of elements in the array for which the index
     * is required.
     * @return An integer in the range [0,arraySize[
     */
    private static int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
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

        assertPropertyIsNumericOrRange(properties, "agent-memory-size");
        assertPropertyIsNumeric(properties, "number-of-strategies-per-agent");

        assertPropertyIsOdd(properties, "number-of-agents");

        HashSet<String> acceptedTypes = new HashSet<String>();
        acceptedTypes.add("standard");

        assertPropertyInSet(properties, "type", acceptedTypes);

        HashSet<String> acceptedAgentTypes = new HashSet<String>();
        acceptedAgentTypes.add("basic");
        acceptedAgentTypes.add("learning");
        acceptedAgentTypes.add("random");

        assertPropertyInSet(properties, "agent-type", acceptedAgentTypes);
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
     * property that is in the specified set.
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
        if(!value.matches("\\d+")){
            throw new IllegalArgumentException(
                "Expected properties object to contain a numeric value for " +
                "the '" + property + "' property."
            );
        }
    }

    /**
     * Checks that the supplied Properties object has an value containing a
     * string representing an odd number for the specified property.
     * @param properties
     * @param property
     */
    private static void assertPropertyIsOdd(
        Properties properties,
        String property
    ) {
        assertPropertyIsNumeric(properties, property);
        int value = Integer.parseInt(properties.getProperty(property));
        if(value % 2 == 0) {
            throw new IllegalArgumentException(
                "Expected properties object to contain an odd number for " +
                "the '" + property + "' property."
            );
        }
    }

    private static void assertPropertyIsNumericOrRange(
        Properties properties,
        String property
    ) {
        boolean isNumeric = false,
                isRange = false;

        String value = properties.getProperty(property);

        if(value.matches("\\d+")) {
            isNumeric = true;
        } else if(value.matches("\\d+\\.\\.\\d+")) {
            int lowerBound, upperBound;

            Pattern rangeBoundPattern = Pattern.compile("(\\d+)\\.\\.(\\d+)");
            Matcher rangeBoundMatcher = rangeBoundPattern.matcher(value);

            rangeBoundMatcher.find();

            lowerBound = Integer.parseInt(rangeBoundMatcher.group(1));
            upperBound = Integer.parseInt(rangeBoundMatcher.group(2));

            if(lowerBound < 0 || upperBound < 0) {
                throw new IllegalArgumentException(
                    "Expected properties object to contain a valid range " +
                    "for the '" + property + "' property but one of the " +
                    "bounds was negative."
                );
            } else if(upperBound < lowerBound) {
                throw new IllegalArgumentException(
                    "Expected properties object to contain a valid range " +
                    "for the '" + property + "' property but the upper bound " +
                    "was smaller than the lower bound."
                );
            }

            isRange = true;
        }

        if(!(isNumeric || isRange)) {
            throw new IllegalArgumentException(
                "Expected properties object to contain a number or a range " +
                "for the '" + property + "' property."
            );
        }
    }
}
