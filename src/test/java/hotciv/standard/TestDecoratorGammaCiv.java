package hotciv.standard;

import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.standard.Factories.GammaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDecoratorGammaCiv {
    private MutableGame transcribedGame;

    @Before
    public void setUp() {
        GameFactory gameFactory = new GammaCivFactory();
        transcribedGame = new GameDecorator(new GameImpl(gameFactory));
    }
    @After
    public void breakDown() {
        transcribedGame.cities.clear();
        transcribedGame.tiles.clear();
        transcribedGame.units.clear();
    }

    // ---- INTEGRATION TESTS ----- //
    // This test shows what happens to a settler when unit action is taken in delta civ
    @Test
    public void settlerUnitsShouldBuildCities() {
        System.out.println("Start of Test 1 \n");
        // Generic map of world has units in places like Iteration 1
        Position redSettlerPos = new Position(1, 1);
        Unit redSettlerUnit = transcribedGame.getUnitAt(redSettlerPos);
        transcribedGame.performUnitActionAt(redSettlerPos);
        assertThat(transcribedGame.getCityAt(redSettlerPos).getOwner(), is(transcribedGame.getPlayerInTurn()));
        System.out.println("\n End of Test 1 \n");
    }

    @Test
    public void archerUnitsShouldFortify() {
        System.out.println("Start of Test 2 \n");
        // Archer will be in pos 0,0 for red
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = transcribedGame.getUnitAt(redArcherPos);
        int currentDefensiveStrength = redArcherUnit.getDefensiveStrength();
        transcribedGame.performUnitActionAt(redArcherPos);
        assertThat(transcribedGame.getUnitAt(redArcherPos).getDefensiveStrength(), is(2 * currentDefensiveStrength));
        System.out.println("\n End of Test 2 \n");
    }

    @Test
    public void archerThatHasBeenFortifiedCannotMove() {
        System.out.println("Start of Test 3 \n");
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = transcribedGame.getUnitAt(redArcherPos);
        transcribedGame.performUnitActionAt(redArcherPos);
        Position redArcherNewPosition = new Position(5, 5);
        // attempt to move unit now
        assertThat(transcribedGame.moveUnit(redArcherPos, redArcherNewPosition), is(false));
        System.out.println("\n End of Test 3 \n");
    }

    @Test
    public void fortifiedArcherUnitsShouldRevert() {
        System.out.println("Start of Test 4 \n");
        // archer will still be in pos 0,0
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = transcribedGame.getUnitAt(redArcherPos);
        int currentDefensiveStrength = redArcherUnit.getDefensiveStrength();
        transcribedGame.performUnitActionAt(redArcherPos);
        // do this again
        assertThat(transcribedGame.getUnitAt(redArcherPos).getDefensiveStrength(), is(2 * currentDefensiveStrength));
        transcribedGame.performUnitActionAt(redArcherPos);
        assertThat(transcribedGame.getUnitAt(redArcherPos).getDefensiveStrength(), is(currentDefensiveStrength));
        System.out.println("\n End of Test 4 \n");
    }

    @Test
    public void fortifiedArcherUnitsShouldNotMove() {
        System.out.println("Start of Test 5 \n");
        // archer will still be in pos 0,0
        Position redArcherPos = new Position(0, 0);
        Position redArcherPosNew = new Position(5, 5);

        Unit redArcherUnit = transcribedGame.getUnitAt(redArcherPos);
        transcribedGame.performUnitActionAt(redArcherPos);
        // do this again
        assertThat(transcribedGame.moveUnit(redArcherPos, redArcherPosNew), is(false));
        transcribedGame.performUnitActionAt(redArcherPos);
        // attempt to move
        assertThat(transcribedGame.moveUnit(redArcherPos, redArcherPosNew), is(true));
        System.out.println("\n End of Test 5 \n");
    }

    // ----- UNIT TESTS ----- //
    @Test
    public void getUnitAtTileShouldBeSame(){
        System.out.println("Start of Test 6 \n");
        // get the known units in specific locations
        Position redArcherPos = new Position(0,0);
        Unit oldUnit = transcribedGame.getUnitAt(redArcherPos);
        Position toPos = new Position(5,5);
        // attempt to move the unit
        boolean didMove = transcribedGame.moveUnit(redArcherPos, toPos);
        Unit newUnit = null;
        newUnit = transcribedGame.getUnitAt(toPos);
        // check that the specific unit exists there now
        assertThat(oldUnit, is(newUnit));
        System.out.println("\n End of Test 6 \n");
    }

    // added test case for iteration 7
    // test invalid move for canMove == false
    @Test
    public void invalidMoveForCanMoveIsFalse(){
        System.out.println("Start of Test 7 \n");
        Position newArcherPos = new Position(4,4);
        Position redArcherPos = new Position(0,0);
        Unit redArcher = transcribedGame.getUnitAt(redArcherPos); // as per transcribedGame setup

        ((UnitImpl) redArcher).setCanMove(false);

        assertThat(((UnitImpl) redArcher).getCanMove(), is(false));

        // should be unable to move since canMove = false
        assertThat(transcribedGame.moveUnit(redArcherPos, newArcherPos), is(false));
        System.out.println("\n End of Test 7 \n");
    }
}
