package hotciv.standard.Interfaces;

import hotciv.framework.Player;
import hotciv.framework.Unit;

import java.util.Objects;
import java.util.UUID;

public interface MutableUnit extends Unit {
    int travelDistace = 1;
    int productionCost = 0;

    boolean canMove = true;
    void setDefendingStrength(int d);
    void setAttackingStrength(int a);
    // TODO: had to add toggles for can move property, get and set
    void setTravelDistace(int t);
    int getTravelDistace();
    int getProductionCost();
    void setProductionCost(int cost);
    boolean getCanMove();
    void setCanMove (boolean can_move);
    // TODO needed to add this to get unique IDs
    UUID getUnitID();
}
