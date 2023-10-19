package hotciv.standard;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Unit;

public class GenericUnitAction implements UnitAction{
    @Override
    public void performAction(UnitImpl currentUnit, Position p, GameImpl currentGame)
    {
        // based on the selected unit, make the unit do something
        // generic doesn't take action yet
    }

    @Override
    public void updateUnitMap(Position from, Position to, Unit unit_from, GameImpl game){
        game.units.remove(from);
        game.units.put(to, unit_from);
    }
    // create a method for moving based on the UnitActionType
    // try to move unit and return true if nothing is there
    // then place the unit at the desired position
    // check for unit at 'from' position
    @Override
    public boolean moveUnit( Position from, Position to, GameImpl game ) {
        Unit unit_from = game.getUnitAt(from);
        if (unit_from == null){
            return false;
        }
        // if the 'to' unit already has a unit there
        if (game.getUnitAt(to) != null) {
            // check to see if the current player occupies this unit or an enemy does
            Unit foundUnit = game.getUnitAt(to);
            Unit attackingUnit = game.getUnitAt(from);
            Player defendingPlayer = foundUnit.getOwner();
            Player attackingPlayer = attackingUnit.getOwner();

            if (!game.canUnitAttack(attackingUnit)) {
                return false;
            }
            if(defendingPlayer != attackingPlayer)
            {
                // let the attacking unit remove the defending unit and then successfully move to that tile
                game.killUnit(to);
                updateUnitMap(from, to, unit_from, game);
                return true;
            }
            return false; // cannot fortify tiles (move own units to tile with own units)
        }
        // otherwise, move the unit from the original position to the new one
        updateUnitMap(from, to, unit_from, game);
        return true;
    }



}
