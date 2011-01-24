package msci.mg;

import msci.mg.agents.Agent;

import java.util.Collection;
import java.util.Map;

public class MinorityGame {
    private Community community;
    private ChoiceHistory choiceHistory;

    public MinorityGame(Community community, ChoiceHistory choiceHistory) {
        this.community = community;
        this.choiceHistory = choiceHistory;
    }

    public Collection<Agent> getAgents() {
        return community.getAgents();
    }

    public Community getCommunity() {
        return community;
    }

    public ChoiceHistory getChoiceHistory() {
        return choiceHistory;
    }

    public int getMinoritySize() {
        Map<Choice, Integer> choiceTotals;
        Integer numberChoosingA, numberChoosingB;

        choiceTotals = community.getChoiceTotals();

        numberChoosingA = choiceTotals.get(Choice.A);
        numberChoosingB = choiceTotals.get(Choice.B);

        return Math.min(numberChoosingA, numberChoosingB);
    }

    public Choice getMinorityChoice() {
        Map<Choice, Integer> choiceTotals = community.getChoiceTotals();
        Integer numberChoosingA = choiceTotals.get(Choice.A);
        Integer numberChoosingB = choiceTotals.get(Choice.B);

        return numberChoosingA < numberChoosingB ? Choice.A : Choice.B;
    }

    public void stepForward() {
        community.prepareAgents();
        community.makeChoices();

        Choice minorityChoice = getMinorityChoice();
        
        community.updateAgents(minorityChoice);
        choiceHistory.add(minorityChoice);
    }

}
