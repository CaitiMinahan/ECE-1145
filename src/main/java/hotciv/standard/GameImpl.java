package hotciv.standard;

import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;

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
  // create current player to keep track of
  private Player currentPlayer;
  private Map<Position, Unit> units; // use a hash map to store the units on the board
  private int age;
  // tracks the number of turns in a round (increments every time each player becomes the current player)
  private int turnCount;


  // GameImpl constructor
  public GameImpl(){
    // initialize the game with the first player as RED

    currentPlayer = Player.RED;
    CityImpl.treasury = 0;

    // game starts at 4000 BC
    age = 4000;
    // initialize the turn count to 0
    turnCount = 0;
    // TODO: may need to later implement players as a list and index through the list to keep track of whose turn it is
    // use a HashMap uses key value pairs to store the positions of the units (positions = keys and units = values)
    units = new HashMap<>();
    // initialize units of RED and BLUE players (RES gets archer and settler and BLUE gets legion)
    units.put(new Position(0,0), new UnitImpl(GameConstants.ARCHER, Player.RED));
    units.put(new Position(1,1), new UnitImpl(GameConstants.SETTLER, Player.RED));
    units.put(new Position(1,2), new UnitImpl(GameConstants.LEGION, Player.BLUE));

  }
  private class UnitImpl implements Unit {
    private String unit;
    private Player owner;
    private int moveCount;
    private int defensiveStrength;
    private int attackingStrength;
    public UnitImpl(String unit, Player owner){
      this.unit = unit;
      this.owner = owner;
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
  }
  private class TileImpl implements Tile {
    private String terrain;
    public TileImpl(String terrain){
      this.terrain = terrain;
    }
    @Override
    public String getTypeString() {
      return terrain;
    }
  }
  public static class CityImpl implements City {
    private int size;
    private Player owner;

    /* productionUnit = type of unit being
      produced at a certain city
     */
    private String productionUnit;
    private String focus;
    /* treasury = number of money/production in the city's treasury
     that can be used to produce a
     unit in the city
     */
    private static int treasury;

    public CityImpl() {
      size = 1;
    }

    @Override
    public Player getOwner() { return owner; }
    @Override
    public int getSize() { return size; }
    @Override
    public int getTreasury() { return treasury; }
    @Override
    public String getProduction() { return productionUnit; }
    @Override
    public String getWorkforceFocus() { return focus; }
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
  public City getCityAt( Position p ) {
    return new CityImpl();
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
  public boolean moveUnit( Position from, Position to ) {
    // try to move unit and return true if nothing is there
    // then place the unit at the desired position
    // check for unit at 'from' position
    Unit unit_from = getUnitAt(from);
    // if there's no unit at the 'from' position (aka, there's nothing to move)
    if (unit_from == null){
      return false;
    }
    // if the 'to' unit already has a unit there
    if (getUnitAt(to) != null) {
      return false;
    }
    // otherwise, move the unit from the original position to the new one
    units.remove(from);
    units.put(to, unit_from);
    return true;
  }
  public void endOfTurn() {
    // TODO: check what constitutes as a round
    // create a city with size (population = 1
    CityImpl city = new CityImpl();
    // add 6 production (or money) at the end of the turn
    city.treasury += 6;
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

}
