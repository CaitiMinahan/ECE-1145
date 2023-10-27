package hotciv.standard;

import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID; // TODO: had to add this to track units
import java.util.logging.*;

import hotciv.standard.WorldAging;
import hotciv.standard.*;

/**
 * Skeleton implementation of HotCiv.
 * 
 * This source code is from the book
 * "Flexible, Reliable Software:
 * Using Patterns and Agile Development"
 * published 2010 by CRC Press.
 * Author:
 * Henrik B Christensen
 * Department of Computer Science
 * Aarhus University
 * 
 * Please visit http://www.baerbak.com/ for further information.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

public class GameImpl implements Game {

  // Refactor GameImpl tp use a reference WorldLayout instance for
  // setting the tiles (Delegate)
  private WorldLayout worldLayoutStrategy;
  private WorldAging worldAgingStrategy;
  private Winner winnerStrategy;

  // Added in the private implementation for UnitAction
  // This is refactoring the non-existent unit action file and setting an
  // action(Delegate) based on the type of civ
  private UnitAction unitActionCivType;

  // TODO: ask if we still need those private variables (above) after refactoring for abstract factory
  // step 3 in refactoring for abstract factory: modify GameImpl constructor to accept GameFactory as its parameter
  private GameFactory gameFactory;

  // create current player to keep track of
  private Player currentPlayer;
  public Map<Position, Unit> units; // use a hash map to store the units on the board

  public Map<Position, City> cities = new HashMap<>();

  private int age; // represents current year of the game

  // tracks the number of turns in a round (increments every time each player becomes the current player)
  private int turnCount;

  public CityImpl currentCity;

  // TODO: might need to keep track of current tile later
  public TileImpl currentTile;

  // TODO: remove old implementation
  // GameImpl constructor
//  public GameImpl(WorldLayout worldLayoutStrategy, WorldAging worldAging, Winner winnerStrategy,
//      UnitAction unitActionCivType) {
  public GameImpl(GameFactory gameFactory) {

    // TODO: remove old implementation
    // refactor GameImpl to use a concrete WorldLayout instance
//    this.worldLayoutStrategy = worldLayoutStrategy;
//    this.worldAgingStrategy = worldAging;
//    this.winnerStrategy = winnerStrategy;
//    // assign the unit action type as the incoming parameter
//    this.unitActionCivType = unitActionCivType;

    // use the factory to create the appropriate strategies for the following variant behaviors:
    this.worldLayoutStrategy = gameFactory.createWorldLayout();
    this.worldAgingStrategy = gameFactory.createWorldAging();
    this.winnerStrategy = gameFactory.createWinnerStrategy();
    this.unitActionCivType = gameFactory.createUnitAction();

    // initialize the game with the first player as RED
    currentPlayer = Player.RED;
    currentCity = new CityImpl(currentPlayer);

    // game starts at 4000 BC
    setAge(-4000);
    // initialize the turn count to 0
    setTurnCount(0);

    // TODO: may need to later implement players as a list and index through the
    // list to keep track of whose turn it is
    // use a HashMap uses key value pairs to store the positions of the units
    units = new HashMap<>();

    // call helper function to set up the world layout according to
    // strategy passed
    setupWorldLayout(worldLayoutStrategy);
  }

  // create helper function to set the map according to setupWorld
  // method in WorldLayout interface
  public void setupWorldLayout(WorldLayout worldLayoutStrategy) {
    if (worldLayoutStrategy != null) {
      worldLayoutStrategy.setupWorld(this); // Pass the current game instance to the layout strategy
    }
  }

  public Unit getUnitAt(Position p) {
    if (units.containsKey(p)) {
      return units.get(p);
    }
    return null;
  }

  public Position getPositionFromUnit(UnitImpl u) {
    // loop through the units map and find the unit with the corresponding ID
    UUID tempId = u.getUnitID();
    for (Map.Entry<Position, Unit> entry : units.entrySet()) {
      // get the key (position)
      // get the unit (value)
      Position pos = entry.getKey();
      Unit unit = entry.getValue();
      UnitImpl ui = (UnitImpl) unit;
      if (ui.getUnitID() == u.getUnitID()) {
        // this is the position we want
        return pos;
      }
    }
    return new Position(-1, -1);
  }

  public Tile getTileAt(Position p) {
    if ((p.getRow() == 1) && (p.getColumn() == 0)) {
      return new TileImpl("ocean");
    } else if (((p.getRow() == 0) && (p.getColumn() == 1))) {
      return new TileImpl("hills");
    } else if (((p.getRow() == 2) && (p.getColumn() == 2))) {
      return new TileImpl("mountain");
    } else {
      return new TileImpl("plains");
    }
  }

  public City getCityAt(Position p) {
    if (cities.containsKey(p)) {
      return cities.get(p);
    }
    return null;
  }

  public Player getWinner() {
    return winnerStrategy.gameWinner(this);
  }

  public int getAge() {
    return age;
  }

  public Player getPlayerInTurn() {
    return currentPlayer;
  }

  public void killUnit(Position positionToClear) {
    units.remove(positionToClear);
  }

  /**
   * @param unitToCheck
   * @return inversion of the check to settler type
   */
  public boolean canUnitAttack(Unit unitToCheck) {
    return !Objects.equals(unitToCheck.getTypeString(), "settler");
  }

  // Helper function to retrieve the unit action type and not change the template design
  String getUnitActionStringType() {
    if(unitActionCivType instanceof GammaCivUnitAction)
    {
      return "GammaCivUnitAction";
    }
    else if(unitActionCivType instanceof GenericUnitAction)
    {
      return "GenericUnitAction";
    }
    return "invalid class type";
  }

  // refactored this to use the different ActionType versions (Delegate)
  public boolean moveUnit(Position from, Position to) {
    // get the current unit action type
    UnitAction UnitActionCivType = unitActionCivType; // from constructor/priv variables
    // based on the type of game we are playing this will use the different
    // implementations
    if (UnitActionCivType != null) {
      // run the move function
      return UnitActionCivType.moveUnit(from, to, this);
    } else {
      // for some reason the unitActionCivType is null when it should be generic or
      // gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");

    }
    return false;
  }


  // when unit needs to take action, use this function
  public void takeUnitAction(Unit u) {
    // based on the type of game we are playing this will use the different
    // implementations
    if (this.unitActionCivType != null) {
      // get the position based on the unit
      // convert unit to unit impl
      UnitImpl ui = (UnitImpl) u;
      Position p = getPositionFromUnit(ui);
      // run the action function
      this.unitActionCivType.performAction(ui, p, this);
    } else {
      // for some reason the unitActionCivType is null when it should be generic or
      // gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
  }

  public void endOfTurn() {

    // add 6 production (or money) at the end of the turn
    currentCity.setTreasury(currentCity.getTreasury() + 6);

    // later on, we can include all players (after blue, yellow goes, etc.)
    // switch players when it's turn ends
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;

    // increment the turn count after every player goes
    setTurnCount(getTurnCount() + 1);

    worldAgingStrategy.gameAging(this);

  }

  public void changeWorkForceFocusInCityAt(Position p, String balance) {
  }

  public void changeProductionInCityAt(Position p, String unitType) {
  }

  public void performUnitActionAt(Position p) {
  }

  public void placeCity(Position position, Player player) {
    if (!cityExistsAt(position)) {
      City newCity = createCity(player);
      cities.put(position, newCity);
      setCurrentCity(newCity);
    }
  }

  private boolean cityExistsAt(Position position) {
    return cities.containsKey(position);
  }

  private City createCity(Player player) {
    return new CityImpl(player);
  }

  private void setCurrentCity(City city) {
    currentCity = (CityImpl) city;
  }

  // HELPER FUNCTIONS FOR BETACIV
  public void setTurnCount(int turnCount) {
    this.turnCount = turnCount;
  }

  public int getTurnCount() {
    return turnCount;
  }

  public void setAge(int age) {
    this.age = age;
  }
}