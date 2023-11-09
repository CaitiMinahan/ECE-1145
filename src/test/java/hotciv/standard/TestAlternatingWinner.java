package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.Factories.ZetaCivFactory;
import hotciv.standard.Factories.*;
import hotciv.standard.Interfaces.*;
import hotciv.standard.TestStubs.StrongerAttackerStubEpsilonCiv;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestAlternatingWinner {
    private MutableGame game;
    private MutableGame strongerAttackerGame; // game object where the epsilonCivFactory has a strongerAttackerStub
    private StrongerAttackerStubEpsilonCiv strongerAttackerStubEpsilonCiv = new StrongerAttackerStubEpsilonCiv();
    @Before
    public void setUp() {
        GameFactory gameFactory = new ZetaCivFactory();
        game = new GameImpl(gameFactory);
        GameFactory strongerAttackerStubGameFactory = new EpsilonCivFactory(strongerAttackerStubEpsilonCiv);
        strongerAttackerGame = new GameImpl(strongerAttackerStubGameFactory);
    }
    @Test
    public void testBetaCivWinner(){
        // initially, no player has won
        assertNull(game.getWinner());

        // simulate BetaCivWinner's winner method
        // where we will make sure the blue player wins after conquering all cities
        game.placeCity(new Position(5,5), Player.BLUE);
        game.placeCity(new Position(7,4), Player.BLUE);

        assertThat(game.getWinner(), is(Player.BLUE));
    }
    @Test
    public void testEpsilonCivWinner(){
        // initially, no player has won
        assertNull(game.getWinner());

        // TODO: EpsilonCivWinner needs to have a winner method implemented
        // simulate EpsilonCivWinner's winner method
        // where we will make sure the red player wins after three successful attacks

        // there are 5 Red player units on the board now using the
        // use the strongerAttackerWithManyUnitsGame game
        ((GameImpl)strongerAttackerGame).units.put(new Position(2,2), new UnitImpl((GameConstants.ARCHER), Player.RED));
        ((GameImpl)strongerAttackerGame).units.put(new Position(2,3), new UnitImpl((GameConstants.ARCHER), Player.RED));
        ((GameImpl)strongerAttackerGame).units.put(new Position(3,0), new UnitImpl((GameConstants.ARCHER), Player.BLUE));
        Position redArcher1Pos = new Position(0,0);
        Position redArcher2Pos = new Position(2,2);
        Position redArcher3Pos = new Position(2,3);
        Position blueArcherPos = new Position(3,0); // Blue will be doing the attacking

        // no winner to start
        boolean didBlueWin = strongerAttackerGame.moveUnit(blueArcherPos, redArcher1Pos);
        didBlueWin = strongerAttackerGame.moveUnit(redArcher1Pos, redArcher2Pos);
        didBlueWin = strongerAttackerGame.moveUnit(redArcher2Pos, redArcher3Pos);

        int blueWins = ((GameImpl)strongerAttackerGame).playerSuccessfulAttacks.get(Player.BLUE);
        Player winner = strongerAttackerGame.getWinner();

        // assert the following
        assertThat(winner, is(Player.BLUE));
        assertThat(blueWins, is(3));

    }
    @Test
    public void testTransitionToEpsilonCivWinner() {
        // initially, no player has won
        assertNull(game.getWinner());

        // simulate BetaCivWinner's winner method
        // where we will make sure the red player wins after conquering all cities
        game.placeCity(new Position(1, 1), Player.RED);
        game.placeCity(new Position(2, 2), Player.RED);

        assertEquals(Player.RED, game.getWinner());


        // Now, play more rounds to transition to EpsilonCiv's winning condition
        for (int i = 0; i < 21; i++) {
            game.endOfTurn();
        }
        Player playerWin = game.getWinner(); // create new player, which is initially null
        assertThat(playerWin, is(nullValue())); // switches to EpsilonCiv's winner strategy after 20 rounds
    }
}
