package msci.mg.agents.abilities;

import java.util.UUID;

/**
 * The {@code Identifiable} interface encapsulates the ability to be identified
 * as unique from other instances.
 * 
 * @author Toby Clemson
 */
public interface Identifiable {
    /**
     * It should be extremely unlikely that two {@code Identifiable}'s have the
     * same identification number.
     */
    UUID getIdentificationNumber();
}
