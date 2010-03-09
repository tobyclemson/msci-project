package msci.mg.agents;

import msci.mg.Choice;
import msci.mg.agents.abilities.Choosable;
import msci.mg.agents.abilities.Identifiable;
import msci.mg.agents.abilities.Scorable;

/**
 * The {@code Agent} interface represents the minimum behaviour requirements
 * of an agent in the minority game.
 *
 * @author Toby Clemson
 */
public interface Agent extends 
    Choosable,
    Comparable<Agent>,
    Identifiable,
    Scorable {

    /**
     * Tells the agent to prepare itself for the next choice making cycle.
     */
    void prepare();

    /**
     * Tells the agent to update its state given that the correct choice for the
     * current turn is as supplied.
     *
     * @param correctChoice The correct choice for the current turn.
     */
    void update(Choice correctChoice);

}
