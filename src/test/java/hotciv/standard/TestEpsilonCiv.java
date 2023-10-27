package hotciv.standard;

import hotciv.standard.*;
import hotciv.framework.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.lang.reflect.Method;

/*
* This file will use Test Stubs to allow the game winner to be determined.
* */
public class TestEpsilonCiv {
    private GameImpl game;
    private WorldLayout epsilonCivWorldLayout;
    private Winner epsilonCivWinner;
    private UnitAction epsilonUnitAction;
    private GenericWorldAging epsilonWorldAging;
    private PlayerSetup epsilonCivPlayerSetup;

    @Before
    public void setUp() {
        epsilonCivWorldLayout = new GenericWorldLayout();
        epsilonUnitAction = new GammaCivUnitAction();
        // todo: Need to add in the EpsilonWinner strat
        epsilonCivWinner = new EpsilonCivWinner();
        epsilonWorldAging = new GenericWorldAging();
        epsilonCivPlayerSetup = new EpsilonCivPlayerSetup();
        game = new GameImpl(epsilonCivWorldLayout, epsilonWorldAging, epsilonCivWinner, epsilonUnitAction, epsilonCivPlayerSetup);
    }

    @Test
    public void NoCurrentWinnerYieldsNullPlayer(){
        Player winner = game.getWinner();
        assertThat(winner, is(nullValue()));
    }

    @Test
    public void AttackIsResolvedAsWin(){

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
