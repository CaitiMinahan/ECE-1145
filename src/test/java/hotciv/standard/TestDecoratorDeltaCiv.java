package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.Factories.DeltaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TestDecoratorDeltaCiv {
    private MutableGame transcribedGame;
    @Before
    public void setUp() {
        GameFactory gameFactory = new DeltaCivFactory();
        transcribedGame = new GameDecorator(new GameImpl(gameFactory));
    }
    @After
    public void breakDown() {
        transcribedGame.cities.clear();
        transcribedGame.tiles.clear();
        transcribedGame.units.clear();
    }
    @Test
    public void setDeltaCivWorldLayoutCorrectlyBLUE() {
        System.out.println("Start of Test 1 \n");
        // End the turn, and it should become BLUE player's turn
        transcribedGame.endOfTurn();

        Position BluePlayerCity = new Position(4, 5);

        assertThat(transcribedGame.getPlayerInTurn(), is(Player.BLUE));
        assertThat(transcribedGame.getCityAt(BluePlayerCity).getOwner(), is(Player.BLUE));
        System.out.println("\n End of Test 1 \n");
    }
    @Test
    public void placeCityForRedPlayer() {
        System.out.println("Start of Test 2 \n");
        Position redCityPosition = new Position(8, 12);
        Player player = Player.RED;

        // Call the method to place the city
        // try casting the method instead:
        transcribedGame.placeCity(redCityPosition, player);

        // Retrieve the city at the specified position
        City placedCity = transcribedGame.getCityAt(redCityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(placedCity, is(notNullValue()));

        // Check that the owner of the placed city is the expected player
        assertThat(placedCity.getOwner(), is(player));
        System.out.println("\n End of Test 2 \n");
    }

    @Test
    public void placeCityForBluePlayer() {
        System.out.println("Start of Test 3 \n");
        Position blueCityPosition = new Position(4, 5);
        Player player = Player.BLUE;

        // Call the method to place the city
        transcribedGame.placeCity(blueCityPosition, player);

        // Retrieve the city at the specified position
        City placedCity = transcribedGame.getCityAt(blueCityPosition);

        // Check that the placedCity is not null, indicating a city was successfully
        // placed
        assertThat(placedCity, is(notNullValue()));

        // Check that the owner of the placed city is the expected player
        assertThat(placedCity.getOwner(), is(player));
        System.out.println("\n End of Test 3 \n");
    }
}
