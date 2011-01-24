package msci.mg.agents;

import msci.mg.Choice;

import java.util.UUID;

public abstract class AbstractAgent implements Agent {
    protected UUID identificationNumber;
    protected int score = 0;
    protected Choice choice = null;

    public AbstractAgent() {
        this.identificationNumber = UUID.randomUUID();
    }

    public UUID getIdentificationNumber() {
        return this.identificationNumber;
    }

    public int getScore() {
        return score;
    }

    public Choice getChoice() {
        if (choice == null) {
            throw new IllegalStateException("No choice has been made yet.");
        }
        return choice;
    }

    public int compareTo(Agent otherAgent) {
        return this.getIdentificationNumber().compareTo(otherAgent.getIdentificationNumber());
    }

    public void incrementScore() {
        score += 1;
    }

    public void prepare() {

    }

    public void update(Choice correctChoice) {
        if (choice == correctChoice) {
            incrementScore();
        }
    }

}
