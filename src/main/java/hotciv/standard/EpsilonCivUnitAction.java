package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.MutableUnit;
import hotciv.standard.Interfaces.UnitAction;
import hotciv.standard.Interfaces.UnitAttacking;

import java.util.Objects;

/* The following code is a copy of gamma civ, but now we need
 * to also track the successful attacks that the player has won
 * */


public class EpsilonCivUnitAction implements UnitAction {

    // create an instance of the generic Unit attacking
    private UnitAttacking unitAttacking;

    // create a constructor that can use the different unitAttacking types
    public EpsilonCivUnitAction(UnitAttacking unitAttacking){
        this.unitAttacking = unitAttacking; // this is important for testing
    }
    @Override
    public void performAction(MutableUnit currentUnit, Position p, MutableGame currentGame)
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
    public void updateUnitMap(Position from, Position to, MutableUnit unit_from, MutableGame game){
        game.units.remove(from);
        game.units.put(to, unit_from);
    }

    // try to move unit and return true if nothing is there
    // then place the unit at the desired position
    // check for unit at 'from' position
    // Need to update the successful attacks here
    @Override
    public boolean moveUnit(Position from, Position to, MutableGame game) {
        Unit unit_from = game.getUnitAt(from);
        MutableUnit mUnit_from = (MutableUnit) unit_from;
        if (unit_from == null){
            return false;
        }
        // get the current unit type once
        String unitTypeString = unit_from.getTypeString();
        boolean unitCanMove = ((UnitImpl) unit_from).getCanMove();
        // check to see if the unit to move is a fortified archer
        if(unitTypeString.equals("archer") && !unitCanMove)
            return false;
        // @TODO need to update the generic with this too
        else if (unitTypeString.equals("archer") && unitCanMove && game.getUnitAt(to) == null)
        {
            updateUnitMap(from, to, mUnit_from, game);
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
            // @TODO need to add this to the generic as well
            // check if the unit is an immobilized archer
            // get the unit impl of attacking unit
            UnitImpl attackingUnitImpl = (UnitImpl)attackingUnit;
            if(Objects.equals(attackingUnit.getTypeString(), "archer") && !attackingUnitImpl.getCanMove())
            {
                return false;
            }
            if(defendingPlayer != attackingPlayer)
            {
                // let the attacking unit remove the defending unit and then successfully move to that tile
                // call the functions
                if(unitAttacking.canAttackerBeatDefender((UnitImpl) attackingUnit, (UnitImpl) foundUnit, from, to, game)) {
                    game.killUnit(to);
                    updateUnitMap(from, to, mUnit_from, game);
                    // update the successful attack map
                    int currentSuccessfulAttacks = game.playerSuccessfulAttacks.get(attackingPlayer);
                    game.playerSuccessfulAttacks.put(attackingPlayer, currentSuccessfulAttacks + 1);

                    // if the dead defender was guarding a city, transfer ownership of the city
                    City capturedCity = game.cities.get(to);
                    if(capturedCity != null){
                        // transfer ownership
                        ((CityImpl) capturedCity).setOwner(attackingPlayer);
                    }
                    return true;
                }
                else
                {
                    // @TODO: need to add this to the generic
                    // remove the attacking unit
                    game.killUnit(from);
                    return false; //defender won
                }
            }
            return false; // cannot fortify tiles (move own units to tile with own units)
        }
        // otherwise, move the unit from the original position to the new one
        updateUnitMap(from, to, mUnit_from, game);
        return true;
    }
}