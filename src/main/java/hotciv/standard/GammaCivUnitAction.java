package hotciv.standard;

import hotciv.framework.Game;
import hotciv.framework.Player;

import java.util.Objects;

public class GammaCivUnitAction implements UnitAction{
    @Override
    public void performAction(UnitImpl currentUnit)
    {
        // if the current unit is a settler
        // build a city
        // remove the unit from the world
        // replace unit with city of same owner
        // population size 1

        Player owner = currentUnit.getOwner();
        if(Objects.equals(currentUnit.getTypeString(), "settler"))
        {
            // get the position of the unit
            // TODO: need to get the location of the unit by creating new function

            // remove the old unit


        }
    }
}
