package hotciv.standard;

import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.Factories.BetaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class TestDecoratorBetaCiv {
    private MutableGame transcribedGame;
    @Before
    public void setUp() {
        GameFactory gameFactory = new BetaCivFactory();
        transcribedGame = new GameDecorator(new GameImpl(gameFactory));
    }
    @Test
    public void blueWinsFromOwningAllCities() {
        System.out.println("Start of Test 1 \n");
        transcribedGame.placeCity(new Position(5,5), Player.BLUE);
        transcribedGame.placeCity(new Position(7,4), Player.BLUE);

        assertThat(transcribedGame.getWinner(), is(Player.BLUE));
        System.out.println("End of Test 1 \n");
    }
    @Test
    public void redWinsFromOwningAllCities() {
        System.out.println("Start of Test 2 \n");
        transcribedGame.placeCity(new Position(2,5), Player.RED);
        transcribedGame.placeCity(new Position(6,1), Player.RED);

        assertThat(transcribedGame.getWinner(), is(Player.RED));
        System.out.println("End of Test 2 \n");
    }
    @Test
    public void shouldReturnNullForMultiplePlayersOwningCities() {
        System.out.println("Start of Test 3 \n");
        transcribedGame.placeCity(new Position(2,5), Player.RED);
        transcribedGame.placeCity(new Position(6,1), Player.BLUE);

        assertNull(transcribedGame.getWinner());
        System.out.println("End of Test 3 \n");
    }
    @Test
    public void shouldBe100BCAfter39Turns() {
        System.out.println("Start of Test 4 \n");
        // Between 4000 BC and 100 BC, age should increase by 100
        // BC is represented by a negative number
        for (int i = 0; i < 39; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(-100));
        System.out.println("End of Test 4 \n");
    }
    @Test
    public void shouldBe1BCAfter100BC() {
        System.out.println("Start of Test 5 \n");
        for (int i = 0; i < 40; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(-1));
        System.out.println("End of Test 5 \n");
    }
    @Test
    public void shouldBe1ADAfter1BC() {
        System.out.println("Start of Test 6 \n");
        for (int i = 0; i < 41; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(1));
        System.out.println("End of Test 6 \n");
    }
    @Test
    public void shouldBe50ADAfter1AD() {
        System.out.println("Start of Test 7 \n");
        for (int i = 0; i < 42; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(50));
        System.out.println("End of Test 7 \n");
    }
    @Test
    public void shouldBe1750After76Rounds() {
        System.out.println("Start of Test 8 \n");
        // After the year 50, age should increase by 50
        for (int i = 0; i < 76; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(1750));
        System.out.println("End of Test 8 \n");
    }
    @Test
    public void shouldBe1900After82Rounds() {
        System.out.println("Start of Test 9 \n");
        // After the year 1750, age should increase by 25
        for (int i = 0; i < 82; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(1900));
        System.out.println("End of Test 9 \n");
    }
    @Test
    public void shouldBe1970After96Rounds() {
        System.out.println("Start of Test 10 \n");
        // After the year 1900, age should increase by 5
        for (int i = 0; i < 96; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(1970));
        System.out.println("End of Test 10 \n");
    }
    @Test
    public void shouldBe1980After106Rounds() {
        System.out.println("Start of Test 11 \n");
        // After the year 1970, age should increase by 1
        for (int i = 0; i < 106; i++){
            transcribedGame.endOfTurn();
            transcribedGame.endOfTurn();
        }
        assertThat(transcribedGame.getAge(), is(1980));
        System.out.println("End of Test 11 \n");
    }

}
