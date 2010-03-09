package msci.mg;

import msci.mg.agents.Agent;
import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import msci.mg.agents.BasicNetworkedAgent;

/**
 * The {@code Neighbourhood} class represents the local neighbourhood of a
 * supplied agent (called the root agent) in a supplied social network.
 *
 * @author Toby Clemson
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

    private Graph<Agent, Friendship> socialNetwork;
    private Agent rootAgent;

    /**
     * Sets the social network to the supplied graph and the root agent to the
     * supplied agent. If the root agent is not present in the supplied social
     * network, an {@code IllegalArgumentException} is thrown.
     *
     * @param socialNetwork The social network in which the agent resides.
     * @param rootAgent The agent in the social network on which to center this
     * neighbourhood.
     */
    public Neighbourhood(
        Graph<Agent, Friendship> socialNetwork,
        Agent rootAgent
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

    public Graph<Agent, Friendship> getSocialNetwork() {
        return socialNetwork;
    }

    public Agent getRootAgent() {
        return rootAgent;
    }

    public Collection<Agent> getFriends() {
        return socialNetwork.getNeighbors(rootAgent);
    }

    /**
     * Returns the agent out of the root agent and all of its friends that has
     * predicted the correct minority choice the most times. If more than one
     * agent has the same correct prediction count, one of them is returned at
     * random.
     */
    public Agent getMostSuccessfulPredictor() {
        // calculate a list of all predictors with the highest correct
        // prediction count
        List<Agent> mostSuccessfulPredictors =
            new ArrayList<Agent>();

        BasicNetworkedAgent networkedRootAgent;

        if(!(rootAgent instanceof BasicNetworkedAgent)) {
            return rootAgent;
        } else {
            networkedRootAgent = (BasicNetworkedAgent) rootAgent;
        }

        mostSuccessfulPredictors.add(rootAgent);
        int currentHighestCorrectPredictionCount =
            networkedRootAgent.getCorrectPredictionCount();

        for(Agent potentialPredictor : getFriends()) {
            if(!(potentialPredictor instanceof BasicNetworkedAgent)) {
                continue;
            }

            BasicNetworkedAgent networkedPotentialPredictor =
                (BasicNetworkedAgent) potentialPredictor;

            if(
                networkedPotentialPredictor.getCorrectPredictionCount() >
                currentHighestCorrectPredictionCount
            ) {
                mostSuccessfulPredictors.clear();
                mostSuccessfulPredictors.add(potentialPredictor);
                currentHighestCorrectPredictionCount =
                    networkedPotentialPredictor.getCorrectPredictionCount();
            } else if(
                networkedPotentialPredictor.getCorrectPredictionCount() ==
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
     * @param arraySize The number of elements in the collection for which the
     * index is required.
     */
    private int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
    }
}