package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.Factories.DeltaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestDeltaCiv {
    private MutableGame game;

    /** Fixture for deltaciv testing. */
    @Before
    public void setUp() {
        GameFactory gameFactory = new DeltaCivFactory();
        game = new GameImpl(gameFactory);
    }
    @After
    public void breakDown() {
        game.cities.clear();
        game.tiles.clear();
        game.units.clear();
    }
    @Test
    public void shouldBeRedAsStartingPlayer() {

        assertThat(game, is(notNullValue()));
        assertThat(game.getPlayerInTurn(), is(Player.RED));
    }

    // TODO: integration test for testing if DeltaCiv world layout was set up
    // properly
    // TODO: this is an integration test b/c it tests the game logic's interaction
    // w/ the world layout methods
    @Test
    public void setDeltaCivWorldLayoutCorrectlyRED() {
        Position RedPlayerCity = new Position(8, 12);
        assertThat(game.getCityAt(RedPlayerCity).getOwner(), is(Player.RED));
    }

    @Test
    public void setDeltaCivWorldLayoutCorrectlyBLUE() {
        // End the turn, and it should become BLUE player's turn
        game.endOfTurn();

        Position BluePlayerCity = new Position(4, 5);

        assertThat(game.getPlayerInTurn(), is(Player.BLUE));
        assertThat(game.getCityAt(BluePlayerCity).getOwner(), is(Player.BLUE));
    }

    // TODO: unit test for testing the placeCity() method in GameImpl
    // TODO: this is a unit test b/c it isolates the placeCity() method to ensure
    // its functionality
    @Test
    public void placeCityForRedPlayer() {
        Position redCityPosition = new Position(8, 12);
        Player player = Player.RED;

        // Call the method to place the city
        // try casting the method instead:
        game.placeCity(redCityPosition, player);

        // Retrieve the city at the specified position
        City placedCity = game.getCityAt(redCityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(placedCity, is(notNullValue()));

        // Check that the owner of the placed city is the expected player
        assertThat(placedCity.getOwner(), is(player));
    }

    @Test
    public void placeCityForBluePlayer() {
        Position blueCityPosition = new Position(4, 5);
        Player player = Player.BLUE;

        // Call the method to place the city
        game.placeCity(blueCityPosition, player);

        // Retrieve the city at the specified position
        City placedCity = game.getCityAt(blueCityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(placedCity, is(notNullValue()));

        // Check that the owner of the placed city is the expected player
        assertThat(placedCity.getOwner(), is(player));
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

