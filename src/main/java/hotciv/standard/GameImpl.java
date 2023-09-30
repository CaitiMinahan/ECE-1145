package hotciv.standard;
import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID; // TODO: had to add this to track units
import java.util.logging.*;


import hotciv.standard.*;

/** Skeleton implementation of HotCiv.

 This source code is from the book
 "Flexible, Reliable Software:
 Using Patterns and Agile Development"
 published 2010 by CRC Press.
 Author:
 Henrik B Christensen
 Department of Computer Science
 Aarhus University

 Please visit http://www.baerbak.com/ for further information.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

public class GameImpl implements Game {

  // TODO: step 2 - Refactor GameImpl tp use a reference WorldLayout instance for setting the tiles (Delegate)
  private WorldLayout worldLayoutStrategy;

  // Added in the private implementation for UnitAction
  // This is refactoring the non-existent unit action file and setting an action(Delegate) based on the type of civ
  private UnitAction unitActionCivType;

  // create current player to keep track of
  private Player currentPlayer;
  public Map<Position, Unit> units; // use a hash map to store the units on the board

  //  TODO: added this since we can't edit city interface
  private Map<Position, City> cities = new HashMap<>();

  private int age;
  // tracks the number of turns in a round (increments every time each player becomes the current player)
  private int turnCount;

  public CityImpl currentCity;

  // TODO: might need to keep track of current tile later
  public TileImpl currentTile;

  // GameImpl constructor
  public GameImpl(WorldLayout worldLayoutStrategy, UnitAction unitActionCivType){

    // TODO: step 3 - refactor GameImpl to use a concrete WorldLayout instance
    this.worldLayoutStrategy = worldLayoutStrategy;
    // assign the unit action type as the incoming parameter
    this.unitActionCivType = unitActionCivType;

    // initialize the game with the first player as RED
    currentPlayer = Player.RED;
    currentCity = new CityImpl(currentPlayer);

    // game starts at 4000 BC
    age = 4000;

    // initialize the turn count to 0
    turnCount = 0;

    // TODO: may need to later implement players as a list and index through the list to keep track of whose turn it is
    // use a HashMap uses key value pairs to store the positions of the units (positions = keys and units = values)
    units = new HashMap<>();

    // TODO: remove this implementation because now we have different world layouts for alpha, beta, gamma and delta
   // initialize units of RED and BLUE players (RES gets archer and settler and BLUE gets legion)
//    units.put(new Position(0,0), new UnitImpl(GameConstants.ARCHER, Player.RED));
//    units.put(new Position(1,1), new UnitImpl(GameConstants.SETTLER, Player.RED));
//    units.put(new Position(1,2), new UnitImpl(GameConstants.LEGION, Player.BLUE));

    // TODO: step 4 - call helper function to set up the world layout according to strategy passed
    setupWorldLayout(worldLayoutStrategy);
  }

  // TODO: step 4 - create helper function to set the map according to setupWorld method in WorldLayout interface
  public void setupWorldLayout(WorldLayout worldLayoutStrategy) {
    if (worldLayoutStrategy != null) {
      worldLayoutStrategy.setupWorld(this); // Pass the current game instance to the layout strategy
    }
  }

  // TODO: modified this
  public Unit getUnitAt( Position p ) {
    // make sure we never return a null unit in the map
//    Unit unit = units.get(p);
    // if the unit is not found in the map, return it
//    if (unit != null){
//      return unit;
//    }
    // let's initialize the archer will be placed at position tile (0,0) and the settler at (1,1) for the red player
//    if ((currentPlayer == Player.RED && (p.equals(new Position(0,0)) || p.equals(new Position(1,1))))){
//      if(p.equals(new Position(0,0))){
//        return new UnitImpl(GameConstants.ARCHER, Player.RED);
//      }
//      else{
//        return new UnitImpl(GameConstants.SETTLER, Player.RED);
//      }
//    }
//    // let's also initialize the legion placed at position tile (1,2) for the blue player
//    else if ((currentPlayer == Player.BLUE && (p.equals(new Position(1,2))))){
//      return new UnitImpl(GameConstants.LEGION, Player.BLUE);
//    }
//    return null;

    if (units.containsKey(p)) {
      return units.get(p);
    }
    return null;
  }

  // TODO: added a helper function to get the Position of a unit
  public Position getPositionFromUnit(UnitImpl u)
  {
    // loop through the units map and find the unit with the corresponding ID
    UUID tempId = u.getUnitID();
    for (Map.Entry<Position, Unit> entry: units.entrySet()) {
     // get the key (position)
     // get the unit (value)
      Position pos = entry.getKey();
      Unit unit = entry.getValue();
      UnitImpl ui = (UnitImpl)unit;
      if (ui.getUnitID() == u.getUnitID())
      {
        // this is the position we want
        return pos;
      }
    }
    // TODO: make not that we should never see this happen
    return new Position(-1,-1);
  }

  public Tile getTileAt( Position p ) {
    if ((p.getRow() == 1) && (p.getColumn() == 0)) {
      return new TileImpl("ocean");
    } else if (((p.getRow() == 0) && (p.getColumn() == 1))) {
      return new TileImpl("hills");
    } else if (((p.getRow() == 2) && (p.getColumn() == 2))) {
      return new TileImpl("mountain");
    }
    else {
      return new TileImpl("plains");
    }
  }

  // TODO: modified this
  public City getCityAt(Position p) {
    if (cities.containsKey(p)) {
      return cities.get(p);
    }
    return null;

  }

  public Player getWinner() {
    if(age == 3000){
      return Player.RED; // red player wins in 3000 BC
    }
    return null;
  }
  public int getAge() {
    return age;
  }
  public Player getPlayerInTurn() {
    return currentPlayer;
  }
  public void killUnit(Position positionToClear) { units.remove(positionToClear); }

  /**
   * @param unitToCheck
   * @return inversion of the check to settler type
   */
  public boolean canUnitAttack(Unit unitToCheck) {
    return !Objects.equals(unitToCheck.getTypeString(), "settler");
  }

  // Helper function to retrieve the unit action type and not change the template design
  UnitAction getUnitActionType()
  {
    return unitActionCivType;
  }
  // refactored this to use the different ActionType versions (Delegate)
  public boolean moveUnit(Position from, Position to) {
    // get the current unit action type
    UnitAction UnitActionCivType = getUnitActionType();
    // based on the type of game we are playing this will use the different implementations
    if(UnitActionCivType != null )
    {
      // run the move function
      return UnitActionCivType.moveUnit(from, to, this);
    }
    else
    {
      // for some reason the unitActionCivType is null when it should be generic or gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
    return false;
  }

  // TODO: when unit needs to take action, use this function
  public void takeUnitAction(Unit u, GameImpl game, UnitAction unitActionCivType) {
    // based on the type of game we are playing this will use the different implementations
    if(unitActionCivType != null )
    {
      // get the position based on the unit
      // convert unit to unit impl
      UnitImpl ui = (UnitImpl) u;
      Position p = getPositionFromUnit(ui);
      // run the action function
      unitActionCivType.performAction( ui,  p, game);
    }
    else
    {
      // for some reason the unitActionCivType is null when it should be generic or gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
  }
  public void endOfTurn() {

    // add 6 production (or money) at the end of the turn
    currentCity.setTreasury(currentCity.getTreasury()+6);

    // later on, we can include all players (after blue, yellow goes, etc.)
    // switch players when it's turn ends
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;

    // increment the turn count after every player goes
    turnCount++;

    // check if round is over
    // if both players have gone, the turnCount should = 2, therefore we can move onto the next round
    // TODO: the turnCount will be 4 after all players go (RED, BLUE, YELLOW and GREEN)
    if (turnCount % 2 == 0){
      // age by 100 years after the round ends
      age -= 100;
    }
  }

  public void changeWorkForceFocusInCityAt( Position p, String balance ) {}
  public void changeProductionInCityAt( Position p, String unitType ) {}
  public void performUnitActionAt( Position p ) {}

  // TODO: step 4 - added helper function for adding new cities for DeltaCiv
  public void placeCity(Position position, Player player) {
    // Check if a city already exists at the specified position
    City existingCity = cities.get(position);

    // If there is no existing city at the position, create a new one and set its owner
    if (existingCity == null) {
      City newCity = new CityImpl(player);
      // Add the city to the collection
      cities.put(position, newCity);
      currentCity = (CityImpl) newCity;  // TODO: modified this
    }
  }
}