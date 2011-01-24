package msci.mg.agents;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import msci.mg.Choice;

public class RandomAgent extends AbstractAgent implements UnintelligentAgent {
    protected static AbstractDistribution randomNumberGenerator;

    static {
        randomNumberGenerator = new Uniform(
                new MersenneTwister((int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE)));
    }

    public RandomAgent() {
        super();
    }

    public void choose() {
        Choice currentChoice;

        if (randomNumberGenerator.nextInt() == 0) {
            currentChoice = Choice.A;
        } else {
            currentChoice = Choice.B;
        }

        this.choice = currentChoice;
    }
}
