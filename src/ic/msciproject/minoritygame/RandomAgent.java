/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ic.msciproject.minoritygame;

import java.util.List;
import cern.jet.random.engine.*;
import cern.jet.random.*;

/**
 *
 * @author tobyclemson
 */
public class RandomAgent extends AbstractAgent {
    protected static AbstractDistribution randomGenerator;

    static {
        randomGenerator = new Uniform(
            new MersenneTwister(new java.util.Date())
        );
    }

    public RandomAgent(StrategyManager strategyManager) {
        super(strategyManager);
    }

    public void incrementScore() {
        score += 1;
    }

    @Override
    public void choose(List<Choice> choiceHistory) {
        Choice choice = null;

        if(randomGenerator.nextInt() == 0) {
            choice = Choice.A;
        } else {
            choice = Choice.B;
        }

        lastChoice = choice;
    }

    public void update(
        Choice minorityChoice,
        List<Choice> choiceHistory
    ) {
        
    }
}
