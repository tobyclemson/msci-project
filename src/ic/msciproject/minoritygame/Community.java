package ic.msciproject.minoritygame;

import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * The Community class holds a collection of AbstractAgent objects
 * extending the collection with agent related functionality.
 * @author tobyclemson
 */
public class Community {

    /**
     * A graph representing the social network of friendships between agents in
     * the game.
     */
    private Graph<AbstractAgent, Friendship> socialNetwork;

    /**
     * Constructs a community instance to manage the supplied social network
     * of agents and friendships between them.
     * @param socialNetwork The social network for this community to manage.
     */
    public Community(Graph<AbstractAgent, Friendship> socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    /**
     * Returns all agents managed by this community sorted by
     * identification_number.
     * @return A sorted list of the agents managed by the community.
     */
    public List<AbstractAgent> getAgents() {
        List<AbstractAgent> agents =
            new ArrayList<AbstractAgent>(socialNetwork.getVertices());

        Collections.sort(agents);

        return agents;
    }

    /**
     * Returns all of the friendships between agents in the community.
     * @return The friendships between agents in the community.
     */
    public Collection<Friendship> getFriendships() {
        return socialNetwork.getEdges();
    }

    /**
     * Returns the social network that is managed by this community.
     * @return The social network managed by this community.
     */
    public Graph<AbstractAgent, Friendship> getSocialNetwork() {
        return socialNetwork;
    }

    /**
     * Returns an integer representing the number of agents in the
     * community.
     * @return The number of agents in the community.
     */
    public int getNumberOfAgents() {
        return socialNetwork.getVertexCount();
    }

    /**
     * Returns a map of the possible choices Choice.A and Choice.B to the
     * number of agents that have made that choice.
     * @return A map of choice totals.
     */
    public Map<Choice, Integer> getChoiceTotals() {
        // initialise required variables.
        HashMap<Choice, Integer> totals = new HashMap<Choice, Integer>();
        int choiceATotal = 0;
        int choiceBTotal = 0;

        // count the number of agents that have made the choices Choice.A and
        // Choice.B.
        for(AbstractAgent agent : getAgents()) {
            switch(agent.getChoice()) {
                case A:
                    choiceATotal++;
                    break;
                case B:
                    choiceBTotal++;
                    break;
            }
        }

        // set the totals for the choices Choice.A and Choice.B.
        totals.put(Choice.A, choiceATotal);
        totals.put(Choice.B, choiceBTotal);

        // return the totals map.
        return totals;
    }

    /**
     * Asks each agent in this community to prepare to make a choice.
     */
    public void prepareAgents() {
        for(AbstractAgent agent : getAgents()) {
            agent.prepare();
        }
    }

    /**
     * Asks each agent in this community to make a choice between Choice.A and
     * Choice.B.
     */
    public void makeChoices() {
        for(AbstractAgent agent : getAgents()) {
            agent.choose();
        }
    }

    /**
     * Asks each agent in this Community to update based on the current minority
     * choice.
     * @param minorityChoice The current minority choice.
     */
    public void updateAgents(Choice minorityChoice) {
        for(AbstractAgent agent : getAgents()) {
            agent.update(minorityChoice);
        }
    }
}
