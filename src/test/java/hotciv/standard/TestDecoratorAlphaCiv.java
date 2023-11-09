package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.Factories.AlphaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestDecoratorAlphaCiv {
    private MutableGame transcribedGame; // new game object for producing a transcript for the game

    /** Fixture for alphaciv testing. **/
    @Before
    public void setUp() {
        GameFactory gameFactory = new AlphaCivFactory();
        transcribedGame = new GameDecorator(new GameImpl(gameFactory));
    }
    @Test
    public void citiesShouldEndWith60Production() {
        Position cityPosition = new Position(3, 2);
        transcribedGame.placeCity(cityPosition, Player.RED);

        for (int i = 0; i < 10; i++) {
            transcribedGame.endOfTurn();
        }
        // 10 rounds, 6 production each round = 60 production
        assertThat(transcribedGame.getCityAt(cityPosition).getTreasury(), is(60));
    }
    @Test
    public void cityShouldHaveSizeOne() {
        Position cityPosition = new Position(3, 2);
        transcribedGame.placeCity(cityPosition, Player.RED);

        for (int i = 0; i < 10; i++) {
            transcribedGame.endOfTurn();
        }
        // Retrieve the city at the specified position
        City city = transcribedGame.getCityAt(cityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(city, is(notNullValue()));
        assertThat(transcribedGame.getCityAt(cityPosition).getSize(), is(1));
    }
    @Test
    public void shouldAlternateBetweenRedAndBluePlayers() {
        // Check that the game starts with RED player
        assertThat(transcribedGame.getPlayerInTurn(), is(Player.RED));

        // End the turn, and it should become BLUE player's turn
        transcribedGame.endOfTurn();
        assertThat(transcribedGame.getPlayerInTurn(), is(Player.BLUE));

        // End the turn again, it should become RED player's turn
        transcribedGame.endOfTurn();
        assertThat(transcribedGame.getPlayerInTurn(), is(Player.RED));
    }
    @Test
    public void OnlyOneUnitAllowedOnATile() {
        Position moveFrom = new Position(0, 0);
        Position moveTo = new Position(0, 1);
        assertThat(transcribedGame.moveUnit(moveFrom, moveTo), is(true)); // confirm that we can move our unit from (0,0) to (0,1)
    }
    @Test
    public void GameStartsAt4000BCAndAges100EachRound() {
        // create a new game instance
        // TODO: update GameImp constructor
        // make sure we initially start the game at year 4000 BC
        assertEquals(transcribedGame.getAge(), -4000);
        // perform one round of turns (RED then BLUE)
        transcribedGame.endOfTurn();
        transcribedGame.endOfTurn();
        // verify the age has increased by 100
        assertEquals(transcribedGame.getAge(), -3900);
    }
    @Test
    public void RedWinsIn3000BC() {
        // simulate going through the rounds before we each year 3000 BC
        while (transcribedGame.getAge() < -3000) {
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(-3000));
        assertThat(transcribedGame.getWinner(), is(Player.RED));
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
        assertThat(transcribedGame.getUnitAt(redPlayerPosition).getOwner(), is(Player.RED));
        assertThat(transcribedGame.getUnitAt(redPlayerPosition2).getOwner(), is(Player.RED));
        assertThat(transcribedGame.getUnitAt(bluePlayerPosition).getOwner(), is(Player.BLUE));

        // move the red player to attack the blue player
        // should return true that the red player now moved to the blue's tile
        assertThat(transcribedGame.moveUnit(redPlayerPosition, bluePlayerPosition), is(true));

        // check that there are now two units
        assertThat(transcribedGame.getUnitAt(bluePlayerPosition).getOwner(), is(Player.RED));
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
        assertThat(transcribedGame.moveUnit(redArcherPosition, redSettlerPosition), is(false));
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
        assertThat(transcribedGame.moveUnit(redSettlerPosition, blueLegionPosition), is(false));
    }
}
