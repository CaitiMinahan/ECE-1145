package hotciv.standard;

import hotciv.standard.Interfaces.ChangeProduction;
import hotciv.framework.*;
import hotciv.standard.GameImpl;

import java.util.Objects;

public class ThetaCivChangeProduction implements ChangeProduction {
    @Override
    public void changeProduction(Position p, String unitType, GameImpl game) {
        // the UFO type will cause the position to city to produce a new UFO
        // need to add in the game parameter to access the city
        if(Objects.equals(unitType, GameConstants.UFO) && game.cities.get(p) != null){
            // produce a new UFO
            UnitImpl newUfo = new UnitImpl(GameConstants.UFO, game.getCurrentPlayer());
            // assing the position to the current position
            game.units.put(p, newUfo);
        }
    }
}
