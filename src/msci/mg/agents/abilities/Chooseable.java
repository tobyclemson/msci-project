package msci.mg.agents.abilities;

import msci.mg.Choice;

/**
 * The {@code Choosable} interface encapsulates the ability to choose between
 * the two outcomes {@code Choice.A} and {@code Choice.B}.
 *
 * @author Toby Clemson
 */
public interface Chooseable {

    /**
     * Instructs the {@code Choosable} to choose between {@code Choice.A} and
     * {@code Choice.B}.
     */
    void choose();

    /**
     * Returns the choice made the last time {@link Choosable#choose()} was
     * called, either {@code Choice.A} or {@code Choice.B}. If the
     * {@link #choose} method has not yet been called, an
     * {@code IllegalStateException} is thrown.
     *
     * @return The choice made the last time {@code Choosable#choose()} was
     * called.
     */
    Choice getChoice();

}
