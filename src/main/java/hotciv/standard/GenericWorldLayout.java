package hotciv.standard;
import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hotciv.standard.*;
// TODO: define generic world layout for alphaciv, betaciv and gammaciv (assuming deltaciv is the only variant)
// TODO: this implements the worldLayout interface
public class GenericWorldLayout implements WorldLayout {
    @Override
    public void setupWorld(GameImpl game) {
        // Initialize map according to what we did in iteration 1
        game.units = new HashMap<>(); // Create a new units map
        game.units.put(new Position(0, 0), new UnitImpl(GameConstants.ARCHER, Player.RED));
        game.units.put(new Position(1, 1), new UnitImpl(GameConstants.SETTLER, Player.RED));
        game.units.put(new Position(1, 2), new UnitImpl(GameConstants.LEGION, Player.BLUE));
    }

}