package ic.msciproject.minoritygame;

import java.util.ArrayList;

/**
 * The AbstractMinorityGame class implements the basic functionality of a
 * minority game and all specific versions of the minority game should extend
 * it.
 * @author tobyclemson
 */
public class AbstractMinorityGame {

    /**
     * An ArrayList instance holding AbstractAgents used to represent the
     * agents associated with the minority game. This is returned by the
     * {@link #getAgents} method.
     */
    private ArrayList<AbstractAgent> agents;

    /**
     * A HistoryString instance holding the history string representing a
     * string of recent outcomes in the minority game. This is returned by the
     * {@link #getHistoryString} method.
     */
    private HistoryString historyString;

    /**
     * Constructs an AbstractMinorityGame instance setting the agents and
     * history string parameters to the supplied ArrayList and HistoryString
     * instances.
     * @param agents An ArrayList instance containing the agents associated
     * with this minority game instance.
     * @param historyString A HistoryString instance to use as the history
     * string for this minority game instance.
     */
    public AbstractMinorityGame(
        ArrayList<AbstractAgent> agents,
        HistoryString historyString
    ){
        this.agents = agents;
        this.historyString = historyString;
    }

    /**
     * Returns an ArrayList of derivatives of AbstractAgent representing the
     * agents associated with this instance of the minority game.
     * @return An ArrayList containing the agents associated with the minority
     * game.
     */
    public ArrayList<AbstractAgent> getAgents(){
        return agents;
    }

    /**
     * Returns a HistoryString instance containing a string of the recent
     * outcomes of time steps in the minority game
     * @return A HistoryString instance representing the history string for the
     * minority game.
     */
    public HistoryString getHistoryString(){
        return historyString;
    }
}
