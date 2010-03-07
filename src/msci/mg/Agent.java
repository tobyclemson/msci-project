package msci.mg;

import msci.mg.Choice;
import msci.mg.agents.abilities.Chooseable;
import msci.mg.agents.abilities.Identifiable;
import msci.mg.agents.abilities.Scorable;

/**
 *
 * @author Toby Clemson
 */
public interface Agent extends  Scorable, Identifiable<Agent>, Chooseable {

    /**
     * Tells the agent to prepare itself for the next choice making cycle.
     */
    void prepare();

    /**
     * Tells the agent to update its internal state given that the current
     * minority choice is as specified. The default implementation calls
     * {@link incrementScore} if the supplied minority choice is equal to the
     * current choice and adds the minority choice to this agent's memory.
     * 
     * @param minorityChoice The current minority choice.
     */
    void update(Choice minorityChoice);

}
