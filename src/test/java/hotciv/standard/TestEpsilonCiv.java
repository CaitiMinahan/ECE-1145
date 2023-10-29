package hotciv.standard;

import hotciv.framework.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/*
* This file will use Test Stubs to allow the game winner to be determined.
* */
public class TestEpsilonCiv {
    private GameImpl strongerAttackerGame; // game object where the epsilonCivFactory has a strongerAttackerStub
    private GameImpl strongerDefenderGame; // game object where the epsilonCivFactory has a strongerDefenderStub
    private GameImpl genericGame; // generic game object

    // variables for the generic unitAttack and Stubs
    // attacking delegate with normal functionality
    private GenericUnitAttacking genericUnitAttacking;
    // StrongerAttacker implementation of UnitAttacking
    private StrongerAttackerStubEpsilonCiv strongerAttackerStubEpsilonCiv = new StrongerAttackerStubEpsilonCiv();
    // StrongerDefender implementation of UnitAttacking
    private StrongerDefenderStubEpsilonCiv strongerDefenderStubEpsilonCiv = new StrongerDefenderStubEpsilonCiv();
    @Before
    public void setUp() {
        // for each stub, create a new instantiation
        GameFactory strongerAttackerStubGameFactory = new EpsilonCivFactory(strongerAttackerStubEpsilonCiv);
        GameFactory strongerDefenderStubGameFactory = new EpsilonCivFactory(strongerDefenderStubEpsilonCiv);
        GameFactory genericGameFactory = new EpsilonCivFactory(); // could pass in GenericUnitAttacking or not since constructor
        // function is overloaded
        genericGame = new GameImpl(genericGameFactory); // use for main implementation testing
        strongerAttackerGame = new GameImpl(strongerAttackerStubGameFactory);
        strongerDefenderGame = new GameImpl(strongerDefenderStubGameFactory);
    }

    // a test stub for setting the defensive and attacking strengths
    // test that in a generic game, a winner returns null if nothing happens.
    @Test
    public void NoCurrentWinnerYieldsNullPlayer(){
        Player winner = genericGame.getWinner();
        assertThat(winner, is(nullValue()));
        // ensure that the successfulPlayerAttacks map is not zero
        int numRedWins = genericGame.playerSuccessfulAttacks.get(Player.RED);
        assertThat(numRedWins, is(0));
    }

    // Show that a successful attack increases the player's win count
    // Defense was set to be higher than defense in the stub
    @Test
    public void AttackIsResolvedAsWin(){
        // attacking unit - archer at 0,0 red
        Position redArcherPos = new Position(0,0);
        // defending unit - legion at 1,2 blue
        Position blueLegionPos = new Position(1,2);

        // get the status of the win for Red player
        int numRedWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.RED);
        // fight
        boolean didRedWin = strongerAttackerGame.moveUnit(redArcherPos, blueLegionPos);
        assertThat(didRedWin, is(true));
        // test that the hash map has incremented
        int updatedNumRedWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.RED);
        assertThat(updatedNumRedWins > numRedWins, is(true));
    }

    // show that with three successful attacks, a Player has won the whole game
    @Test
    public void ThreeSuccessfulAttacksYieldsWin(){
        // there are 5 Red player units on the board now using the
        // use the strongerAttackerWithManyUnitsGame game
        strongerAttackerGame.units.put(new Position(2,2), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerAttackerGame.units.put(new Position(2,3), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerDefenderGame.units.put(new Position(2,4), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerAttackerGame.units.put(new Position(3,0), new UnitImpl((GameConstants.ARCHER), Player.BLUE));
        Position redArcher1Pos = new Position(0,0);
        Position redArcher2Pos = new Position(2,2);
        Position redArcher3Pos = new Position(2,3);
        Position blueArcherPos = new Position(3,0); // Blue will be doing the attacking

        // no winner to start
        boolean didBlueWin = strongerAttackerGame.moveUnit(blueArcherPos, redArcher1Pos);
        didBlueWin = strongerAttackerGame.moveUnit(redArcher1Pos, redArcher2Pos);
        didBlueWin = strongerAttackerGame.moveUnit(redArcher2Pos, redArcher3Pos);

        int blueWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.BLUE);
        Player winner = strongerAttackerGame.getWinner();

        // assert the following
        assertThat(winner, is(Player.BLUE));
        assertThat(blueWins, is(3));
    }

    // TODO: need to change the last two test case names to reflect what is happening

    // An attacker with > attacking strength beats defender
    @Test
    public void strongerAttackerBeatsWeakDefender(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = strongerAttackerGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
    }

    // An attacker with < attacking strength loses to defender
    @Test
    public void strongerDefenderBeatsWeakAttacker(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = strongerDefenderGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
    }

}