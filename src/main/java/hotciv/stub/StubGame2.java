package hotciv.stub;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.Factories.SemiCivFactory;
import hotciv.standard.GammaCivUnitAction;
import hotciv.standard.GenericUnitAction;
import hotciv.standard.Interfaces.*;
import hotciv.standard.UnitImpl;

import java.util.*;

import static hotciv.standard.Interfaces.MutableGame.cities;

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

public class StubGame2 implements MutableGame {

  // === Unit handling ===
  private Position pos_archer_red;
  private Position pos_legion_blue;
  private Position pos_settler_red;
  private Position pos_ufo_red;

  private Unit red_archer;
  private Unit currentUnit;
  private Player currentPlayer;
  private int turnCount;

  private WorldLayout worldLayoutStrategy;
  private WorldAging worldAgingStrategy;
  private Winner winnerStrategy;
  private PlayerSetup playerSetup;
  private UnitAction unitActionCivType;
  private int age; // represents current year of the game

  public CityImpl currentCity;
  private List<GameObserver> observers = new ArrayList<>();


  public boolean cityExistsAt(Position position) {
    return cities.containsKey(position);
  }

  public City createCity(Player player) {
    return new CityImpl(player);
  }

  public void setCurrentCity(City city) {
    currentCity = (CityImpl) city;
  }

  public StubGame2() {
    defineWorld(1);

    // Hardcode Semi-Civ into constructor
    GameFactory gameFactory = new SemiCivFactory();
    this.worldLayoutStrategy = gameFactory.createWorldLayout();
    this.worldAgingStrategy = gameFactory.createWorldAging();
    this.winnerStrategy = gameFactory.createWinnerStrategy();
    this.unitActionCivType = gameFactory.createUnitAction();
    this.playerSetup = gameFactory.createPlayerSetup();

    // AlphaCiv configuration
    pos_archer_red = new Position( 2, 0);
    pos_legion_blue = new Position( 3, 2);
    pos_settler_red = new Position( 4, 3);
    pos_ufo_red = new Position( 6, 4);

    // the only one I need to store for this stub
    red_archer = new StubUnit( GameConstants.ARCHER, Player.RED );

    inTurn = Player.RED;
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

  public void placeCity(Position position, Player player) {
    if (!cityExistsAt(position)) {
      City newCity = createCity(player);
      cities.put(position, newCity);
      setCurrentCity(newCity);
    }
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

  // create helper function to set the map according to setupWorld
  // method in WorldLayout interface
  public void setupWorldLayout(WorldLayout worldLayoutStrategy) {
    if (worldLayoutStrategy != null) {
      worldLayoutStrategy.setupWorld(this); // Pass the current game instance to the layout strategy
    }
  }
  // Getter and setter for the current Unit variable
  public Unit getCurrentUnit(){ return currentUnit; }
  public void setCurrentUnit (Unit u) { currentUnit = u; }
  // Getter for the current player
  public Player getCurrentPlayer() { return currentPlayer;}

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

  // A simple implementation to draw the map of DeltaCiv
  protected Map<Position,Tile> world; 
  public Tile getTileAt( Position p ) { return world.get(p); }


  /** define the world.
   * @param worldType 1 gives one layout while all other
   * values provide a second world layout.
   */
  protected void defineWorld(int worldType) {
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
    if (cities.containsKey(p)) {
      return cities.get(p);
    }
    return null;
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
    System.out.println("-- StubGame2 / performUnitActionAt called.");
    Unit u = getUnitAt(p);
    // based on the type of game we are playing this will use the different
    // implementations
    if (this.unitActionCivType != null) {
      // get the position based on the unit
      // convert unit to unit impl
      UnitImpl ui = (UnitImpl) u;
      // run the action function
      this.unitActionCivType.performAction(ui, p, this);
    } else {
      // for some reason the unitActionCivType is null when it should be generic or
      // gammaCiv instance
      System.out.println("The UnitAction type was null, should be generic or GammaCiv");
    }
  }

  // added setTileFocus from GameImpl into here to satisfy todo
  public void setTileFocus(Position position) {
    System.out.println("-- StubGame2 / setTileFocus called.");
//    System.out.println(" *** IMPLEMENTATION PENDING ***");

    // Implement logic to set tile focus
    // Notify observers about the tile focus change
    for (GameObserver observer : observers) {
      observer.tileFocusChangedAt(position);
    }

    // Handle different tile types
    Tile tile = getTileAt(position);
    if (tile != null) {
      String tileType = tile.getTypeString();

      // todo: Update the GUI to display tile type in the right side bar
      System.out.println("Tile focused: " + tileType);

      // Implement additional logic based on the tile type if needed
    }  }

  // todo: test placing a city ??

  // todo: test placing a unit ??

}

class StubUnit implements MutableUnit {
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

  public void setDefendingStrength(int d) {}
  public void setAttackingStrength(int a) {}
  public void setTravelDistace(int t) {}
  public int getTravelDistace() { return 0; }
  public int getProductionCost() { return 0; }
  public void setProductionCost(int cost) {}
  public boolean getCanMove() { return true; }
  public void setCanMove (boolean can_move) {}
  // TODO needed to add this to get unique IDs
  public UUID getUnitID() { return null; }
}
