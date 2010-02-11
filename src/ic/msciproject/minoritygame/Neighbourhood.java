package ic.msciproject.minoritygame;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.Port;

/**
 * The Neighbourhood class contains a graph of agents and friendships as well
 * as a reference to a root agent that must be present in the graph of agents,
 * extending the graph with behaviour related to the root agent's neighbourhood.
 * @author tobyclemson
 */
public class Neighbourhood {

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
         * integers (except the endpoints) to be used as a seed
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
     * The social network in which the root agent associated with this
     * neighbourhood resides. The neighbourhood represents a sub graph of this
     * social network containing only the agents connected to the root agent by
     * and edge. This is returned by the {@link #getSocialNetwork} method.
     */
    private Graph<AbstractAgent, Friendship> socialNetwork;

    /**
     * The root agent associated with this neighbourhood, i.e., the agent on
     * which the neighbourhood is centred. This is returned by the {@link
     * #getRootAgent} method.
     */
    private AbstractAgent rootAgent;

    /**
     * Constructs a Neighbourhood instance representing a sub graph of the
     * supplied social network centered on the root agent. If the root agent
     * is not present in the social network, an IllegalArgumentError is thrown.
     * @param socialNetwork The social network that this neighbourhood is a
     * sub network of.
     * @param rootAgent The agent in the social network to center this
     * neighbourhood on.
     */
    public Neighbourhood(
        Graph<AbstractAgent, Friendship> socialNetwork,
        AbstractAgent rootAgent
    ) {
        if(!socialNetwork.containsVertex(rootAgent)){
            throw new IllegalArgumentException(
                "The supplied social network does not contain the supplied " +
                "root agent."
            );
        }
        this.socialNetwork = socialNetwork;
        this.rootAgent = rootAgent;
    }

    /**
     * Returns the social network of agents and friendships associated with
     * this neighbourhood.
     * @return The social network associated with this nieghbourhood.
     */
    public Graph<AbstractAgent, Friendship> getSocialNetwork() {
        return socialNetwork;
    }

    /**
     * Returns the root agent associated with this neighbourhood, i.e., the
     * agent that this neighbourhood is centered on in the supplied social
     * network.
     * @return This nieghbourhood's root agent.
     */
    public AbstractAgent getRootAgent() {
        return rootAgent;
    }

    /**
     * Returns a list of all of the friends of the root agent that exist in the
     * supplied social network sorted by identification number. In the default
     * implementation, a friend is any agent connected by an friendship to the
     * root agent in the social network.
     * @return The root agent's friends.
     */
    public List<AbstractAgent> getFriends() {
        List<AbstractAgent> friends = new ArrayList<AbstractAgent>(
            socialNetwork.getNeighbors(rootAgent)
        );

        Collections.sort(friends);

        return friends;
    }

    /**
     * Returns the agent out of the root agent and all of its friends that has
     * predicted the correct minority choice the most times. If more than one
     * agent has the same correct prediction count, one of them is returned at
     * random.
     * @return The agent in this neighbourhood that has predicted the minority
     * choice correctly most often.
     */
    public AbstractAgent getMostSuccessfulPredictor() {
        // calculate a list of all predictors with the highest correct
        // prediction count
        List<AbstractAgent> mostSuccessfulPredictors =
            new ArrayList<AbstractAgent>();

        mostSuccessfulPredictors.add(rootAgent);
        int currentHighestCorrectPredictionCount =
            rootAgent.getCorrectPredictionCount();

        for(AbstractAgent potentialPredictor : getFriends()) {
            if(
                potentialPredictor.getCorrectPredictionCount() >
                currentHighestCorrectPredictionCount
            ) {
                mostSuccessfulPredictors.clear();
                mostSuccessfulPredictors.add(potentialPredictor);
                currentHighestCorrectPredictionCount =
                    potentialPredictor.getCorrectPredictionCount();
            } else if(
                potentialPredictor.getCorrectPredictionCount() ==
                currentHighestCorrectPredictionCount
            ) {
                mostSuccessfulPredictors.add(potentialPredictor);
            }
        }

        // return one of those most successful predictors at random
        return mostSuccessfulPredictors.get(
            getRandomIndex(mostSuccessfulPredictors.size())
        );
    }

    /**
     * Returns a random integer in the range [0,arraySize[ representing a
     * random array index.
     * @param arraySize The number of elements in the array for which the index
     * is required.
     * @return An integer in the range [0,arraySize[
     */
    private int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
    }

}
