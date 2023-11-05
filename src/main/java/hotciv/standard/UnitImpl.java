package hotciv.standard;
import hotciv.framework.Player;
import hotciv.framework.Unit;

import java.util.Objects;
import java.util.UUID;
import hotciv.standard.*;

public class UnitImpl implements Unit {
    private String unit;
    private Player owner;
    private UUID ID;
    private int moveCount;
    private int defensiveStrength;
    private int attackingStrength;
    private int travelDistace = 1;
    private int productionCost = 0;

    private boolean canMove = true;
    public UnitImpl(String unit, Player owner){
        this.unit = unit;
        this.owner = owner;
        this.ID = UUID.randomUUID();

        // if the unit is a UFO
        if(Objects.equals(this.getTypeString(), "ufo")){
            setDefendingStrength(8);
            setAttackingStrength(1);
            setTravelDistace(2);
            setProductionCost(60); // UFO cost 60
        }
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
    void setAttackingStrength(int a) {attackingStrength = a;}
    // TODO: had to add toggles for can move property, get and set
    public void setTravelDistace(int t) {travelDistace = t;}
    public int getTravelDistace() { return travelDistace;}
    public int getProductionCost() { return productionCost;}
    public void setProductionCost(int cost) { productionCost = cost;}
    public boolean getCanMove() { return canMove;}
    void setCanMove (boolean can_move) {
        canMove = can_move;
    }
    // TODO needed to add this to get unique IDs
    public UUID getUnitID() { return ID;}
}