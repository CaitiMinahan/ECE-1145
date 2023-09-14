package hotciv.standard;
import hotciv.framework.Player;
import hotciv.framework.Unit;
import hotciv.standard.*;

public class UnitImpl implements Unit {
    private String unit;
    private Player owner;
    private int moveCount;
    private int defensiveStrength;
    private int attackingStrength;
    public UnitImpl(String unit, Player owner){
        this.unit = unit;
        this.owner = owner;
    }
    @Override
    public String getTypeString() {
        return unit;
    }
    @Override
    public Player getOwner() {
        return owner;
    }
    @Override
    public int getMoveCount() {
        return moveCount;
    }  // TODO: should MoveCount deal with rounds ending?
    @Override
    public int getDefensiveStrength() {
        return defensiveStrength;
    }
    @Override
    public int getAttackingStrength() {
        return attackingStrength;
    }
}