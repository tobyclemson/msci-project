package msci.mg.factories;

import msci.mg.Friendship;
import org.apache.commons.collections15.Factory;

public class FriendshipFactory implements Factory<Friendship> {
    public Friendship create() {
        return new Friendship();
    }
}
