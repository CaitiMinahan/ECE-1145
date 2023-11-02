package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Factories.ThetaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.UnitAction;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
public class TestThetaCiv {
    private GameImpl game;
    private UnitAction thetaUnitAction;
    private GameFactory gameFactory;

    @Before
    public void setUp() {
        gameFactory = new ThetaCivFactory();
        game = new GameImpl(gameFactory);
    }

    @Test
    public void createNewUFOUnitShouldReturnTypeCorrectly() {
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTypeString(), is("ufo"));
    }

    @Test
    public void UFOunitShouldHaveTwoMoves(){
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTravelDistace(), is(2));
    }

    @Test
    public void UFOunitShouldHaveOneMoveAfterTravel(){
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTravelDistace(), is(2));

        // put the ufo on the map
        Position ufoUnitPos1 = new Position(2,2);
        Position ufoUnitPos2 = new Position(2,3);
        Position ufoUnitPos3 = new Position(2,4);

        game.units.put(ufoUnitPos1, ufoUnit);
        game.moveUnit(ufoUnitPos1, ufoUnitPos2);
        assertThat(ufoUnit.getTravelDistace(), is(1));
    }

    @Test
    public void UFOunitCannotTravelThreeTimesPerTurn(){
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTravelDistace(), is(2));

        // put the ufo on the map
        Position ufoUnitPos1 = new Position(2,2);
        Position ufoUnitPos2 = new Position(2,3);
        Position ufoUnitPos3 = new Position(2,4);
        Position ufoUnitPos4 = new Position(2,5);

        game.units.put(ufoUnitPos1, ufoUnit);
        boolean canMove = game.moveUnit(ufoUnitPos1, ufoUnitPos2);
        assertThat(ufoUnit.getTravelDistace(), is(1));
        assertThat(canMove, is(true));

        canMove = game.moveUnit(ufoUnitPos2, ufoUnitPos3);
        assertThat(ufoUnit.getTravelDistace(), is(0));
        assertThat(canMove, is(true));
        // attempt illegal 3rd move
        canMove = game.moveUnit(ufoUnitPos3, ufoUnitPos4);
        assertThat(canMove, is(false));
    }

    @Test
    public void nonUFOUnitsCannotMoveMoreThanOnce(){
        // create a unit and some positions
        UnitImpl archerUnit = new UnitImpl(GameConstants.ARCHER, Player.BLUE);

        Position archerPos = new Position(3,1);
        Position archerPos2 = new Position(3,2);
        Position archerPos3 = new Position(3,3);

        game.units.put(archerPos, archerUnit);

        // move the archer
        boolean canMove = game.moveUnit(archerPos, archerPos2);
        assertThat(canMove, is(true));
        int getMoveCount = archerUnit.getTravelDistace();
        assertThat(getMoveCount, is(0));
        // try and move again
        canMove = game.moveUnit(archerPos2,archerPos3);
        assertThat(canMove, is(false));
    }
}
