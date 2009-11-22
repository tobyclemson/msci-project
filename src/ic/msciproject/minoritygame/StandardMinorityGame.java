package ic.msciproject.minoritygame;

import java.util.ArrayList;

/**
 * The StandardMinorityGame class represents the simplest variant of the
 * minority game in which only global information in the form of the history
 * string is available to the agents in making their choice of outcome.
 * @author tobyclemson
 */
public class StandardMinorityGame extends AbstractMinorityGame{

    /**
     * Constructs a StandardMinorityGame instance setting the agents and
     * history string parameters to the supplied ArrayList and HistoryString
     * instances.
     * @param agents An ArrayList instance containing the agents associated
     * with this minority game instance.
     * @param historyString A HistoryString instance to use as the history
     * string for this minority game instance.
     */
    public StandardMinorityGame(
        ArrayList<AbstractAgent> agents,
        HistoryString historyString
    ){
        super(agents, historyString);
    }

}
