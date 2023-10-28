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

    // a test stub for setting the defensive and attacking strengths
    public class FixedStrengthStrategy implements Unit{
        // want to return the strengths of the unit
        @Override
        public int getAttackingStrength(){
            return 100;
        }

        @Override
        public String getTypeString() {
            return null;
        }

        @Override
        public Player getOwner() {
            return getO;
        }

        @Override
        public int getMoveCount() {
            return 0;
        }

        @Override
        public int getDefensiveStrength() {
            return 1;
        }
    }

    @Test
    public void NoCurrentWinnerYieldsNullPlayer(){
        Player winner = game.getWinner();
        assertThat(winner, is(nullValue()));
    }

    // Show that a successful attack increases the player's win count
    @Test
    public void AttackIsResolvedAsWin(){
        // attacking unit - archer at 0,0 red
        Position redArcherPos = new Position(0,0);
        // defending unit - legion at 1,2 blue
        Position blueLegionPos = new Position(1,2);

        //set attacking strength -- stubs
        // set defending strength -- stubs
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
