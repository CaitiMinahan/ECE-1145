package hotciv.standard;

import hotciv.framework.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import hotciv.standard.*;

/** Skeleton class for AlphaCiv test cases

    Updated Oct 2015 for using Hamcrest matchers

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
public class TestAlphaCiv {
  private Game game;
  private WorldLayout worldLayout;

  /** Fixture for alphaciv testing. */
  @Before
  public void setUp() {
      // TODO: step 3 - refactor worldLayout to use a concrete WorldLayout instance
      // TODO: when we create TestDeltaCiv, we will specify the layout in the setUp as new DeltaCivWorldLayout
      WorldLayout worldLayout = new GenericWorldLayout(); // layout for AlphaCiv as specified in GenericWorldLayout
      game = new GameImpl(worldLayout);
  }

    // FRS p. 455 states that 'Red is the first player to take a turn'.
  @Test
  public void shouldBeRedAsStartingPlayer() {

    assertThat(game, is(notNullValue()));
    assertThat(game.getPlayerInTurn(), is(Player.RED));
  }
  @Test
  public void shouldDefineTiles() {
    Position oceanPosition = new Position(1,0);
    Position hillsPosition = new Position(0,1);
    Position mountPosition = new Position(2, 2);
    Position plainsPosition = new Position(0,0);
    Position plainsPosition2 = new Position(3,2);

    assertThat(game.getTileAt(oceanPosition).getTypeString(), is(GameConstants.OCEANS));
    assertThat(game.getTileAt(hillsPosition).getTypeString(), is(GameConstants.HILLS));
    assertThat(game.getTileAt(mountPosition).getTypeString(), is(GameConstants.MOUNTAINS));
    assertThat(game.getTileAt(plainsPosition).getTypeString(), is(GameConstants.PLAINS));
    assertThat(game.getTileAt(plainsPosition2).getTypeString(), is(GameConstants.PLAINS));
  }
//  @Test
//  public void citiesShouldEndWith60Production() {
////        GameImpl city = new GameImpl();
//      Position cityPosition = new Position(3,2);
//
//        for (int i = 0; i < 10; i++){
//            game.endOfTurn();
//        }
//        // 10 rounds, 6 production each round = 60 production
//        assertThat(game.getCityAt(cityPosition).getTreasury(), is(60));
//        assertThat(game.getCityAt(cityPosition).getSize(), is(1));
//  }
//  // test alternative players when one's turn ends
  @Test
  public void shouldAlternateBetweenRedAndBluePlayers() {
    // Check that the game starts with RED player
    assertThat(game.getPlayerInTurn(), is(Player.RED));

    // End the turn, and it should become BLUE player's turn
    game.endOfTurn();
    assertThat(game.getPlayerInTurn(), is(Player.BLUE));

    // End the turn again, it should become RED player's turn
    game.endOfTurn();
    assertThat(game.getPlayerInTurn(), is(Player.RED));
  }

@Test
public void RedStartsWithArcherandSettler(){
    Position RedPlayerPositionArcher = new Position(0,0);
    Position RedPlayerPositionSettler = new Position(1,1);
    assertThat(game.getUnitAt(RedPlayerPositionArcher).getTypeString(), is(GameConstants.ARCHER));
    assertThat(game.getUnitAt(RedPlayerPositionSettler).getTypeString(), is(GameConstants.SETTLER));
}
@Test
public void BlueStartsWithLegion(){
    Position BluePlayerPositionLeigon = new Position(1,2);
    assertThat(game.getUnitAt(BluePlayerPositionLeigon).getTypeString(), is(GameConstants.LEGION));
}

@Test
public void OnlyOneUnitAllowedOnATile(){
    Position moveFrom = new Position(0,0);
    Position moveTo = new Position(0,1);
    assertEquals(game.moveUnit(moveFrom, moveTo), true); // confirm that we can move our unit from (0,0) to (0,1)
}
@Test
public void GameStartsAt4000BCAndAges100EachRound(){
    // create a new game instance
    // TODO: update GameImp constructor
    Game game = new GameImpl(worldLayout);
    // make sure we initially start the game at year 4000 BC
    assertEquals(game.getAge(), 4000);
    // perform one round of turns (RED then BLUE)
    game.endOfTurn();
    game.endOfTurn();
    // verify the age has decreased by 100
    assertEquals(game.getAge(), 3900);
  }
@Test
public void RedWinsIn3000BC(){
    // simulate going through the rounds before we each year 3000 BC
    while (game.getAge() > 3000){
      game.endOfTurn();
    }
    assertThat(game.getAge(), is(3000));
    assertThat(game.getWinner(), is(Player.RED));
  }
@Test
public void AttackingUnitShouldAlwaysWin(){
    // when a unit moves into an occupied space, the battle begins. Attacking unit should always win
    // initialize the location of the red player
    // TODO: remove line below so the test passes
//    Game game = new GameImpl(worldLayout);
    Position redPlayerPosition = new Position(0,0); // position of red player with archer
    Position redPlayerPosition2 = new Position(1,1); // position of red player with settler
    Position bluePlayerPosition = new Position(1,2); // position of blue player with legion

    // check that there are three units
    assertThat(game.getUnitAt(redPlayerPosition).getOwner(), is(Player.RED));
    assertThat(game.getUnitAt(redPlayerPosition2).getOwner(), is(Player.RED));
    assertThat(game.getUnitAt(bluePlayerPosition).getOwner(), is(Player.BLUE));

    // move the red player to attack the blue player
    // should return true that the red player now moved to the blue's tile
    assertThat(game.moveUnit(redPlayerPosition, bluePlayerPosition), is(true));

    // check that there are now two units
    assertThat(game.getUnitAt(bluePlayerPosition).getOwner(), is(Player.RED));
}

}

