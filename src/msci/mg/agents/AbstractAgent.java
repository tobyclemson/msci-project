package msci.mg.agents;

import msci.mg.Agent;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.UUID;
import msci.mg.Choice;
import msci.mg.Friendship;
import msci.mg.Neighbourhood;

/**
 * The {@code AbstractAgent} class implements the basic functionality of an
 * agent i.e., the ability to be identified and scored as well as the basic
 * mechanisms of preparing and updating for choices. However,
 * {@code AbstractAgent} does not implement a mechanism for making a choice as
 * this is the responsibility of subclasses.
 *
 * @author Toby Clemson
 */
public abstract class AbstractAgent implements Agent {
    protected UUID identificationNumber;
    protected int score = 0;
    protected int correctPredictionCount = 0;
    protected Neighbourhood neighbourhood;
    protected Agent bestFriend;
    protected Choice prediction = null;
    protected Choice choice = null;

    public AbstractAgent(){
        this.identificationNumber = UUID.randomUUID();
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * No two agents will have the same identification number.
     */
    public UUID getIdentificationNumber() {
        return this.identificationNumber;
    }

    public int getScore() {
        return score;
    }

    public int getCorrectPredictionCount() {
        return correctPredictionCount;
    }

    /**
     * This method requires that the {@link #choose()} method has been called
     * since this object was created. If it hasn't, an 
     * {@code IllegalStateException} is thrown.
     */
    public Choice getChoice() {
        if(choice == null) {
            throw new IllegalStateException(
                "No choice has been made yet."
            );
        }
        return choice;
    }

    /**
     * If no prediction has been made yet, this mehthod throws an
     * {@code IllegalStateException}.
     */
    public Choice getPrediction() {
        if(prediction == null) {
            throw new IllegalStateException(
                "No prediction has been made yet"
            );
        }
        return prediction;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    public Collection<? extends Agent> getFriends() {
        if(this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }

        return this.neighbourhood.getFriends();
    }

    /**
     * An agent's best friend is defined as the agent whose prediction the
     * agent most recently followed. In the default implementation, since
     * agents have no knowledge of each other, a reference to this agent is
     * returned since this agent always follows its own predictions.
     */
    public Agent getBestFriend() {
        return this;
    }

    /**
     * This method requires that a {@code Neighbourhood} instance has been set
     * for this agent. If no neighbourhood has been set, an
     * {@code IllegalStateException} is thrown.
     */
    public Graph<? extends Agent, Friendship> getSocialNetwork() {
        if(this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }

        return neighbourhood.getSocialNetwork();
    }

    /**
     * The comparison is conducted as follows:
     * <ul>
     *  <li>if this agent's identification number is less than the other
     *      agent's, the method returns a negative number
     *  <li>if both agent's identification numbers are the same, the method
     *      returns 0
     *  <li>if the other agent's identification number is greater than
     *      this agent's, the method returns a positive number.
     * </ul>
     */
    public int compareTo(Agent otherAgent) {
        return this.getIdentificationNumber().compareTo(
            otherAgent.getIdentificationNumber()
        );
    }

    /**
     * This default implementation adds 1 to the current score.
     */
    public void incrementScore() {
        score += 1;
    }

    /**
     * The default implementation adds 1 to the correct prediction count.
     */
    public void incrementCorrectPredictionCount() {
        correctPredictionCount += 1;
    }

    /**
     * The default implementation does nothing.
     */
    public void prepare() {}

    /**
     * Updates this agent given that the correct choice was as supplied. In the
     * default implementation, the agent's score is incremented and the
     * correct choice is added to its memory.
     */
    public void update(Choice correctChoice) {
        if(choice == correctChoice) {
            incrementScore();
        }
    }

}
