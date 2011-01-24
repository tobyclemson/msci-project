package msci.mg;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

public class ChoiceHistory {

    private static Choice choiceSet[] = {Choice.A, Choice.B};
    private static AbstractDistribution randomNumberGenerator;

    static {
        randomNumberGenerator = new Uniform(
                new MersenneTwister((int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE)));
    }

    private List<Choice> contents;

    public ChoiceHistory(int initialLength) {
        this.contents = new ArrayList<Choice>(initialLength);

        for (int i = 0; i < initialLength; i++) {
            contents.add(choiceSet[randomNumberGenerator.nextInt()]);
        }
    }

    public int getSize() {
        return contents.size();
    }

    public List<Choice> asList() {
        return contents;
    }

    public List<Choice> asList(int distanceIntoThePast) {
        int size = contents.size();
        return contents.subList(size - distanceIntoThePast, size);
    }

    public void add(Choice choice) {
        contents.add(choice);
    }
}
