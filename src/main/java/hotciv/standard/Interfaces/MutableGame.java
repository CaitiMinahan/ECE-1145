package hotciv.standard.Interfaces;

import hotciv.framework.Game;
import hotciv.framework.*;
import hotciv.standard.UnitImpl;

import java.util.HashMap;
import java.util.Map;

public interface MutableGame extends Game {

    Map<Position, Unit> units = new HashMap<>(); // use a hash map to store the units on the board
    Map<Position, Tile> tiles = new HashMap<>(); // using a hashmap to store tiles with positions

    Map<Position, City> cities = new HashMap<>();
    Map<Player, Integer> playerSuccessfulAttacks = new HashMap<>(); // tracks the players wins in attacking
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
