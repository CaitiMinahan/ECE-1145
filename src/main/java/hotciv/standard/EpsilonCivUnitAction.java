package hotciv.standard;
import hotciv.framework.*;
import java.util.Objects;

import static java.util.Objects.isNull;

/* The following code is a copy of gamma civ, but now we need
* to also track the successful attacks that the player has won
* */
public class EpsilonCivUnitAction implements UnitAction{
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
        //define a function to move the units since it gets called three times
        @Override
        public void updateUnitMap(Position from, Position to, Unit unit_from, GameImpl game){
            game.units.remove(from);
            game.units.put(to, unit_from);
        }

        // try to move unit and return true if nothing is there
        // then place the unit at the desired position
        // check for unit at 'from' position
        // Need to update the successful attacks here
        @Override
        public boolean moveUnit( Position from, Position to, GameImpl game) {
            Unit unit_from = game.getUnitAt(from);
            if (unit_from == null){
                return false;
            }
            // get the current unit type once
            String unitTypeString = unit_from.getTypeString();
            boolean unitCanMove = ((UnitImpl) unit_from).getCanMove();
            // check to see if the unit to move is a fortified archer
            if(unitTypeString.equals("archer") && !unitCanMove)
                return false;
            else if (unitTypeString.equals("archer") && unitCanMove)
            {
                updateUnitMap(from, to, unit_from, game);
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
                if (!game.canUnitAttack(attackingUnit)) {
                    return false;
                }
                if(defendingPlayer != attackingPlayer)
                {
                    // let the attacking unit remove the defending unit and then successfully move to that tile
                    // TODO: have to go through the attacking a defensive calculations before killing a unit
                    game.killUnit(to);
                    updateUnitMap(from, to, unit_from, game);
                    // update the successful attack map
                    int currentSuccessfulAttacks = game.playerSuccessfulAttacks.get(attackingPlayer);
                    game.playerSuccessfulAttacks.put(attackingPlayer, currentSuccessfulAttacks+1);
                    return true;
                }
                return false; // cannot fortify tiles (move own units to tile with own units)
            }
            // otherwise, move the unit from the original position to the new one
            updateUnitMap(from, to, unit_from, game);
            return true;
        }

    }

}
