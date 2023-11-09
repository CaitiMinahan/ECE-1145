package hotciv.standard.Interfaces;

import hotciv.framework.Game;
import hotciv.framework.*;
import hotciv.standard.UnitImpl;

public interface MutableGame extends Game {
    void setupWorldLayout(WorldLayout worldLayout);
    Unit getCurrentUnit();
    void setCurrentUnit(Unit u);
    Player getCurrentPlayer();
    Position getPositionFromUnit(UnitImpl u);
    void killUnit(Position positionToClear);
    boolean canUnitAttack(Unit unitToCheck);
    String getUnitActionStringType();
    void placeCity(Position position, Player player);
    boolean cityExistsAt(Position position);
    City createCity(Player player);
    void setCurrentCity(City city);
    void setTurnCount(int turnCount);
    int getTurnCount();
    void setAge(int age);
}
