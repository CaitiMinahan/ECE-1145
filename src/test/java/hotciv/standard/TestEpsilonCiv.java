package hotciv.standard;

import org.junit.Before;
import org.junit.Test;

/*
* This file will use Test Stubs to allow the game winner to be determined.
* */
public class TestEpsilonCiv {
    private GameImpl game;
    private WorldLayout epsilonCivWorldLayout;
    private Winner epsilonWinner;
    private UnitAction epsilonUnitAction;
    private GenericWorldAging epsilonWorldAging;

    @Before
    public void setUp() {
        epsilonCivWorldLayout = new GenericWorldLayout();
        epsilonUnitAction = new GammaCivUnitAction();
        // todo: Need to add in the EpsilonWinner strat
        epsilonWinner = new EpsilonWinner();
        epsilonWorldAging = new GenericWorldAging();
        game = new GameImpl(epsilonCivWorldLayout, epsilonWorldAging, epsilonWinner, epsilonUnitAction);
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
