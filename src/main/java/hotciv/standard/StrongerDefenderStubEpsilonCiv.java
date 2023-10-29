package hotciv.standard;
import hotciv.framework.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class StrongerDefenderStubEpsilonCiv implements UnitAttacking{
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
        // return a small strength
        return 1;
    }

    @Override
    public int getDefendingUnitStrength(UnitImpl defender, Position to, GameImpl game) {
        // return a strong defense
        return 1000;
    }

    @Override
    // Doesn't matter, not being tested
    public int getTerrainMultiplier(UnitImpl unit) {
        return -1;
    }

    @Override
    // Doesn't matter, not being tested
    public int getNumFriendlyTiles(Position from, GameImpl game) {
        return -1;
    }
}
