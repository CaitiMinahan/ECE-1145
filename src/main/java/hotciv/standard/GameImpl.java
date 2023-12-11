package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.Interfaces.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

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

public class GameImpl implements MutableGame {
  private WorldLayout worldLayoutStrategy;
  private WorldAging worldAgingStrategy;
  private Winner winnerStrategy;
  private UnitAction unitActionCivType;
  private PlayerSetup playerSetup;
  private ChangeProduction productionStrategy;
  private Player currentPlayer;
  private int age; // represents current year of the game
  // tracks the number of turns in a round (increments every time each player becomes the current player)
  private int turnCount;
  public CityImpl currentCity;
  private MutableUnit currentUnit;
  public TileImpl currentTile;

  // adding this to implement GameObserver
  private List<GameObserver> observers = new ArrayList<>();

  public GameImpl(GameFactory gameFactory) {
    // use the factory to create the appropriate strategies for the following variant behaviors:
    this.worldLayoutStrategy = gameFactory.createWorldLayout();
    this.worldAgingStrategy = gameFactory.createWorldAging();
    this.winnerStrategy = gameFactory.createWinnerStrategy();
    this.unitActionCivType = gameFactory.createUnitAction();
    this.playerSetup = gameFactory.createPlayerSetup();
    this.productionStrategy = gameFactory.changeProduction();

    // initialize the game with the first player as RED
    currentPlayer = Player.RED;
    currentCity = new CityImpl(currentPlayer);

    // game starts at 4000 BC
    setAge(-4000);
    // initialize the turn count to 0
    setTurnCount(0);

    // call helper function to set up the world layout according to
    // strategy passed
    setupWorldLayout(worldLayoutStrategy);
    // set up the player based on the hash map
    playerSetup.setupPlayer(this);
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

  // Getter and setter for the current Unit variable
  public MutableUnit getCurrentUnit(){ return currentUnit; }
  public void setCurrentUnit (MutableUnit u) { currentUnit = u; }

  // Getter for the current player
  public Player getCurrentPlayer() { return currentPlayer;}

  public Position getPositionFromUnit(MutableUnit u) {
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
  public String getUnitActionStringType() {
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

  // modified moveUnit to support the observer behavior
  public boolean moveUnit(Position from, Position to) {
    // get the current unit action type
    UnitAction UnitActionCivType = unitActionCivType; // from constructor/priv variables

    // Debug print to check the value of unitActionCivType
//    System.out.println("UnitActionCivType: " + UnitActionCivType);

    // Check if the unit can actually move
    if (unitActionCivType != null && unitActionCivType.moveUnit(from, to, this)) {
      // Notify observers about the world change
      for (GameObserver observer : observers) {
        observer.worldChangedAt(to);
      }
      return true; // Indicate successful move
    } else {
      // for some reason the unitActionCivType is null when it should be generic or
      // gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
    return false;
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

    // reset the current units move counter back to 1 or 2

    Unit currUnit = getCurrentUnit();
    // if the current unit was set
    if (currUnit != null ){
      if(Objects.equals(currUnit.getTypeString(), "ufo")){
        ((UnitImpl) currUnit).setTravelDistace(2);
      } else {
        ((UnitImpl) currUnit).setTravelDistace(1);
      }
    }

    // added this for implementing the obersver pattern
    // Notify observers about the end of turn
    for (GameObserver observer : observers) {
      observer.turnEnds(getPlayerInTurn(), getAge());
    }
  }

  public void changeWorkForceFocusInCityAt(Position p, String balance) {
  }

  // @TODO need to implement this with the UFO
  public void changeProductionInCityAt(Position p, String unitType) {
    productionStrategy.changeProduction(p, unitType, this);
  }

  // TODO: make sure all function calls to take Unit Action are replaced with perform unit action
  public void performUnitActionAt(Position p) {
    Unit u = getUnitAt(p);
    // based on the type of game we are playing this will use the different
    // implementations
    if (this.unitActionCivType != null) {
      // get the position based on the unit
      // convert unit to mutable unit
      MutableUnit mu = (MutableUnit) u;
      // run the action function
      this.unitActionCivType.performAction(mu, p, this);
    } else {
      // for some reason the unitActionCivType is null when it should be generic or
      // gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
  }

  @Override
  public void addObserver(GameObserver observer) {
    observers.add(observer);
  }

  @Override
  public void setTileFocus(Position position) {
    // Implement logic to set tile focus
    // Notify observers about the tile focus change
    for (GameObserver observer : observers) {
      observer.tileFocusChangedAt(position);
    }
  }

  public void placeCity(Position position, Player player) {
    if (!cityExistsAt(position)) {
      City newCity = createCity(player);
      cities.put(position, newCity);
      setCurrentCity(newCity);
    }
  }

  public boolean cityExistsAt(Position position) {
    return cities.containsKey(position);
  }

  public City createCity(Player player) {
    return new CityImpl(player);
  }

  public void setCurrentCity(City city) {
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