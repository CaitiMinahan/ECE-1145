package hotciv.standard;

import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EpsilonCivPlayerSetup implements PlayerSetup {
    @Override
    public void setupPlayer(GameImpl game) {
        // Init the hash map for the two player
        // TODO: can expand this later for more color players
        game.playerSuccessfulAttacks.put(Player.RED, 0);
        game.playerSuccessfulAttacks.put(Player.BLUE, 0);
    }
}
