package msci.mg.factories;

import msci.mg.Friendship;
import org.apache.commons.collections15.Factory;

/**
 * The FriendshipFactory class constructs Friendship objects representing
 * edges in the social network.
 * @author tobyclemson
 */
public class FriendshipFactory implements Factory<Friendship>{

    /**
     * Constructs a Friendship object and returns it.
     * @return The Friendship instance created.
     */
    public Friendship create() {
        return new Friendship();
    }

}
