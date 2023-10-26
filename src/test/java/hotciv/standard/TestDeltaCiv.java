package hotciv.standard;

import hotciv.framework.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import hotciv.standard.*;
import java.lang.reflect.Method;

public class TestDeltaCiv {
    private Game game;
    private WorldLayout deltaCivWorldLayout;
    private UnitAction unitActionType;

    private GameFactory gameFactory;

    /** Fixture for deltaciv testing. */
    @Before
    public void setUp() {
        // TODO: remove old implementation
        // layout for DeltaCiv as specified in DeltaCivWorldLayout
//        WorldLayout deltaCivWorldLayout = new DeltaCivWorldLayout();
//        unitActionType = new GenericUnitAction();
//        GenericWorldAging genericWorldAging = new GenericWorldAging();
//        Winner genericWinner = new GenericWinner();
//        game = new GameImpl(deltaCivWorldLayout, genericWorldAging, genericWinner, unitActionType);

        // step 4 in refactoring for abstract factory: create an instance of the concrete factory for the appropriate game variant:
        gameFactory = new DeltaCivFactory();
        game = new GameImpl(gameFactory);

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
        ((GameImpl) game).placeCity(redCityPosition, player);

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
        ((GameImpl) game).placeCity(blueCityPosition, player);

        // Retrieve the city at the specified position
        City placedCity = game.getCityAt(blueCityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(placedCity, is(notNullValue()));

        // Check that the owner of the placed city is the expected player
        assertThat(placedCity.getOwner(), is(player));
    }
}