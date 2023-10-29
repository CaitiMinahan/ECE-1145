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
    }

    // Show that a successful attack increases the player's win count
    /*
    * Defense was set to be higher than defense in the stub
    * */
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
        Player winner =  strongerAttackerGame.getWinner();
        assertThat(didRedWin, is(true));
        // test that the hash map has incremented
        int updatedNumRedWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.RED);
        assertThat(numRedWins > updatedNumRedWins, is(true));
    }

    @Test
    public void ThreeSuccessfulAttacksYieldsWin(){

    }

    // TODO: need to change the last two test case names to reflect what is happening
    @Test
    public void combinedAttackStrength(){

    }

    @Test
    public void combinedDefensiveStrength(){

    }

}