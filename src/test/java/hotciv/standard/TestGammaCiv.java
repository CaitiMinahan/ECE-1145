package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.Factories.GammaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestGammaCiv {
    private MutableGame game;

    @Before
    public void setUp() {
        GameFactory gameFactory = new GammaCivFactory();
        game = new GameImpl(gameFactory);
    }
    @After
    public void breakDown() {
        game.cities.clear();
        game.tiles.clear();
        game.units.clear();
    }

    // ---- INTEGRATION TESTS ----- //
    // This test shows what happens to a settler when unit action is taken in delta
    // civ
    // TODO: make it so that the unit action no longer takes in the game and the unitActionCivType
    @Test
    public void settlerUnitsShouldBuildCities() {
        // Generic map of world has units in places like Iteration 1
        Position redSettlerPos = new Position(1, 1);
        Unit redSettlerUnit = game.getUnitAt(redSettlerPos);
        game.performUnitActionAt(redSettlerPos);
        assertThat(game.getCityAt(redSettlerPos).getOwner(), is(game.getPlayerInTurn()));
    }

    @Test
    public void archerUnitsShouldFortify() {
        // Archer will be in pos 0,0 for red
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        int currentDefensiveStrength = redArcherUnit.getDefensiveStrength();
        game.performUnitActionAt(redArcherPos);
        assertThat(game.getUnitAt(redArcherPos).getDefensiveStrength(), is(2 * currentDefensiveStrength));
    }

    @Test
    public void archerThatHasBeenFortifiedCannotMove() {
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        game.performUnitActionAt(redArcherPos);
        Position redArcherNewPosition = new Position(5, 5);
        // attempt to move unit now
        assertThat(game.moveUnit(redArcherPos, redArcherNewPosition), is(false));
    }

    @Test
    public void fortifiedArcherUnitsShouldRevert() {
        // archer will still be in pos 0,0
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        int currentDefensiveStrength = redArcherUnit.getDefensiveStrength();
        game.performUnitActionAt(redArcherPos);
        // do this again
        assertThat(game.getUnitAt(redArcherPos).getDefensiveStrength(), is(2 * currentDefensiveStrength));
        game.performUnitActionAt(redArcherPos);
        assertThat(game.getUnitAt(redArcherPos).getDefensiveStrength(), is(currentDefensiveStrength));
    }

    @Test
    public void fortifiedArcherUnitsShouldNotMove() {
        // archer will still be in pos 0,0
        Position redArcherPos = new Position(0, 0);
        Position redArcherPosNew = new Position(5, 5);

        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        game.performUnitActionAt(redArcherPos);
        // do this again
        assertThat(game.moveUnit(redArcherPos, redArcherPosNew), is(false));
        game.performUnitActionAt(redArcherPos);
        // attempt to move
        assertThat(game.moveUnit(redArcherPos, redArcherPosNew), is(true));
    }

    // ----- UNIT TESTS ----- //
    @Test
    public void getUnitActionTypeShouldBeGamma(){
        String newUnit = game.getUnitActionStringType();
        assertThat(newUnit,is("GammaCivUnitAction"));
        }
    @Test
    public void getUnitAtTileShouldBeSame(){
        // get the known units in specific locations
        Position redArcherPos = new Position(0,0);
        Unit oldUnit = game.getUnitAt(redArcherPos);
        Position toPos = new Position(5,5);
        // attempt to move the unit
        boolean didMove = game.moveUnit(redArcherPos, toPos);
        Unit newUnit = null;
        newUnit = game.getUnitAt(toPos);
        // check that the specific unit exists there now
        assertThat(oldUnit, is(newUnit));
    }

    // added test case for iteration 7
    // test invalid move for canMove == false
    @Test
    public void invalidMoveForCanMoveIsFalse(){
        Position newArcherPos = new Position(4,4);
        Position redArcherPos = new Position(0,0);
        Unit redArcher = game.getUnitAt(redArcherPos); // as per game setup

        ((UnitImpl) redArcher).setCanMove(false);

        assertThat(((UnitImpl) redArcher).getCanMove(), is(false));

        // should be unable to move since canMove = false
        assertThat(game.moveUnit(redArcherPos, newArcherPos), is(false));

    }

    // testing the observer pattern
    // these tests will use methods from the GameObserverSpy to make sure the Observer is interacting with the game
    // the Observer should be notified whenever there is a state change
    @Test
    public void testWorldChangedAt() {
        // Setup: Add an observer
        GameObserverSpy observer = new GameObserverSpy();
        game.addObserver(observer);

        Position observerFrom = new Position(0, 0);
        Position observerTo = new Position(3, 3);

        // Action: Make a change in the world (e.g., move a unit)
        assertThat(game.moveUnit(observerFrom, observerTo), is(true));

        // Assertion: Verify that the observer's worldChangedAt method was called
        assertTrue(observer.worldChangedAtCalled);
    }

    @Test
    public void testTurnEnds() {
        // Setup: Add an observer
        GameObserverSpy observer = new GameObserverSpy();
        game.addObserver(observer);

        // Action: Move to the end of the turn
        game.endOfTurn();

        // Assertion: Verify that the observer's turnEnds method was called
        assertTrue(observer.turnEndsCalled);
    }

    @Test
    public void testTileFocusChangedAt() {
        // Setup: Add an observer
        GameObserverSpy observer = new GameObserverSpy();
        game.addObserver(observer);

        // Action: Change the focus to a tile
        game.setTileFocus(new Position(2, 2));

        // Assertion: Verify that the observer's tileFocusChangedAt method was called
        assertTrue(observer.tileFocusChangedAtCalled);
    }

}
