package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.Interfaces.UnitAction;
import hotciv.standard.Interfaces.UnitAttacking;

import java.util.Objects;

public class ThetaCivUnitAction implements UnitAction {

    // create an instance of the generic unit attacing
    private UnitAttacking unitAttacking;

    //create a constructor that can sue the diffent attacking types if not generic
    public ThetaCivUnitAction(UnitAttacking unitAttacking){
        this.unitAttacking = unitAttacking; // allows for test stubs
    }
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
        // change the population size if the unit is a ufo
        if(Objects.equals(currentUnit.getTypeString(), "ufo")){
            // check to see if the position is on a city
            if(currentGame.cities.get(p) != null)
            {
                CityImpl currentCity = (CityImpl) currentGame.cities.get(p);
                // if the population is greater than 1, remove

                if(currentCity.getPopulationSize() > 1){
                    currentCity.setPopulationSize(currentCity.getPopulationSize() - 1);
                }
                else {
                    // remove the city
                    currentGame.cities.remove(p);
                }
            }
            else {
                // otherwise we don't have a city
                // if the position contains terrain of Forest then change to Plains otherwise nothing
                String terrainType = currentGame.tiles.get(p).getTypeString();
                if (Objects.equals(terrainType, "forest")) {
                    TileImpl updatedTile = new TileImpl(GameConstants.PLAINS);
                    currentGame.tiles.put(p, (Tile) updatedTile);
                }
            }
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
    @Override
    public boolean moveUnit( Position from, Position to, GameImpl game) {
        Unit unit_from = game.getUnitAt(from);
        // set the current unit in game
        game.setCurrentUnit(unit_from);
        // test the unit for the allowable moves
        UnitImpl ui = (UnitImpl) unit_from;
        int travelMoves = ui.getTravelDistace();
        if(travelMoves == 0){
            return false; // unit has no moves left
        }
            if (unit_from == null) {
                return false;
            }
            // get the current unit type once
            String unitTypeString = unit_from.getTypeString();
            boolean unitCanMove = ((UnitImpl) unit_from).getCanMove();
            // check to see if the unit to move is a fortified archer
            if (unitTypeString.equals("archer") && !unitCanMove)
                return false;
            else if (unitTypeString.equals("archer") && unitCanMove && game.getUnitAt(to) == null) {
                updateUnitMap(from, to, unit_from, game);
                // unit is moved, remove a travel distance
                ((UnitImpl) unit_from).setTravelDistace(travelMoves - 1);
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
                // check if the unit is an immobilized archer
                // get the unit impl of attacking unit
                UnitImpl attackingUnitImpl = (UnitImpl)attackingUnit;
                if(Objects.equals(attackingUnit.getTypeString(), "archer") && !attackingUnitImpl.getCanMove())
                {
                    return false;
                }
                if (defendingPlayer != attackingPlayer) {
                    // let the attacking unit remove the defending unit and then successfully move to that tile
                    // call the functions
                    if(unitAttacking.canAttackerBeatDefender((UnitImpl) attackingUnit, (UnitImpl) foundUnit, from, to, game)) {
                        game.killUnit(to);
                        updateUnitMap(from, to, unit_from, game);
                        // update the successful attack map
                        int currentSuccessfulAttacks = game.playerSuccessfulAttacks.get(attackingPlayer);
                        game.playerSuccessfulAttacks.put(attackingPlayer, currentSuccessfulAttacks + 1);
                        ((UnitImpl) unit_from).setTravelDistace(travelMoves - 1);

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
                        // remove the attacking unit
                        game.killUnit(from);
                        return false; // the defending unit overpowered the attacking unit
                    }

                }
                return false; // cannot fortify tiles (move own units to tile with own units)
            }
            // otherwise, move the unit from the original position to the new one
            updateUnitMap(from, to, unit_from, game);
            ((UnitImpl) unit_from).setTravelDistace(travelMoves - 1);
            // here we have no units in the to position - safe to move
            // check for city
            // if there is a city, transfer ownership of the city
            City currentCity = game.cities.get(to);
            if(currentCity != null){
                // transfer ownership
                if(unit_from.getTypeString() != GameConstants.UFO){
                    ((CityImpl) currentCity).setOwner(unit_from.getOwner());
                }
            }
        return true;
    }
}
