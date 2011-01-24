package msci.mg.agents;

import msci.mg.Choice;
import msci.mg.agents.abilities.Choosable;
import msci.mg.agents.abilities.Identifiable;
import msci.mg.agents.abilities.Scorable;

public interface Agent extends Choosable, Comparable<Agent>, Identifiable, Scorable {
    void prepare();
    void update(Choice correctChoice);
}
