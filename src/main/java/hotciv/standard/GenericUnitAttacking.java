package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.UnitAttacking;

public class GenericUnitAttacking implements UnitAttacking {
    @Override
    public boolean canAttackerBeatDefender(UnitImpl attacker, UnitImpl defender, Position from, Position to, MutableGame game) {
        // get attacking strength and defensive strength
        // scale by terrain
        int attackStrength = getAttackingUnitStrength(attacker, from, game) * getTerrainMultiplier(attacker);
        int defendStrength = getDefendingUnitStrength(defender, to, game) * getTerrainMultiplier(defender);
        return attackStrength > defendStrength;
    }

    @Override
    public int getAttackingUnitStrength(UnitImpl attacker, Position from, MutableGame game) {
        // look at the surrounding 8 tiles and get the unit's attacking strength as well as the friendly count
        int numSupporters = getNumFriendlyTiles(from, game);
        return attacker.getAttackingStrength() + numSupporters;
    }

    @Override
    public int getDefendingUnitStrength(UnitImpl defender, Position to, MutableGame game) {
        // look at the surrounding 8 tiles and get the unit's defending strength as well as the friendly count
        int numSupporters = getNumFriendlyTiles(to, game);
        return defender.getDefensiveStrength() + numSupporters;
    }

    @Override
    public int getTerrainMultiplier(UnitImpl unit) {
        String terrainType = unit.getTypeString();
        switch(terrainType) {
            case "forest":
            case "hill":
                return 2;
            case "city":
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public int getNumFriendlyTiles(Position from, MutableGame game) {
        // want the logic from the Utility code provided
        Iterable<Position> neighborIterator = UnitAttacking.get8neighborhoodOf(from);
        int countFriendlyUnits = 0;
        Player attackingPlayer = game.getUnitAt(from).getOwner();
        for (Position pos: neighborIterator) {
            Player owner = game.getUnitAt(pos).getOwner();
            if(attackingPlayer == owner)
                countFriendlyUnits++;
        }
        return countFriendlyUnits;
    }
}
