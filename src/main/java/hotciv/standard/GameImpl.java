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

  // GameImpl constructor
  public GameImpl(){
    // initialize the game with the first player as RED
    currentPlayer = Player.RED;
    units = new HashMap<>();
    // initialize units of RED and BLUE players
    units.put(new Position(0,0), new UnitImpl(GameConstants.ARCHER, Player.RED));
    units.put(new Position(1,1), new UnitImpl(GameConstants.SETTLER, Player.RED));
    units.put(new Position(1,2), new UnitImpl(GameConstants.LEGION, Player.BLUE));
  }
  public Tile getTileAt( Position p ) { return null; }
  public Unit getUnitAt( Position p ) {
    // make sure we never return a null unit in the map
    Unit unit = units.get(p);
    // if the unit is not found in the map, return it
    if (unit != null){
      return unit;
    }

    // let's say that the red player has two units initially: archer and settler
    // the archer will be placed at position tile (0,0) and the settler at (1,1)
    if ((currentPlayer == Player.RED && (p.equals(new Position(0,0)) || p.equals(new Position(1,1))))){
      if(p.equals(new Position(0,0))){
        return new UnitImpl(GameConstants.ARCHER, Player.RED);
      }
      else{
        return new UnitImpl(GameConstants.SETTLER, Player.RED);
      }
    }
    else if ((currentPlayer == Player.BLUE && (p.equals(new Position(1,2))))){
      return new UnitImpl(GameConstants.LEGION, Player.BLUE);
    }
    // return null;
    // return a default unit for positions not found in the map
    return null;
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
    }
    @Override
    public int getDefensiveStrength() {
      return defensiveStrength;
    }
    @Override
    public int getAttackingStrength() {
      return attackingStrength;
    }
  }
  public City getCityAt( Position p ) { return null; }
  public Player getPlayerInTurn() {
    // return null;
    // return the current player
    return currentPlayer;
    }
  public Player getWinner() { return null; }
  public int getAge() { return 0; }
  public boolean moveUnit( Position from, Position to ) {
    // try to move unit and reutrn true if nothing is there
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
    //swicth players when the other's turn ends
    //later on, we can use a switch case to switch between players (after blue, yellow goes, etc.)
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;
  }
  public void changeWorkForceFocusInCityAt( Position p, String balance ) {}
  public void changeProductionInCityAt( Position p, String unitType ) {}
  public void performUnitActionAt( Position p ) {}
}
