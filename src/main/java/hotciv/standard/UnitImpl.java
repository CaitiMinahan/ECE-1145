package hotciv.standard;
import hotciv.framework.Player;
import hotciv.framework.Unit;
import java.util.UUID;
import hotciv.standard.*;

public class UnitImpl implements Unit {
    private String unit;
    private Player owner;

    // TODO: added in ID to track specific units in the hashmap
    private UUID ID;
    private int moveCount;
    private int defensiveStrength;
    private int attackingStrength;

    private boolean canMove = true;
    public UnitImpl(String unit, Player owner){
        this.unit = unit;
        this.owner = owner;
        this.ID = UUID.randomUUID();
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

    // TODO: had to add a setter for the unit action to work
    void setDefendingStrength(int d) {
        defensiveStrength = d;
    }
    // TODO: had to add toggles for can move property, get and set
    public boolean getCanMove() { return canMove;}
    void setCanMove (boolean can_move) {
        canMove = can_move;
    }
    // TODO needed to add this to get unique IDs
    public UUID getUnitID() { return ID;}
}