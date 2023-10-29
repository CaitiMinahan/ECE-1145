package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* The following code is a copy of gamma civ, but now we need
 * to also track the successful attacks that the player has won
 * */


public class EpsilonCivUnitAction implements UnitAction{

    // create an instance of the generic Unit attacking
    private UnitAttacking unitAttacking;

    // create a constructor that can use the different unitAttacking types
    public EpsilonCivUnitAction(UnitAttacking unitAttacking){
        this.unitAttacking = unitAttacking; // this is important for testing
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
        else if (unitTypeString.equals("archer") && unitCanMove && game.getUnitAt(to) == null)
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
                // TODO: have to go through the attacking a defensive calculations before killing a unit

                // call the functions
                if(unitAttacking.canAttackerBeatDefender((UnitImpl) attackingUnit, (UnitImpl) foundUnit, from, to, game)) {
                    game.killUnit(to);
                    updateUnitMap(from, to, unit_from, game);
                    // update the successful attack map
                    int currentSuccessfulAttacks = game.playerSuccessfulAttacks.get(attackingPlayer);
                    game.playerSuccessfulAttacks.put(attackingPlayer, currentSuccessfulAttacks + 1);
                    return true;
                }
                else
                {
                    return false; //defender won
                }
            }
            return false; // cannot fortify tiles (move own units to tile with own units)
        }
        // otherwise, move the unit from the original position to the new one
        updateUnitMap(from, to, unit_from, game);
        return true;
    }

    // TODO: All 4 of these functions would be good to unit test.
    // TODO: extend these functions into another file that can be used for stubbing

    // function to compare the defensive and attacking strength of two units.
//    {
//        public boolean canAttackerBeatDefender (UnitImpl attacker, UnitImpl defender, Position from, Position
//        to, GameImpl game){
//        // get attacking strength and defensive strength
//        // scale by terrain
//        int attackStrength = getAttackingUnitStrength(attacker, from, game) * getTerrainMultiplier(attacker);
//        int defendStrength = getDefendingUnitStrength(defender, to, game) * getTerrainMultiplier(defender);
//        return attackStrength > defendStrength;
//    }
//
//        // function to get the attacking strength + terrain bonus
//        public int getAttackingUnitStrength (UnitImpl attacker, Position from, GameImpl game){
//        // look at the surrounding 8 tiles and get the unit's attacking strength as well as the friendly count
//        int numSupporters = getNumFriendlyTiles(from, game);
//        return attacker.getAttackingStrength() + numSupporters;
//    }
//
//        // function to get the defensive strength + terrain bonus
//        public int getDefendingUnitStrength (UnitImpl defender, Position to, GameImpl game){
//        // look at the surrounding 8 tiles and get the unit's defending strength as well as the friendly count
//        int numSupporters = getNumFriendlyTiles(to, game);
//        return defender.getDefensiveStrength() + numSupporters;
//    }
//
//        // function to get the terrain bonus
//        public int getTerrainMultiplier (UnitImpl unit){
//        String terrainType = unit.getTypeString();
//        switch (terrainType) {
//            case "forest":
//            case "hill":
//                return 2;
//            case "city":
//                return 3;
//            default:
//                return -1;
//        }
//    }
//
//        public int getNumFriendlyTiles (Position from, GameImpl game){
//        // want the logic from the Utility code provided
//        Iterable<Position> neighborIterator = get8neighborhoodOf(from);
//        int countFriendlyUnits = 0;
//        Player attackingPlayer = game.getUnitAt(from).getOwner();
//        for (Position pos : neighborIterator) {
//            Player owner = game.getUnitAt(pos).getOwner();
//            if (attackingPlayer == owner)
//                countFriendlyUnits++;
//        }
//        return countFriendlyUnits;
//    }
//        public static Iterator<Position> get8neighborhoodIterator (Position center){
//        List<Position> list = new ArrayList<>();
//        // Define the 'delta' to add to the row for the 8 positions
//        int[] rowDelta = new int[]{-1, -1, 0, +1, +1, +1, 0, -1};
//        // Define the 'delta' to add to the colum for the 8 positions
//        int[] columnDelta = new int[]{0, +1, +1, +1, 0, -1, -1, -1};
//
//        for (int index = 0; index < rowDelta.length; index++) {
//            int row = center.getRow() + rowDelta[index];
//            int col = center.getColumn() + columnDelta[index];
//            if (row >= 0 && col >= 0
//                    && row < GameConstants.WORLDSIZE
//                    && col < GameConstants.WORLDSIZE)
//                list.add(new Position(row, col));
//        }
//        return list.iterator();
//    }
//
//        public static Iterable<Position> get8neighborhoodOf (Position center){
//        final Iterator<Position> iterator = get8neighborhoodIterator(center);
//        Iterable<Position> iterable = new Iterable<Position>() {
//            @Override
//            public Iterator<Position> iterator() {
//                return iterator;
//            }
//        };
//        return iterable;
//    }
//    }
}