package hotciv.standard;
import hotciv.framework.*;

import java.util.HashMap;

import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.WorldLayout;
// this defines generic world layout for alphaciv, betaciv and gammaciv (assuming deltaciv is the only variant)
// step 4 - move world layout setup to a class implementing the worldLayout interface

public class GenericWorldLayout implements WorldLayout {
    @Override
    public void setupWorld(MutableGame game) {
        // Initialize map according to what we did in iteration 1
        game.units.put(new Position(0, 0), new UnitImpl(GameConstants.ARCHER, Player.RED));
        game.units.put(new Position(1, 1), new UnitImpl(GameConstants.SETTLER, Player.RED));
        game.units.put(new Position(1, 2), new UnitImpl(GameConstants.LEGION, Player.BLUE));
    }
}