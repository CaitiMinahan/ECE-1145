package hotciv.standard;

import hotciv.framework.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/*
* This file will use Test Stubs to allow the game winner to be determined.
* */
public class TestEpsilonCiv {
    private GameImpl game;
    private GameFactory gameFactory;

    @Before
    public void setUp() {
        gameFactory = new EpsilonCivFactory();
        game = new GameImpl(gameFactory);
    }

    // a test stub for setting the defensive and attacking strengths
//    public class FixedStrengthStrategy implements Unit{
//        // want to return the strengths of the unit
//        @Override
//        public int getAttackingStrength(){
//            return 100;
//        }
//
//        @Override
//        public String getTypeString() {
//            return null;
//        }
//
//        @Override
//        public Player getOwner() {
//            return Player.RED;
//        }
//
//        @Override
//        public int getMoveCount() {
//            return 0;
//        }
//
//        @Override
//        public int getDefensiveStrength() {
//            return 1;
//        }
// }

    // test stub should be the EpsilonCivUnitAction since there are so many things going on there
    public class fixedEpsilonUnitAction implements UnitAction{

        @Override
        public void performAction(UnitImpl currentUnit, Position p, GameImpl currentGame) {

        }

        @Override
        public boolean moveUnit(Position from, Position to, GameImpl currentGame) {
            return false;
        }

        @Override
        public void updateUnitMap(Position from, Position to, Unit unit_from, GameImpl currentGame) {
            // do nothing
        }
        public int getNumFriendlyTiles(Position from, GameImpl game){
            return 5; //hard coded value
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
        // fight
        boolean didRedWin = game.moveUnit(redArcherPos, blueLegionPos);
        Player winner =  game.getWinner();
        assertThat(didRedWin, is(true));
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