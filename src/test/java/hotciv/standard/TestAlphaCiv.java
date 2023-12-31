package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.Factories.AlphaCivFactory;
import hotciv.standard.Interfaces.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Skeleton class for AlphaCiv test cases
 * <p>
 * Updated Oct 2015 for using Hamcrest matchers
 * <p>
 * This source code is from the book
 * "Flexible, Reliable Software:
 * Using Patterns and Agile Development"
 * published 2010 by CRC Press.
 * Author:
 * Henrik B Christensen
 * Department of Computer Science
 * Aarhus University
 * <p>
 * Please visit http://www.baerbak.com/ for further information.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TestAlphaCiv {
    private MutableGame game;
    /**
     * Fixture for alphaciv testing.
     */
    @Before
    public void setUp() {
        GameFactory gameFactory = new AlphaCivFactory();
        game = new GameImpl(gameFactory);

    }
    @After
    public void breakDown() {
        game.cities.clear();
        game.tiles.clear();
        game.units.clear();
    }

    // FRS p. 455 states that 'Red is the first player to take a turn'.
    @Test
    public void shouldBeRedAsStartingPlayer() {

        assertThat(game, is(notNullValue()));
        assertThat(game.getPlayerInTurn(), is(Player.RED));
    }

    @Test
    public void shouldDefineOceanTile() {
        Position oceanPosition = new Position(1, 0);
        assertThat(game.getTileAt(oceanPosition).getTypeString(), is(GameConstants.OCEANS));
    }

    @Test
    public void shouldDefineHillTile() {
        Position hillsPosition = new Position(0, 1);
        assertThat(game.getTileAt(hillsPosition).getTypeString(), is(GameConstants.HILLS));
    }

    @Test
    public void shouldDefineMountainTile() {
        Position mountPosition = new Position(2, 2);
        assertThat(game.getTileAt(mountPosition).getTypeString(), is(GameConstants.MOUNTAINS));
    }

    @Test
    public void shouldDefinePlainsTile() {
        Position plainsPosition = new Position(3, 2);
        assertThat(game.getTileAt(plainsPosition).getTypeString(), is(GameConstants.PLAINS));
    }

    @Test
    public void citiesShouldEndWith60Production() {
        Position cityPosition = new Position(3, 2);
        game.placeCity(cityPosition, Player.RED);

        for (int i = 0; i < 10; i++) {
            game.endOfTurn();
        }
        // 10 rounds, 6 production each round = 60 production
        assertThat(game.getCityAt(cityPosition).getTreasury(), is(60));
    }

    @Test
    public void cityShouldHaveSizeOne() {
        Position cityPosition = new Position(3, 2);
        game.placeCity(cityPosition, Player.RED);

        for (int i = 0; i < 10; i++) {
            game.endOfTurn();
        }
        // Retrieve the city at the specified position
        City city = game.getCityAt(cityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(city, is(notNullValue()));
        assertThat(game.getCityAt(cityPosition).getSize(), is(1));
    }

    // test alternative players when one's turn ends
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
    public void RedStartsWithArcherandSettler() {
        Position RedPlayerPositionArcher = new Position(0, 0);
        Position RedPlayerPositionSettler = new Position(1, 1);
        assertThat(game.getUnitAt(RedPlayerPositionArcher).getTypeString(), is(GameConstants.ARCHER));
        assertThat(game.getUnitAt(RedPlayerPositionSettler).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void BlueStartsWithLegion() {
        Position BluePlayerPositionLeigon = new Position(1, 2);
        assertThat(game.getUnitAt(BluePlayerPositionLeigon).getTypeString(), is(GameConstants.LEGION));
    }

  // modified test case for moveUnit method for iteration 7
  @Test
  public void OnlyOneUnitAllowedOnATile() {
    Position moveFrom = new Position(0, 0);
    Position moveTo = new Position(0, 1);
    assertThat(game.getUnitAt(moveTo), is(nullValue()));  // modified this to make sure we can only move units to empty tiles
    assertThat(game.moveUnit(moveFrom, moveTo), is(true)); // confirm that we can move our unit from (0,0) to (0,1)

    // added this to try and move unit to an occupied tile
    Position moveToNewSpot = new Position(1,1); // we know a settler is here
    assertThat(game.moveUnit(moveTo, moveToNewSpot), is(false)); // confirm we cannot move unit to occupied tile at (1,1)

  }

    @Test
    public void GameStartsAt4000BCAndAges100EachRound() {
        // create a new game instance
        // TODO: update GameImp constructor
        // make sure we initially start the game at year 4000 BC
        assertEquals(game.getAge(), -4000);
        // perform one round of turns (RED then BLUE)
        game.endOfTurn();
        game.endOfTurn();
        // verify the age has increased by 100
        assertEquals(game.getAge(), -3900);
    }

    @Test
    public void RedWinsIn3000BC() {
        // simulate going through the rounds before we each year 3000 BC
        while (game.getAge() < -3000) {
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(-3000));
        assertThat(game.getWinner(), is(Player.RED));
    }

    @Test
    public void AttackingUnitShouldAlwaysWin() {
        // when a unit moves into an occupied space, the battle begins. Attacking unit
        // should always win
        // initialize the location of the red player
        Position redPlayerPosition = new Position(0, 0); // position of red player with archer
        Position redPlayerPosition2 = new Position(1, 1); // position of red player with settler
        Position bluePlayerPosition = new Position(1, 2); // position of blue player with legion

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

    @Test
    public void RedUnitCannotAttackRedUnit() {
        // when a red unit attempts to move into a space occupied by another red unit,
        // the original unit should not be able to move (no fortification)
        Position redArcherPosition = new Position(0, 0); // position of the red player archer
        Position redSettlerPosition = new Position(1, 1); // position of the red player settler

        // false will indicate that a player cannot move a unit to another tile if
        // another of their own
        // units occupies that tile
        assertThat(game.moveUnit(redArcherPosition, redSettlerPosition), is(false));
    }

  @Test
  public void NonAttackingUnitCannotAttack() {
    /*
     * when a unit of non-attacking type attempts to move into
     * a space occupied by another unit, it cannot attack and should
     * not be allowed to move
     */
    Position redSettlerPosition = new Position(1, 1);
    Position blueLegionPosition = new Position(1, 2);
    // attempt to move the red settler into the blue occupied tile
    assertThat(game.moveUnit(redSettlerPosition, blueLegionPosition), is(false));
  }

  // test added for moveUnit method for iteration 7
  // test moving units to a tile located at a city
  @Test
  public void successfulMoveIntoCityAndCityOwnerBecomesUnitOwner(){
    Position bluePlayerCityPosition = new Position(7, 11);
    Position redPlayerUnitPositionFrom = new Position(0,0);
    Unit unit = game.getUnitAt(redPlayerUnitPositionFrom);
    Player player1 = Player.RED;
    Player player2 = Player.BLUE;

    // Call the method to place the city
    game.placeCity(bluePlayerCityPosition, player2);

    // Retrieve the city at the specified position
    City placedCity = game.getCityAt(bluePlayerCityPosition);

    // Check that the placedCity is not null, indicating a city was successfully placed
    assertThat(placedCity, is(notNullValue()));

    // call moveUnit method to move unit into a city
    assertThat(game.moveUnit(redPlayerUnitPositionFrom, bluePlayerCityPosition), is(true));

    // assert that the owner of the city is the owner of the unit
    assertThat(placedCity.getOwner(), is(player1));
    assertThat(unit.getOwner(), is(player1));

  }

  // testing the observer pattern
  // these tests will use methods from the GameObserverSpy to make sure the Observer is interacting with the game
  // the Observer should be notified whenever there is a state change
  @Test
  public void testWorldChangedAt() {
      // Setup: Add an observer
      GameObserverSpy observer = new GameObserverSpy();
      game.addObserver(observer);

      Position observerFrom = new Position(0, 0);
      Position observerTo = new Position(3, 3);

      // Action: Make a change in the world (e.g., move a unit)
      assertThat(game.moveUnit(observerFrom, observerTo), is(true));

      // Assertion: Verify that the observer's worldChangedAt method was called
      assertTrue(observer.worldChangedAtCalled);
    }

    @Test
    public void testTurnEnds() {
        // Setup: Add an observer
        GameObserverSpy observer = new GameObserverSpy();
        game.addObserver(observer);

        // Action: Move to the end of the turn
        game.endOfTurn();

        // Assertion: Verify that the observer's turnEnds method was called
        assertTrue(observer.turnEndsCalled);
    }

    @Test
    public void testTileFocusChangedAt() {
        // Setup: Add an observer
        GameObserverSpy observer = new GameObserverSpy();
        game.addObserver(observer);

        // Action: Change the focus to a tile
        game.setTileFocus(new Position(2, 2));

        // Assertion: Verify that the observer's tileFocusChangedAt method was called
        assertTrue(observer.tileFocusChangedAtCalled);
    }
}

