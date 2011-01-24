package msci.mg;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import edu.uci.ics.jung.graph.Graph;
import msci.mg.agents.Agent;
import msci.mg.agents.BasicNetworkedAgent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Neighbourhood {
    private static AbstractDistribution randomNumberGenerator;

    static {
        randomNumberGenerator = new Uniform(
                new MersenneTwister((int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE)));
    }

    private Graph<Agent, Friendship> socialNetwork;
    private Agent rootAgent;

    public Neighbourhood(Graph<Agent, Friendship> socialNetwork, Agent rootAgent) {
        if (!socialNetwork.containsVertex(rootAgent)) {
            throw new IllegalArgumentException("The supplied social network does not contain the supplied root agent.");
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

    public Agent getMostSuccessfulPredictor() {
        List<Agent> mostSuccessfulPredictors = new ArrayList<Agent>();

        BasicNetworkedAgent networkedRootAgent;

        if (!(rootAgent instanceof BasicNetworkedAgent)) {
            return rootAgent;
        } else {
            networkedRootAgent = (BasicNetworkedAgent) rootAgent;
        }

        mostSuccessfulPredictors.add(rootAgent);
        int currentHighestCorrectPredictionCount = networkedRootAgent.getCorrectPredictionCount();

        for (Agent potentialPredictor : getFriends()) {
            if (!(potentialPredictor instanceof BasicNetworkedAgent)) {
                continue;
            }

            BasicNetworkedAgent networkedPotentialPredictor = (BasicNetworkedAgent) potentialPredictor;

            if (networkedPotentialPredictor.getCorrectPredictionCount() > currentHighestCorrectPredictionCount) {
                mostSuccessfulPredictors.clear();
                mostSuccessfulPredictors.add(potentialPredictor);
                currentHighestCorrectPredictionCount = networkedPotentialPredictor.getCorrectPredictionCount();
            } else if (networkedPotentialPredictor.getCorrectPredictionCount() == currentHighestCorrectPredictionCount) {
                mostSuccessfulPredictors.add(potentialPredictor);
            }
        }

        return mostSuccessfulPredictors.get(getRandomIndex(mostSuccessfulPredictors.size()));
    }

    private int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
    }
}