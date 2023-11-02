package hotciv.standard.TestStubs;
import hotciv.framework.*;
import hotciv.standard.GameImpl;
import hotciv.standard.Interfaces.UnitAttacking;
import hotciv.standard.UnitImpl;

public class StrongerAttackerStubEpsilonCiv implements UnitAttacking {
    @Override
    // Same as before in the generic
    public boolean canAttackerBeatDefender(UnitImpl attacker, UnitImpl defender, Position from, Position to, GameImpl game) {
        // get attacking strength and defensive strength
        // scale by terrain
        int attackStrength = getAttackingUnitStrength(attacker, from, game) * getTerrainMultiplier(attacker);
        int defendStrength = getDefendingUnitStrength(defender, to, game) * getTerrainMultiplier(defender);
        return attackStrength > defendStrength;
    }

    @Override
    // Stub -- Overwrite to be really strong
    public int getAttackingUnitStrength(UnitImpl attacker, Position from, GameImpl game) {
        // return a large strength
        return 1000;
    }

    @Override
    public int getDefendingUnitStrength(UnitImpl defender, Position to, GameImpl game) {
        // return a weak defense
        return 1;
    }

    @Override
    // Doesn't matter, not being tested
    public int getTerrainMultiplier(UnitImpl unit) {
        return 1;
    }

    @Override
    // Doesn't matter, not being tested
    public int getNumFriendlyTiles(Position from, GameImpl game) {
        return -1;
    }
}
