package hotciv.standard;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Unit;

import java.util.Objects;

public class GammaCivUnitAction implements UnitAction{
    @Override
    public void performAction(UnitImpl currentUnit, Position p, GameImpl currentGame)
    {
        // if the current unit is a settler
        // build a city
        // remove the unit from the world
        // replace unit with city of same owner
        // population size 1

        Player owner = currentUnit.getOwner();
        if(Objects.equals(currentUnit.getTypeString(), "settler"))
        {
            // remove the old unit
            currentGame.killUnit(p);
            // fill the position with a new city
            currentGame.placeCity(p, owner);
        }
        // consider if the unit is an archer
        else if(Objects.equals(currentUnit.getTypeString(), "archer"))
        {
            // the action will fortify (double its defensive strength)
            // if already fortified the archer will be unable to move.
            // calling the action again will remove its fortification
            currentUnit.setDefendingStrength(currentUnit.getDefensiveStrength() * 2);
            currentUnit.setCanMove(!currentUnit.getCanMove()); // toggles the current move status
        }
    }
    public boolean moveUnit( Position from, Position to, GameImpl game) {

        // try to move unit and return true if nothing is there
        // then place the unit at the desired position
        // check for unit at 'from' position
        Unit unit_from = game.getUnitAt(from);
        // if there's no unit at the 'from' position (aka, there's nothing to move)
        if (unit_from == null){
            return false;
        }
        // check to see if the unit to move is a fortified archer
        if(unit_from.getTypeString().equals("archer") && ((UnitImpl) unit_from).getCanMove() == false)
        {
            return false;
        }
        else if (unit_from.getTypeString().equals("archer") && ((UnitImpl) unit_from).getCanMove() == true)
        {
            // update the destination tile with unit
            // TODO : should abstract this to function since it gets called 3 different times
            game.units.remove(from);
            game.units.put(to, unit_from);
//            updateUnitMap();
            return true;
        }
        // if the 'to' unit already has a unit there
        if (game.getUnitAt(to) != null) {
            // check to see if the current player occupies this unit or an enemy does
            Unit foundUnit = game.getUnitAt(to);
            Unit attackingUnit = game.getUnitAt(from);
            Player defendingPlayer = foundUnit.getOwner();
            Player attackingPlayer = attackingUnit.getOwner();

            // check if the attacking unit is capable of attacking
            // remove this statement to show case breaking
            if (!game.canUnitAttack(attackingUnit)) {
                return false;
            }

            if(defendingPlayer != attackingPlayer)
            {
                // let the attacking unit remove the defending unit and then successfully move to that tile
                game.killUnit(to);
                // update the destination tile with unit
                game.units.remove(from);
                game.units.put(to, unit_from);
//                updateUnitMap();
                return true;
            }
            return false; // cannot fortify tiles (move own units to tile with own units)
        }
        // otherwise, move the unit from the original position to the new one
        game.units.remove(from);
        game.units.put(to, unit_from);
//        updateUnitMap();
        return true;
    }

}
