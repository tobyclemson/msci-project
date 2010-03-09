package msci.mg.agents.abilities;

/**
 * The {@code Scorable} interface encapsulates the ability to maintain an
 * integer score and increment it according to some incrementation strategy.
 *
 * @author Toby Clemson
 */
public interface Scorable {
    int getScore();
    void incrementScore();
}
