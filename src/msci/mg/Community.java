package msci.mg;

import edu.uci.ics.jung.graph.Graph;
import msci.mg.agents.Agent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Community {
    private Graph<Agent, Friendship> socialNetwork;

    public Community(Graph<Agent, Friendship> socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public Collection<Agent> getAgents() {
        return socialNetwork.getVertices();
    }

    public Collection<Friendship> getFriendships() {
        return socialNetwork.getEdges();
    }

    public Graph<Agent, Friendship> getSocialNetwork() {
        return socialNetwork;
    }

    public int getNumberOfAgents() {
        return socialNetwork.getVertexCount();
    }

    public Map<Choice, Integer> getChoiceTotals() {
        HashMap<Choice, Integer> totals = new HashMap<Choice, Integer>();
        int choiceATotal = 0;
        int choiceBTotal = 0;

        for (Agent agent : getAgents()) {
            switch (agent.getChoice()) {
                case A:
                    choiceATotal++;
                    break;
                case B:
                    choiceBTotal++;
                    break;
            }
        }

        totals.put(Choice.A, choiceATotal);
        totals.put(Choice.B, choiceBTotal);

        return totals;
    }

    public void prepareAgents() {
        for (Agent agent : getAgents()) {
            agent.prepare();
        }
    }

    public void makeChoices() {
        for (Agent agent : getAgents()) {
            agent.choose();
        }
    }

    public void updateAgents(Choice minorityChoice) {
        for (Agent agent : getAgents()) {
            agent.update(minorityChoice);
        }
    }
}
