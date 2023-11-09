package hotciv.standard.Interfaces;

import hotciv.framework.Game;
import hotciv.framework.*;
import hotciv.standard.UnitImpl;

public interface MutableGame extends Game {
    public void setupWorldLayout(WorldLayout worldLayout);
    public Unit getCurrentUnit();
    public void setCurrentUnit(Unit u);
    public Player getCurrentPlayer();
    public Position getPositionFromUnit(UnitImpl u);
    public void killUnit(Position positionToClear);
    public boolean canUnitAttack(Unit unitToCheck);
    String getUnitActionStringType();
    public void placeCity(Position position, Player player);
    public boolean cityExistsAt(Position position);
    public City createCity(Player player);
    public void setCurrentCity(City city);
    public void setTurnCount(int turnCount);
    public int getTurnCount();
    public void setAge(int age);
}
