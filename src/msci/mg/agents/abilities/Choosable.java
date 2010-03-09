package msci.mg.agents.abilities;

import msci.mg.Choice;

/**
 * The {@code Choosable} interface encapsulates the ability to choose between
 * the possible choices in the {@code Choice} enumeration.
 *
 * @author Toby Clemson
 */
public interface Choosable {
    void choose();

    /**
     * If the {@link #choose} method has not yet been called, an
     * {@code IllegalStateException} is thrown.
     *
     * @return The choice made the last time {@code Choosable#choose()} was
     * called.
     */
    Choice getChoice();

}
