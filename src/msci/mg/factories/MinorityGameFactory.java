package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.*;
import msci.mg.agents.Agent;
import msci.mg.agents.BasicNetworkedAgent;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.FactoryUtils;

import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinorityGameFactory {
    public static MinorityGame construct(Properties properties) {
        assertPropertiesAreValid(properties);

        int numberOfAgents,
                numberOfStrategiesPerAgent,
                maximumAgentMemorySize = 0,
                averageNumberOfFriends = 0,
                numberOfFriendsInEachDirection = 0;
        double linkProbability = 0.0;
        String agentType,
                networkType,
                requiredAgentMemorySize;

        ChoiceHistory choiceHistory;
        Community community;
        MinorityGame minorityGame;

        AgentFactory agentFactory = null;
        FriendshipFactory friendshipFactory;
        SocialNetworkFactory socialNetworkFactory;

        Factory<Integer> memoryCapacityFactory = null;
        Factory<Integer> numberOfStrategiesFactory;

        requiredAgentMemorySize = properties.getProperty("agent-memory-size");
        if (requiredAgentMemorySize.matches("\\d*")) {
            int memoryCapacity = Integer.parseInt(
                    properties.getProperty("agent-memory-size")
            );

            memoryCapacityFactory = FactoryUtils.constantFactory(
                    memoryCapacity
            );

            maximumAgentMemorySize = memoryCapacity;
        } else if (requiredAgentMemorySize.matches("\\d*\\.\\.\\d*")) {
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

        choiceHistory = new ChoiceHistory(maximumAgentMemorySize);

        numberOfAgents = Integer.parseInt(properties.getProperty("number-of-agents"));
        numberOfStrategiesPerAgent = Integer.parseInt(properties.getProperty("number-of-strategies-per-agent"));
        numberOfStrategiesFactory = FactoryUtils.constantFactory(numberOfStrategiesPerAgent);

        agentType = properties.getProperty("agent-type");

        if (properties.containsKey("network-type")) {
            networkType = properties.getProperty("network-type");
        } else {
            networkType = "empty";
        }

        if (properties.containsKey("link-probability")) {
            linkProbability = Double.valueOf(
                    properties.getProperty("link-probability")
            );
        }

        if (properties.containsKey("average-number-of-friends")) {
            averageNumberOfFriends = Integer.parseInt(
                    properties.getProperty("average-number-of-friends")
            );
        }

        if (properties.containsKey("number-of-friends-in-each-direction")) {
            numberOfFriendsInEachDirection = Integer.parseInt(
                    properties.getProperty("number-of-friends-in-each-direction")
            );
        }

        if (agentType.equals("basic")) {
            agentFactory = new BasicAgentFactory(
                    memoryCapacityFactory,
                    numberOfStrategiesFactory,
                    choiceHistory.asList());
        } else if (agentType.equals("learning")) {
            agentFactory = new LearningAgentFactory(
                    memoryCapacityFactory,
                    numberOfStrategiesFactory,
                    choiceHistory.asList());
        } else if (agentType.equals("networked")) {
            agentFactory = new NetworkedAgentFactory(
                    memoryCapacityFactory,
                    numberOfStrategiesFactory,
                    choiceHistory.asList());
        } else if (agentType.equals("random")) {
            agentFactory = new RandomAgentFactory();
        }

        friendshipFactory = new FriendshipFactory();

        if (networkType.equals("complete")) {
            socialNetworkFactory = new CompleteSocialNetworkFactory(agentFactory, friendshipFactory, numberOfAgents);
        } else if (networkType.equals("random")) {
            socialNetworkFactory = new RandomSocialNetworkFactory(
                    agentFactory, friendshipFactory, numberOfAgents, linkProbability);
        } else if (networkType.equals("scale-free")) {
            socialNetworkFactory = new ScaleFreeSocialNetworkFactory(
                    agentFactory, friendshipFactory, numberOfAgents, averageNumberOfFriends);
        } else if (networkType.equals("regular-ring")) {
            socialNetworkFactory = new RegularRingSocialNetworkFactory(
                    agentFactory, friendshipFactory, numberOfAgents, numberOfFriendsInEachDirection);
        } else {
            socialNetworkFactory = new EmptySocialNetworkFactory(
                    agentFactory, friendshipFactory, numberOfAgents);
        }

        Graph<Agent, Friendship> socialNetwork = socialNetworkFactory.create();

        for (Agent agent : socialNetwork.getVertices()) {
            if (!(agent instanceof BasicNetworkedAgent)) {
                continue;
            }

            BasicNetworkedAgent networkedAgent = (BasicNetworkedAgent) agent;

            Graph<Agent, Friendship> localSocialNetwork = new SparseGraph<Agent, Friendship>();
            localSocialNetwork.addVertex(agent);

            for (Agent friend : socialNetwork.getNeighbors(agent)) {
                localSocialNetwork.addVertex(friend);
                localSocialNetwork.addEdge(
                        socialNetwork.findEdge(agent, friend), agent, friend
                );
            }

            Neighbourhood neighbourhood = new Neighbourhood(
                    localSocialNetwork, agent
            );

            networkedAgent.setNeighbourhood(neighbourhood);
        }

        community = new Community(socialNetwork);
        minorityGame = new MinorityGame(community, choiceHistory);

        return minorityGame;
    }

    private static void assertPropertiesAreValid(Properties properties) {
        assertPropertyExists(properties, "agent-memory-size");
        assertPropertyExists(properties, "number-of-agents");
        assertPropertyExists(properties, "agent-type");
        assertPropertyExists(properties, "number-of-strategies-per-agent");

        assertPropertyIsIntegerOrRange(properties, "agent-memory-size");
        assertPropertyIsInteger(properties, "number-of-strategies-per-agent");

        assertPropertyIsOdd(properties, "number-of-agents");

        HashSet<String> acceptedAgentTypes = new HashSet<String>();
        acceptedAgentTypes.add("basic");
        acceptedAgentTypes.add("learning");
        acceptedAgentTypes.add("random");
        acceptedAgentTypes.add("networked");

        assertPropertyInSet(properties, "agent-type", acceptedAgentTypes);

        HashSet<String> acceptedNetworkTypes = new HashSet<String>();
        acceptedNetworkTypes.add("empty");
        acceptedNetworkTypes.add("complete");
        acceptedNetworkTypes.add("random");
        acceptedNetworkTypes.add("scale-free");
        acceptedNetworkTypes.add("regular-ring");

        if (properties.containsKey("network-type")) {
            assertPropertyInSet(
                    properties, "network-type", acceptedNetworkTypes
            );
            if (properties.getProperty("network-type").equals("random")) {
                assertPropertyExists(properties, "link-probability");
                assertPropertyIsProbability(properties, "link-probability");
            }
            if (properties.getProperty("network-type").equals("scale-free")) {
                assertPropertyExists(properties, "average-number-of-friends");
                assertPropertyIsInteger(
                        properties, "average-number-of-friends"
                );
            }
            if (properties.getProperty("network-type").equals("regular-ring")) {
                assertPropertyExists(
                        properties, "number-of-friends-in-each-direction"
                );
                assertPropertyIsInteger(
                        properties, "number-of-friends-in-each-direction"
                );
            }
        }
    }

    private static void assertPropertyExists(Properties properties, String property) {
        if (!properties.containsKey(property)) {
            throw new IllegalArgumentException(
                    "Expected properties object to contain a '" + property + "' property.");
        }
    }

    private static void assertPropertyInSet(Properties properties, String property, HashSet<String> set) {
        if (!set.contains(properties.getProperty(property))) {
            throw new IllegalArgumentException(String.format(
                    "Expected properties object to contain a recognized value for the '%s' property. " +
                    "'type' can be one of %s", property, set));
        }
    }

    private static void assertPropertyIsInteger(Properties properties, String property) {
        String value = properties.getProperty(property);
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException(String.format(
                    "Expected properties object to contain an integer value for the '%s' property.", property));
        }
    }

    private static void assertPropertyIsOdd(Properties properties, String property) {
        assertPropertyIsInteger(properties, property);
        int value = Integer.parseInt(properties.getProperty(property));
        if (value % 2 == 0) {
            throw new IllegalArgumentException(String.format(
                    "Expected properties object to contain an odd number for the '%s' property.", property));
        }
    }

    private static void assertPropertyIsIntegerOrRange(Properties properties, String property) {
        boolean isInteger = false,
                isRange = false;

        String value = properties.getProperty(property);

        if (value.matches("\\d+")) {
            isInteger = true;
        } else if (value.matches("\\d+\\.\\.\\d+")) {
            int lowerBound, upperBound;

            Pattern rangeBoundPattern = Pattern.compile("(\\d+)\\.\\.(\\d+)");
            Matcher rangeBoundMatcher = rangeBoundPattern.matcher(value);

            rangeBoundMatcher.find();

            lowerBound = Integer.parseInt(rangeBoundMatcher.group(1));
            upperBound = Integer.parseInt(rangeBoundMatcher.group(2));

            if (lowerBound < 0 || upperBound < 0) {
                throw new IllegalArgumentException(String.format(
                        "Expected properties object to contain a valid range for the " +
                        "'%s' property but one of the bounds was negative.", property));
            } else if (upperBound < lowerBound) {
                throw new IllegalArgumentException(String.format(
                        "Expected properties object to contain a valid range for the " +
                        "'%s' property but the upper bound was smaller than the lower bound.", property));
            }

            isRange = true;
        }

        if (!(isInteger || isRange)) {
            throw new IllegalArgumentException(String.format(
                    "Expected properties object to contain an integer or a range for the '%s' property.", property));
        }
    }

    private static void assertPropertyIsDecimal(Properties properties, String property) {
        String value = properties.getProperty(property);
        if (!value.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException(String.format(
                    "Expected properties object to contain an decimal value for the '%s' property.", property));
        }
    }

    private static void assertPropertyIsProbability(Properties properties, String property) {
        assertPropertyIsDecimal(properties, property);
        double value = Double.valueOf(properties.getProperty(property));
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException(String.format(
                    "Expected properties object to contain an probability for the '%s' property, " +
                    "i.e., a floating point value between 0 and 1.", property));
        }
    }
}
