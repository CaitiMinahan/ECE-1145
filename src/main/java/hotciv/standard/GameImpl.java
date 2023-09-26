package hotciv.standard;
import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

  // TODO: Refactor GameImpl tp use a reference WorldLayout instance for setting the tiles (Delegate)
  private WorldLayout worldLayoutStrategy;

  // create current player to keep track of
  private Player currentPlayer;
  public Map<Position, Unit> units; // use a hash map to store the units on the board

  //  TODO: added this since we can't edit city interface
  private Map<Position, Player> cityOwners = new HashMap<>();

  private int age;
  // tracks the number of turns in a round (increments every time each player becomes the current player)
  private int turnCount;

  public CityImpl currentCity;

  // TODO: might need to keep track of current tile later
  public TileImpl currentTile;

  // GameImpl constructor
  public GameImpl(WorldLayout worldLayoutStrategy){

    // TODO: step 3 - refactor GameImpl to use a concrete WorldLayout instance
    this.worldLayoutStrategy = worldLayoutStrategy;

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

  public Unit getUnitAt( Position p ) {
    // make sure we never return a null unit in the map
    Unit unit = units.get(p);
    // if the unit is not found in the map, return it
    if (unit != null){
      return unit;
    }
    // let's initialize the archer will be placed at position tile (0,0) and the settler at (1,1) for the red player
    if ((currentPlayer == Player.RED && (p.equals(new Position(0,0)) || p.equals(new Position(1,1))))){
      if(p.equals(new Position(0,0))){
        return new UnitImpl(GameConstants.ARCHER, Player.RED);
      }
      else{
        return new UnitImpl(GameConstants.SETTLER, Player.RED);
      }
    }
    // let's also initialize the legion placed at position tile (1,2) for the blue player
    else if ((currentPlayer == Player.BLUE && (p.equals(new Position(1,2))))){
      return new UnitImpl(GameConstants.LEGION, Player.BLUE);
    }
    return null;
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
    // Check if the position exists in the cityOwners map
    if (cityOwners.containsKey(p)) {
      Player owner = cityOwners.get(p);
      // Create and return a city with the correct owner
      return new CityImpl(owner);
    }
    // Return null if no city is found at the position
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

  public boolean canUnitAttack(Unit unitToCheck) {
    return !Objects.equals(unitToCheck.getTypeString(), "settler");
  }
  public boolean moveUnit( Position from, Position to ) {
    // try to move unit and return true if nothing is there
    // then place the unit at the desired position
    // check for unit at 'from' position
    Unit unit_from = getUnitAt(from);
    boolean isAttacking = false;

    // if there's no unit at the 'from' position (aka, there's nothing to move)
    if (unit_from == null){
      return false;
    }
    // if the 'to' unit already has a unit there
    if (getUnitAt(to) != null) {
      // check to see if the current player occupies this unit or an enemy does
      Unit foundUnit = getUnitAt(to);
      Unit attackingUnit = getUnitAt(from);
      Player defendingPlayer = foundUnit.getOwner();
      Player attackingPlayer = attackingUnit.getOwner();

      // check if the attacking unit is capable of attacking
      // remove this statement to show case breaking
      if (!canUnitAttack(attackingUnit)) {
        return false;
      }

      if(defendingPlayer != attackingPlayer)
      {
        // let the attacking unit remove the defending unit and then successfully move to that tile
        killUnit(to);
        // update the destination tile with unit
        units.remove(from);
        units.put(to, unit_from);
        return true;
      }
      return false; // cannot fortify tiles (move own units to tile with own units)
    }
    // otherwise, move the unit from the original position to the new one
    units.remove(from);
    units.put(to, unit_from);
    return true;
  }
  public void endOfTurn() {
    // create a city with size (population = 1
    // add 6 production (or money) at the end of the turn
    currentCity.setTreasury(currentCity.getTreasury()+6);
    // later on, we can include all players (after blue, yellow goes, etc.)
    // switch players when it's the other's turn
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
    City existingCity = getCityAt(position);

    // If there is no existing city at the position, create a new one and set its owner
    if (existingCity == null) {
      City newCity = new CityImpl(player);
      // Add the city's position and owner to the map
      cityOwners.put(position, player);
    }
  }
}