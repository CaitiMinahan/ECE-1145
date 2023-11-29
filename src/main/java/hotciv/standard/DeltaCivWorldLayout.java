package hotciv.standard;
import hotciv.framework.*;

import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.WorldLayout;

// this is the first variant of the world layout interface: DeltaCiv
public class DeltaCivWorldLayout implements WorldLayout {
    @Override
    public void setupWorld(MutableGame game) {
        // Place cities at the specified positions for Red and Blue players
        game.placeCity(new Position(8, 12), Player.RED);
        game.placeCity(new Position(4, 5), Player.BLUE);

        game.units.put(new Position(0, 0), new UnitImpl(GameConstants.ARCHER, Player.RED));
        game.units.put(new Position(1, 1), new UnitImpl(GameConstants.SETTLER, Player.RED));
        game.units.put(new Position(1, 2), new UnitImpl(GameConstants.LEGION, Player.BLUE));
    }
}

