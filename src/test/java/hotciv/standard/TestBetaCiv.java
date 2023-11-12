package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.Factories.BetaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
public class TestBetaCiv {
    private MutableGame game;
    @Before
    public void setUp() {
        GameFactory gameFactory = new BetaCivFactory();
        game = new GameImpl(gameFactory);

        //game.cities.clear();
    }
    @After
    public void breakDown() {
        game.cities.clear();
        game.tiles.clear();
        game.units.clear();
    }
    @Test
    public void blueWinsFromOwningAllCities() {
        game.placeCity(new Position(5,5), Player.BLUE);
        game.placeCity(new Position(7,4), Player.BLUE);

        assertThat(game.getWinner(), is(Player.BLUE));
    }
    @Test
    public void redWinsFromOwningAllCities() {
        game.placeCity(new Position(2,5), Player.RED);
        game.placeCity(new Position(6,1), Player.RED);

        assertThat(game.getWinner(), is(Player.RED));
    }
    @Test
    public void shouldReturnNullForMultiplePlayersOwningCities() {
        game.placeCity(new Position(2,5), Player.RED);
        game.placeCity(new Position(6,1), Player.BLUE);

        assertNull(game.getWinner());
    }
    @Test
    public void shouldBe100BCAfter39Turns() {
        // Between 4000 BC and 100 BC, age should increase by 100
        // BC is represented by a negative number
        for (int i = 0; i < 39; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(-100));
    }
    @Test
    public void shouldBe1BCAfter100BC() {
        for (int i = 0; i < 40; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(-1));
    }
    @Test
    public void shouldBe1ADAfter1BC() {
        for (int i = 0; i < 41; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1));
    }
    @Test
    public void shouldBe50ADAfter1AD() {
        for (int i = 0; i < 42; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(50));
    }
    @Test
    public void shouldBe1750After76Rounds() {
        // After the year 50, age should increase by 50
        for (int i = 0; i < 76; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1750));
    }
    @Test
    public void shouldBe1900After82Rounds() {
        // After the year 1750, age should increase by 25
        for (int i = 0; i < 82; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1900));
    }
    @Test
    public void shouldBe1970After96Rounds() {
        // After the year 1900, age should increase by 5
        for (int i = 0; i < 96; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1970));
    }
    @Test
    public void shouldBe1980After106Rounds() {
        // After the year 1970, age should increase by 1
        for (int i = 0; i < 106; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1980));
    }
    @Test
    public void shouldSetAge() {
        game.setAge(1950);
        assertThat(game.getAge(), is(1950));
    }
    @Test
    public void shouldSetTurnCount() {
        game.setTurnCount(2);
        assertThat(game.getTurnCount(), is(2));
    }
    @Test
    public void shouldGetTurnCount() {
        game.setTurnCount(10);
        int testAge = game.getTurnCount();

        assertThat(testAge, is(10));
    }
}
