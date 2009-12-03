/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ic.msciproject.minoritygame;

import java.util.List;
import java.util.Random;

/**
 *
 * @author tobyclemson
 */
public class RandomAgent extends AbstractAgent {
    public RandomAgent(StrategyCollection strategyCollection) {
        super(strategyCollection);
    }

    @Override
    public Choice choose(List<Choice> choiceHistory) {
        Random randomChoiceGenerator = new Random();
        Choice choice = null;

        if(randomChoiceGenerator.nextBoolean()) {
            choice = Choice.A;
        } else {
            choice = Choice.B;
        }

        lastChoice = choice;

        return choice;
    }
}
