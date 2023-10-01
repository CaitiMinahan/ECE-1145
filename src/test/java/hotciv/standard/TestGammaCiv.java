package hotciv.standard;

import hotciv.framework.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestGammaCiv {
    private GameImpl game;
    private WorldLayout gammaCivWorldLayout;
    private Winner gammaWinner;
    private UnitAction gammaUnitAction;
    private GenericWorldAging gammaWorldAging;

    @Before
    public void setUp() {
        gammaCivWorldLayout = new GenericWorldLayout();
        gammaUnitAction = new GammaCivUnitAction();
        gammaWinner = new GenericWinner();
        gammaWorldAging = new GenericWorldAging();
        game = new GameImpl(gammaCivWorldLayout, gammaWorldAging, gammaWinner, gammaUnitAction);
    }

    // ---- INTEGRATION TESTS ----- //
    // This test shows what happens to a settler when unit action is taken in delta
    // civ
    @Test
    public void settlerUnitsShouldBuildCities() {
        // Generic map of world has units in places like Iteration 1
        Position redSettlerPos = new Position(1, 1);
        Unit redSettlerUnit = game.getUnitAt(redSettlerPos);
        game.takeUnitAction(redSettlerUnit, game, gammaUnitAction);
        assertThat(game.getCityAt(redSettlerPos).getOwner(), is(game.getPlayerInTurn()));
    }

    @Test
    public void archerUnitsShouldFortify() {
        // Archer will be in pos 0,0 for red
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        int currentDefensiveStrength = redArcherUnit.getDefensiveStrength();
        game.takeUnitAction(redArcherUnit, game, gammaUnitAction);
        assertThat(game.getUnitAt(redArcherPos).getDefensiveStrength(), is(2 * currentDefensiveStrength));
    }

    @Test
    public void archerThatHasBeenFortifiedCannotMove() {
        Position redArcherPos = new Position(0, 0);
        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        game.takeUnitAction(redArcherUnit, game, gammaUnitAction);
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
        game.takeUnitAction(redArcherUnit, game, gammaUnitAction);
        // do this again
        assertThat(game.getUnitAt(redArcherPos).getDefensiveStrength(), is(2 * currentDefensiveStrength));
        game.takeUnitAction(redArcherUnit, game, gammaUnitAction);
        assertThat(game.getUnitAt(redArcherPos).getDefensiveStrength(), is(currentDefensiveStrength));
    }

    @Test
    public void fortifiedArcherUnitsShouldNotMove() {
        // archer will still be in pos 0,0
        Position redArcherPos = new Position(0, 0);
        Position redArcherPosNew = new Position(5, 5);

        Unit redArcherUnit = game.getUnitAt(redArcherPos);
        game.takeUnitAction(redArcherUnit, game, gammaUnitAction);
        // do this again
        assertThat(game.moveUnit(redArcherPos, redArcherPosNew), is(false));
        game.takeUnitAction(redArcherUnit, game, gammaUnitAction);
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
}
