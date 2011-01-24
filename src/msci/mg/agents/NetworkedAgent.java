package msci.mg.agents;

import edu.uci.ics.jung.graph.Graph;
import msci.mg.Choice;
import msci.mg.Friendship;
import msci.mg.Neighbourhood;

import java.util.Collection;

public interface NetworkedAgent extends IntelligentAgent {
    Graph<? extends Agent, Friendship> getSocialNetwork();
    Agent getBestFriend();
    Collection<? extends Agent> getFriends();
    Neighbourhood getNeighbourhood();
    void setNeighbourhood(Neighbourhood neighbourhood);
    Choice getPrediction();
    int getCorrectPredictionCount();
    void incrementCorrectPredictionCount();
}
