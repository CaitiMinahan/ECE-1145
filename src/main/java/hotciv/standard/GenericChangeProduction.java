package hotciv.standard;

import hotciv.standard.Interfaces.ChangeProduction;
import hotciv.framework.*;
import hotciv.standard.GameImpl;
import hotciv.standard.Interfaces.MutableGame;

import java.util.Objects;

public class GenericChangeProduction implements ChangeProduction {
    @Override
    public void changeProduction(Position p, String unitType, MutableGame game) {
        if(Objects.equals(unitType, GameConstants.ARCHER) && game.cities.get(p) != null &&  game.cities.get(p).getTreasury() >= 10){
            // produce a new Archer
            UnitImpl newArcher = new UnitImpl(GameConstants.ARCHER, game.getCurrentPlayer());
            game.units.put(p, newArcher);
            // need to reduce the city treasury by 10
            City currCity = game.cities.get(p);
            int currentTreasury = currCity.getTreasury();
            ((CityImpl) currCity).setTreasury(currentTreasury - 10);
        }
    }
}
