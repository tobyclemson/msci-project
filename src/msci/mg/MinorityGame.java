package msci.mg;

import java.util.List;
import java.util.Map;
import msci.mg.agents.AbstractAgent;

/**
 * The MinorityGame class represents a minority game which can step forward in
 * time by performing actions on the associated community of agents and
 * choice history.
 * @author tobyclemson
 */
public class MinorityGame {

    /**
     * A Community instance containing AbstractAgent instances representing the
     * agents associated with the minority game. This is returned by the
     * {@link #getCommunity} method.
     */
    protected Community community;

    /**
     * A ChoiceHistory instance holding a list of choices representing the
     * entire history of recent outcomes in the minority game. This is returned
     * by the {@link #getChoiceHistory} method.
     */
    protected ChoiceHistory choiceHistory;

    /**
     * Constructs a MinorityGame instance setting the community and choice 
     * history attributes to the supplied Community and ChoiceHistory instances.
     * @param community A Community instance containing the agents
     * associated with the minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history
     * of outcomes for the minority game instance.
     */
    public MinorityGame(
        Community community,
        ChoiceHistory choiceHistory
    ){
        this.community = community;
        this.choiceHistory = choiceHistory;
    }

    /**
     * Returns a List of AbstractAgent instances representing the agents
     * associated with the minority game.
     * @return The agents associated with the minority game.
     */
    public List<AbstractAgent> getAgents() {
        return community.getAgents();
    }

    /**
     * Returns the associated Community instance.
     * @return The associated community instance.
     */
    public Community getCommunity() {
        return community;
    }

    /**
     * Returns a ChoiceHistory instance containing a list of Choice values
     * representing the past minority choices in the minority game.
     * @return A ChoiceHistory instance representing the history of outcomes
     * for the minority game.
     */
    public ChoiceHistory getChoiceHistory() {
        return choiceHistory;
    }

    /**
     * Returns the size of the current minority group.
     * @return The size of the minority group.
     */
    public int getMinoritySize() {
        // declare variables
        Map<Choice, Integer> choiceTotals;
        Integer numberChoosingA, numberChoosingB;
        
        // get a mapping of the choices Choice.A and Choice.B to the number of
        // agents making that choice.
        choiceTotals = community.getChoiceTotals();
        
        // retrieve the numbers of agents making each choice.
        numberChoosingA = choiceTotals.get(Choice.A);
        numberChoosingB = choiceTotals.get(Choice.B);

        // return the smallest of those numbers.
        return Math.min(numberChoosingA, numberChoosingB);
    }

    /**
     * Returns the current minority choice.
     * @return The minority choice.
     */
    public Choice getMinorityChoice() {
        // declare variables
        Map<Choice, Integer> choiceTotals;
        Integer numberChoosingA, numberChoosingB;

        // get a mapping of the choices Choice.A and Choice.B to the number of
        // agents that have made that choice.
        choiceTotals = community.getChoiceTotals();

        // retrieve the numbers of agents making each choice.
        numberChoosingA = choiceTotals.get(Choice.A);
        numberChoosingB = choiceTotals.get(Choice.B);

        // return the choice corresponding to the smallest of thise numbers.
        if(numberChoosingA < numberChoosingB) {
            return Choice.A;
        }
        else {
            return Choice.B;
        }
    }

    /**
     * Takes a step forward in the game.
     *
     * The step is taken as follows:
     * <ul>
     *  <li>Ask all agents to prepare for this time step
     *  <li>Ask all agents to make a choice for this time step
     *  <li>Ask all agents to update their local information given the
     *      current minority choice
     *  <li>Add the most recent minority choice outcome to the choice history
     * </ul>
     */
    public void stepForward() {
      // tell the community to prepare the agents to make a choice
      community.prepareAgents();

      // tell the community to tell all agents to actually make a choice
      community.makeChoices();

      // find out what the agent consensus is
      Choice minorityChoice = getMinorityChoice();

      // tell the community to update each agent given the minority
      // choice
      community.updateAgents(minorityChoice);

      // add the minority choice to the global choice history
      choiceHistory.add(minorityChoice);
    }

}
