package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.Factories.ZetaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.UnitAction;
import hotciv.standard.Interfaces.WorldAging;
import hotciv.standard.Interfaces.WorldLayout;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestAlternatingWinner {
    private Game game;
    private WorldLayout worldLayout;
    private UnitAction genericUnitAction;
    private WorldAging worldAging;
    private AlternatingWinnerStrategy alternatingWinnerStrategy;

    private GameFactory gameFactory;
    @Before
    public void setUp() {
        // TODO: remove old implementation
//        worldLayout = new GenericWorldLayout();
//        genericUnitAction = new GenericUnitAction();
//        worldAging = new GenericWorldAging();
//
//        // added alternating winner strat to pass to GameImpl constructor
//        alternatingWinnerStrategy = new AlternatingWinnerStrategy(); // TODO: EpsilonCivWinner needs to be implemented
//        game = new GameImpl(worldLayout, worldAging, alternatingWinnerStrategy, genericUnitAction);

        // step 4 in refactoring for abstract factory: create an instance of the concrete factory for the appropriate game variant:
        gameFactory = new ZetaCivFactory();
        game = new GameImpl(gameFactory);

    }
    @Test
    public void testBetaCivWinner(){
        // initially, no player has won
        assertNull(game.getWinner());

        // simulate BetaCivWinner's winner method
        // where we will make sure the blue player wins after conquering all cities
        ((GameImpl) game).placeCity(new Position(5,5), Player.BLUE);
        ((GameImpl) game).placeCity(new Position(7,4), Player.BLUE);

        assertThat(game.getWinner(), is(Player.BLUE));
    }
    @Test
    public void testEpsilonCivWinner(){
        // initially, no player has won
        assertNull(game.getWinner());

        // TODO: EpsilonCivWinner needs to have a winner method implemented
        // simulate EpsilonCivWinner's winner method
        // where we will make sure the red player wins after three successful attacks
        // assertThat(game.getWinner(), is(Player.RED)); // where the player can be either red or blue
    }
    @Test
    public void testTransitionToEpsilonCivWinner() {
        // initially, no player has won
        assertNull(game.getWinner());

        // simulate BetaCivWinner's winner method
        // where we will make sure the red player wins after conquering all cities
        ((GameImpl) game).placeCity(new Position(1, 1), Player.RED);
        ((GameImpl) game).placeCity(new Position(2, 2), Player.RED);

        assertEquals(Player.RED, game.getWinner());

        // Now, play more rounds to transition to EpsilonCiv's winning condition
        for (int i = 0; i < 20; i++) {
            game.endOfTurn();
        }
        // TODO: may need to update the winner player here depending on EpsilonCivWinner's implementation
        // Assuming Player.BLUE wins three attacks
        // assertEquals(Player.BLUE, game.getWinner());
    }
}
