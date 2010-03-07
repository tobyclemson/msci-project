package msci.mg.agents.abilities;

/**
 * The {@code Scorable} interface encapsulates the ability to maintain an
 * integer score and increment it according to some incrementation strategy.
 * @author Toby Clemson
 */
public interface Scorable {

    /**
     * Returns the current score of this {@code Scorable} as an integer.
     *
     * @return This {@code Scorable}'s current score.
     */
    int getScore();

    /**
     * Increments the {@code Scorable}'s score.
     */
    void incrementScore();

}
