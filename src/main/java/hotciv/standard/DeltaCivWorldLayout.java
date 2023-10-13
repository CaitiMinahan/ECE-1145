package hotciv.standard;
import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hotciv.standard.*;
// this is the first variant of the world layout interface: DeltaCiv
public class DeltaCivWorldLayout implements WorldLayout {
    @Override
    public void setupWorld(GameImpl game) {
        // Place cities at the specified positions for Red and Blue players
        game.placeCity(new Position(8, 12), Player.RED);
        game.placeCity(new Position(4, 5), Player.BLUE);

    }
}

