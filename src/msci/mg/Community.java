package msci.mg;

import msci.mg.agents.Agent;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * The {@code Community} represents a society of agents providing methods to
 * act on that society as a whole.
 *
 * @author Toby Clemson
 */
public class Community {
    private Graph<Agent, Friendship> socialNetwork;

    /**
     * Sets the social network of agents to the supplied graph.
     *
     * @param socialNetwork The social network for this community to manage.
     */
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

    /**
     * @return A map of the possible choices Choice.A and Choice.B to the
     * number of agents that have made that choice.
     */
    public Map<Choice, Integer> getChoiceTotals() {
        HashMap<Choice, Integer> totals = new HashMap<Choice, Integer>();
        int choiceATotal = 0;
        int choiceBTotal = 0;

        for(Agent agent : getAgents()) {
            switch(agent.getChoice()) {
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

    /**
     * Prepares each agent to make a choice.
     */
    public void prepareAgents() {
        for(Agent agent : getAgents()) {
            agent.prepare();
        }
    }

    /**
     * Asks each agent in this community to make a choice between Choice.A and
     * Choice.B.
     */
    public void makeChoices() {
        for(Agent agent : getAgents()) {
            agent.choose();
        }
    }

    /**
     * Updates each agent based on the current minority choice.
     *
     * @param minorityChoice The current minority choice.
     */
    public void updateAgents(Choice minorityChoice) {
        for(Agent agent : getAgents()) {
            agent.update(minorityChoice);
        }
    }
}
