package ic.msciproject.minoritygame;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import ic.msciproject.minoritygame.factories.AgentFactory;
import ic.msciproject.minoritygame.factories.BasicAgentFactory;
import ic.msciproject.minoritygame.factories.CompleteSocialNetworkFactory;
import ic.msciproject.minoritygame.factories.EmptySocialNetworkFactory;
import ic.msciproject.minoritygame.factories.FriendshipFactory;
import ic.msciproject.minoritygame.factories.LearningAgentFactory;
import ic.msciproject.minoritygame.factories.RandomAgentFactory;
import ic.msciproject.minoritygame.factories.RandomValueFactory;
import ic.msciproject.minoritygame.factories.SocialNetworkFactory;
import java.util.Properties;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.FactoryUtils;

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
     * @return An instance of a subclass of MinorityGame initialised
     * as requested.
     * @throws IllegalArgumentException
     */
    public static MinorityGame construct(Properties properties) {
        // check the supplied properties are satisfactory, if not, throw an
        // IllegalArgumentException.
        assertPropertiesAreValid(properties);

        // declare required variables
        int numberOfAgents,
            numberOfStrategiesPerAgent,
            maximumAgentMemorySize = 0;
        String  agentType,
                networkType,
                requiredAgentMemorySize;

        ChoiceHistory choiceHistory;
        Community community;
        MinorityGame minorityGame = null;

        AgentFactory agentFactory = null;
        FriendshipFactory friendshipFactory = null;
        SocialNetworkFactory socialNetworkFactory = null;
        
        Factory<Integer> memoryCapacityFactory = null;
        Factory<Integer> numberOfStrategiesFactory = null;

        // deduce the agent memory sizes required
        requiredAgentMemorySize = properties.getProperty("agent-memory-size");
        if(requiredAgentMemorySize.matches("\\d*")) {
            // parse the required memory capacity
            int memoryCapacity = Integer.parseInt(
                properties.getProperty("agent-memory-size")
            );

            // fetch a factory that returns the supplied value always
            memoryCapacityFactory = FactoryUtils.constantFactory(
                memoryCapacity
            );

            maximumAgentMemorySize = memoryCapacity;
        } else if(requiredAgentMemorySize.matches("\\d*\\.\\.\\d*")) {
            // create a RandomValueFactory to return memory capacities randomly
            // in the specified range.
            Pattern rangeBoundPattern = Pattern.compile("(\\d+)\\.\\.(\\d+)");
            Matcher rangeBoundMatcher = rangeBoundPattern.matcher(
                requiredAgentMemorySize
            );

            rangeBoundMatcher.find();

            int lowerBound = Integer.parseInt(rangeBoundMatcher.group(1));
            int upperBound = Integer.parseInt(rangeBoundMatcher.group(2));

            memoryCapacityFactory = new RandomValueFactory(
                lowerBound, upperBound
            );

            maximumAgentMemorySize = upperBound;
        }

        // initialise a ChoiceHistory instance with an initial length equal to
        // the requested memory size.
        choiceHistory = new ChoiceHistory(maximumAgentMemorySize);

        // build a collection of agents of the specified type with the required
        // number of strategies and memories of the specified size.
        numberOfAgents = Integer.parseInt(
            properties.getProperty("number-of-agents")
        );

        numberOfStrategiesPerAgent = Integer.parseInt(
            properties.getProperty("number-of-strategies-per-agent")
        );
        numberOfStrategiesFactory = FactoryUtils.constantFactory(
            numberOfStrategiesPerAgent
        );

        agentType = properties.getProperty("agent-type");

        if(properties.containsKey("network-type")) {
            networkType = properties.getProperty("network-type");
        } else {
            networkType = "empty";
        }

        // build the correct agent factory based on the agent type property
        if(agentType.equals("basic")) {
            agentFactory = new BasicAgentFactory(
                memoryCapacityFactory, 
                numberOfStrategiesFactory,
                choiceHistory.asList()
            );
        } else if(agentType.equals("learning")) {
            agentFactory = new LearningAgentFactory(
                memoryCapacityFactory, 
                numberOfStrategiesFactory,
                choiceHistory.asList()
            );
        } else if(agentType.equals("random")) {
            agentFactory = new RandomAgentFactory();
        }

        // build a friendship factory
        friendshipFactory = new FriendshipFactory();

        // build the correct social network factory based on the network type
        // property
        if(networkType.equals("complete")) {
            socialNetworkFactory = new CompleteSocialNetworkFactory(
                agentFactory, friendshipFactory, numberOfAgents
            );
        } else {
            socialNetworkFactory = new EmptySocialNetworkFactory(
                agentFactory, friendshipFactory, numberOfAgents
            );
        }

        // construct the social network
        Graph<AbstractAgent, Friendship> socialNetwork =
            socialNetworkFactory.create();

        // update each agent's social network to reflect the friendships in the
        // complete social network
        for(AbstractAgent agent : socialNetwork.getVertices()) {
            // create an empty graph representing the agents network
            Graph<AbstractAgent, Friendship> localSocialNetwork =
                new SparseGraph<AbstractAgent, Friendship>();

            // add the agent to the local social network
            localSocialNetwork.addVertex(agent);

            // add all agents and friendships connected to the agent to the
            // graph
            for(AbstractAgent friend : socialNetwork.getNeighbors(agent)) {
                localSocialNetwork.addVertex(friend);
                localSocialNetwork.addEdge(
                    socialNetwork.findEdge(agent, friend), agent, friend
                );
            }

            // set the graph as the agent's social network
            agent.setSocialNetwork(localSocialNetwork);
        }

        // create a Community instance using the generated list of agents.
        community = new Community(socialNetwork);

        // build a minority game instance passing the agents and history
        // string.
        minorityGame = new MinorityGame(
            community, choiceHistory
        );

        // return the minority game
        return minorityGame;
    }

    /**
     * Checks that the contents of the supplied Properties object are valid,
     * if not then an IllegalArgumentException is thrown.
     * @param properties The Properties object to check.
     * @throws IllegalArgumentException
     */
    private static void assertPropertiesAreValid(Properties properties){
        assertPropertyExists(properties, "agent-memory-size");
        assertPropertyExists(properties, "number-of-agents");
        assertPropertyExists(properties, "agent-type");
        assertPropertyExists(properties, "number-of-strategies-per-agent");

        assertPropertyIsNumericOrRange(properties, "agent-memory-size");
        assertPropertyIsNumeric(properties, "number-of-strategies-per-agent");

        assertPropertyIsOdd(properties, "number-of-agents");

        HashSet<String> acceptedAgentTypes = new HashSet<String>();
        acceptedAgentTypes.add("basic");
        acceptedAgentTypes.add("learning");
        acceptedAgentTypes.add("random");

        assertPropertyInSet(properties, "agent-type", acceptedAgentTypes);

        HashSet<String> acceptedNetworkTypes = new HashSet<String>();
        acceptedNetworkTypes.add("empty");
        acceptedNetworkTypes.add("complete");
        acceptedNetworkTypes.add("random");

        if(properties.containsKey("network-type")) {
            assertPropertyInSet(
                properties, "network-type", acceptedNetworkTypes
            );
        }
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
     * @param properties The Properties object to check.
     * @param property The name of the property of which to check the value.
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

    /**
     * Checks that the supplied Properties object has a value containing a
     * string representing either a number or a range for the specified
     * property.
     * @param properties The Properties object to check.
     * @param property The name of the property of which to check the value.
     */
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
