package hotciv.stub;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.UnitAction;
import hotciv.standard.Interfaces.Winner;
import hotciv.standard.UnitImpl;

import java.util.*;

import static hotciv.standard.Interfaces.MutableGame.cities;
// this is not usable for us since its static
// instead of importing just create a new map

/** Test stub for game for visual testing of
 * minidraw based graphics.
 *
 * SWEA support code.
 *
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

public class StubGame2 implements Game {

  // === Unit handling ===
  private Position pos_archer_red;
  private Position pos_legion_blue;
  private Position pos_settler_red;
  private Position pos_ufo_red;
//  public Map<Position, City> cities = new HashMap<>();

  private Unit red_archer;
  private City newCity;

  private Winner winnerStrategy;
  private int age; // represents current year of the game
  private UnitAction unitActionCivType;

  public CityImpl currentCity;
  private List<GameObserver> observers = new ArrayList<>();
  // add a city to the map and give it blue ownership
  private Position BlueCityPos ;

  public boolean cityExistsAt(Position position) {
    return cities.containsKey(position);
  }

  public CityImpl createCity(Player player) {
    return new CityImpl(player);
  }

  public void setCurrentCity(City city) {
    currentCity = (CityImpl) city;
  }

  public Unit getUnitAt(Position p) {
    if ( p.equals(pos_archer_red) ) {
      return red_archer;
    }
    if ( p.equals(pos_settler_red) ) {
      return new StubUnit( GameConstants.SETTLER, Player.RED );
    }
    if ( p.equals(pos_legion_blue) ) {
      return new StubUnit( GameConstants.LEGION, Player.BLUE );
    }
    if ( p.equals(pos_ufo_red) ) {
      return new StubUnit( ThetaConstants.UFO, Player.RED );
    }
    return null;
  }

  // Stub only allows moving red archer
  public boolean moveUnit( Position from, Position to ) { 
    System.out.println( "-- StubGame2 / moveUnit called: "+from+"->"+to );
    if ( from.equals(pos_archer_red) ) {
      pos_archer_red = to;
    }
    else if (from.equals((pos_legion_blue))){
      pos_legion_blue = to;
    }
    else if (from.equals((pos_settler_red))){
      pos_settler_red = to;
    }
    else if (from.equals((pos_ufo_red))){
      pos_ufo_red = to;
    }
    // notify our observer(s) about the changes on the tiles
    gameObserver.worldChangedAt(from);
    gameObserver.worldChangedAt(to);


    return true; 
  }

  // === Turn handling ===
  private Player inTurn;
  public void endOfTurn() {
    System.out.println( "-- StubGame2 / endOfTurn called." );
    inTurn = (getPlayerInTurn() == Player.RED ?
              Player.BLUE : 
              Player.RED );
    // no age increments
    gameObserver.turnEnds(inTurn, -4000);
  }

  public Player getPlayerInTurn() { return inTurn; }

  // === Observer handling ===
  protected GameObserver gameObserver;
  // observer list is only a single one...
  public void addObserver(GameObserver observer) {
    gameObserver = observer;
  } 

  public StubGame2() { 
    defineWorld(1); 
    // AlphaCiv configuration
    pos_archer_red = new Position( 2, 0);
    pos_legion_blue = new Position( 3, 2);
    pos_settler_red = new Position( 4, 3);
    pos_ufo_red = new Position( 6, 4);
    BlueCityPos = new Position(7,7);

//    putCity(cityPos, BlueCity);
//    cities.put(cityPos, BlueCity); How the hell do I add things to this

    // the only one I need to store for this stub
    red_archer = new StubUnit( GameConstants.ARCHER, Player.RED );
    // give the new city a stub city init
    newCity = new StubCity(Player.BLUE);

    // create new game observer
    addObserver(gameObserver);

    inTurn = Player.RED;
  }

  // A simple implementation to draw the map of DeltaCiv
  protected Map<Position,Tile> world; 
  public Tile getTileAt( Position p ) { return world.get(p); }


  /** define the world.
   * @param worldType 1 gives one layout while all other
   * values provide a second world layout.
   */
  protected void defineWorld(int worldType) {
    // TODO: the scope of this hashmap is limited to the function call, not sure this would work
    world = new HashMap<Position,Tile>();
    for ( int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
      for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
        Position p = new Position(r,c);
        world.put( p, new StubTile(GameConstants.PLAINS));

        // todo: do we need to adjust the world to look the same as what the GUI looks like when gradle show is ran?
      }
    }
  }

  // TODO: Add more stub behaviour to test MiniDraw updating
  // added in the GameImpl methods for the above todo 
  public City getCityAt(Position p) {
    if (p.equals(BlueCityPos)) {
      return newCity;
//      return null;
    } else {
      return null;
    }
  }

//  if (cities.containsKey(p))
//  {
//    return cities.get(p);
//    } else {
//      return null;
//    }
//  }

  public void putCity(Position p, City c) {
    if(!cities.containsKey(p)){
      cities.put(p,c);
    }
    else {
      System.out.println("Cannot set a city at pos: " + p + ".");
    }
  }
  public Player getWinner() {
    return winnerStrategy.gameWinner((MutableGame) this);
  }
  public int getAge() {
    return age;
  }

  // TODO: need to fill in the two methods below (they are empty in GameImpl)
  public void changeWorkForceFocusInCityAt( Position p, String balance ) {}
  public void changeProductionInCityAt( Position p, String unitType ) {}
  public void performUnitActionAt(Position p) {
    Unit u = getUnitAt(p);
    // based on the type of game we are playing this will use the different
    // implementations
    if (this.unitActionCivType != null) {
      // get the position based on the unit
      // convert unit to unit impl
      UnitImpl ui = (UnitImpl) u;
      // run the action function
      this.unitActionCivType.performAction(ui, p, (MutableGame) this);
    } else {
      // for some reason the unitActionCivType is null when it should be generic or
      // gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
  }

  // added setTileFocus from GameImpl into here to satisfy todo
  public void setTileFocus(Position position) {
    System.out.println("StubGame2 -- setTileFocus called.");
    System.out.println(" Position: " + position);

    // Implement logic to set tile focus
    // Notify observers about the tile focus change
//    for (GameObserver observer : observers) {
//      observer.tileFocusChangedAt(position);
//    }
    gameObserver.tileFocusChangedAt(position);

    // Handle different tile types
    Tile tile = getTileAt(position);
    if (tile != null) {
      String tileType = tile.getTypeString();

      System.out.println("Tile focused: " + tileType);
      // check for city or unit on the tile
      City newCity = getCityAt(position);
      Unit newUnit = getUnitAt(position);

      if(newCity != null){
        System.out.println("City on this position");
      }
      if(newUnit != null){
        System.out.println("Unit on this position of type " + newUnit.getTypeString());
      }
      // Implement additional logic based on the tile type if needed
    }  }
}

class StubUnit implements  Unit {
  private String type;
  private Player owner;
  public StubUnit(String type, Player owner) {
    this.type = type;
    this.owner = owner;
  }
  public String getTypeString() { return type; }
  public Player getOwner() { return owner; }
  public int getMoveCount() { return 1; }
  public int getDefensiveStrength() { return 0; }
  public int getAttackingStrength() { return 0; }
}

class StubCity implements City {

  private int size;
  private Player owner;
  private int populationSize;

  //type of unit being produced at a certain city
  private String productionUnit;
  private String focus;
  /*  treasury = number of money/production in the city's treasury
   that can be used to produce a
   unit in the city
   */
  private int treasury;

  // TODO: modified CityImpl function so we pass in the owner of the city
  public StubCity(Player owner) {
    size = 1;
    treasury = 0;
    this.owner = owner;
  }
  public void setTreasury(int t){
    this.treasury = t;
  }

  public void setSize(int size) { this.size = size;}
  public void setPopulationSize(int popSize) {this.populationSize = popSize;}
  public int getPopulationSize() { return this.populationSize;}
  public Player getOwner() { return owner; }
  public int getSize() { return size; }
  public int getTreasury() { return treasury; }
  public String getProduction() { return productionUnit; }
  public String getWorkforceFocus() { return focus; }

  // new function to set the owner of a city in case of battle
  public void setOwner(Player player) { this.owner = player;}
}
