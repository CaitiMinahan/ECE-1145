package hotciv.standard;

import hotciv.standard.Interfaces.ChangeProduction;
import hotciv.framework.*;
import hotciv.standard.GameImpl;
import hotciv.standard.Interfaces.MutableGame;

import java.util.Objects;

public class ThetaCivChangeProduction implements ChangeProduction {
    @Override
    public void changeProduction(Position p, String unitType, MutableGame game) {
        // the UFO type will cause the position to city to produce a new UFO
        // need to add in the game parameter to access the city
        // can only produce this if the city has >= 60 production
        if(Objects.equals(unitType, GameConstants.UFO) && game.cities.get(p) != null &&  game.cities.get(p).getTreasury() >= 60 ){
            // produce a new UFO
            UnitImpl newUfo = new UnitImpl(GameConstants.UFO, game.getCurrentPlayer());
            game.units.put(p, newUfo);
            // need to reduce the city treasury by 60
            City currCity = game.cities.get(p);
            int currentTreasurey = currCity.getTreasury();
            ((CityImpl) currCity).setTreasury(currentTreasurey - 60);
        }
    }
}
