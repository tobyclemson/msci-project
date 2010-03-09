package msci.mg.agents;

import java.util.UUID;
import msci.mg.Choice;

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
    protected Choice choice = null;

    /**
     * Constructs an {@code AbstractAgent} by initialising the identification
     * number to a random {@code UUID}
     */
    public AbstractAgent(){
        this.identificationNumber = UUID.randomUUID();
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
     * The default implementation does nothing.
     */
    public void prepare() {}

    /**
     * Updates this agent given that the correct choice was as supplied. In the
     * default implementation, the agent's score is incremented.
     */
    public void update(Choice correctChoice) {
        if(choice == correctChoice) {
            incrementScore();
        }
    }

}
