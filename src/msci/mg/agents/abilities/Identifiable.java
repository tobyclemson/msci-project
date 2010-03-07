package msci.mg.agents.abilities;

import java.util.UUID;

/**
 * The {@code Identifiable} interface encapsulates the ability to be identified
 * as unique from other instances and to be ordered based on the identifier.
 *
 * @param <T> 
 * @author Toby Clemson
 */
public interface Identifiable<T> extends Comparable<T> {

    /**
     * Compares this {@code Identifiable} to the supplied {@code Identifiable}
     * basing the comparison on the identification number returned by 
     * {@link Identifiable#getIdentificationNumber()}.
     *
     * @param other The instance to compare to.
     * @return An integer whose sign depends on the comparison of each 
     * {@code Identifiable}'s identification number.
     */
    int compareTo(T other);

    /**
     * Returns this {@code Identifiable}'s identification number. It should be
     * extremely unlikely that two {@code Identifiable}'s have the same
     * identification number.
     *
     * @return This agent's identification number.
     */
    UUID getIdentificationNumber();

}
