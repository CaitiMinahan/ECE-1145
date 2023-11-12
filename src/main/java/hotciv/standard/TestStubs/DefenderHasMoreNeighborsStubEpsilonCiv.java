package hotciv.standard.TestStubs;
        import hotciv.framework.*;
        import hotciv.standard.GameImpl;
        import hotciv.standard.Interfaces.MutableGame;
        import hotciv.standard.Interfaces.UnitAttacking;
        import hotciv.standard.UnitImpl;

public class DefenderHasMoreNeighborsStubEpsilonCiv implements UnitAttacking {
    @Override
    // Same as before in the generic
    public boolean canAttackerBeatDefender(UnitImpl attacker, UnitImpl defender, Position from, Position to, MutableGame game) {
        // get attacking strength and defensive strength
        // scale by terrain
        int attackStrength = getAttackingUnitStrength(attacker, from, game) * getTerrainMultiplier(attacker);
        int defendStrength = getDefendingUnitStrength(defender, to, game) * getTerrainMultiplier(defender);
        return attackStrength > defendStrength;
    }

    @Override
    // Stub
    public int getAttackingUnitStrength(UnitImpl attacker, Position from, MutableGame game) {
        // return a large strength
        int terrainAdv = getTerrainMultiplier(attacker);
        int neighborAdvantage = getNumFriendlyTiles(from, game); // disadvantage
        return neighborAdvantage + terrainAdv;
    }

    @Override
    public int getDefendingUnitStrength(UnitImpl defender, Position to, MutableGame game) {
        // return a weak defense
        int terrainAdv = getTerrainMultiplier(defender);
        int neighborAdvantage = getNumFriendlyTiles(to, game) * 10; // advantage
        return neighborAdvantage + terrainAdv;
    }

    @Override
    // Doesn't matter, not being tested
    public int getTerrainMultiplier(UnitImpl unit) {
        return 1;
    }

    @Override
    // Doesn't matter, not being tested
    public int getNumFriendlyTiles(Position from, MutableGame game) {
        return 3;
    }
}
