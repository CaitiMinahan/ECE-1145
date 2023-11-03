package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Factories.EpsilonCivFactory;
import hotciv.standard.Factories.ThetaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.UnitAction;
import hotciv.standard.TestStubs.StrongerAttackerStubEpsilonCiv;
import hotciv.standard.TestStubs.StrongerDefenderStubEpsilonCiv;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
public class TestThetaCiv {
    private GameImpl game;
    private GameImpl strongerAttackerGame; // this is a test stub from last iteration
    private GameImpl strongerDefenderGame; // this is a test stub from last iteration
    private UnitAction thetaUnitAction;
    private GameFactory gameFactory;

    // factories for the attacking stubs
    private StrongerAttackerStubEpsilonCiv strongerAttackerStubThetaCiv = new StrongerAttackerStubEpsilonCiv();
    private StrongerDefenderStubEpsilonCiv strongerDefenderStubThetaCiv = new StrongerDefenderStubEpsilonCiv();


    @Before
    public void setUp() {

        gameFactory = new ThetaCivFactory();
        // additional game factories
        GameFactory strongerAttackerStubGameFactory = new ThetaCivFactory(strongerAttackerStubThetaCiv);
        GameFactory strongerDefenderStubGameFactory = new ThetaCivFactory(strongerDefenderStubThetaCiv);

        game = new GameImpl(gameFactory);
        // additional game modes
        strongerAttackerGame = new GameImpl(strongerAttackerStubGameFactory);
        strongerDefenderGame = new GameImpl(strongerDefenderStubGameFactory);
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

    // Need tests for the following:
    /* UFO can fly over city and not conquer it if there are no units on it
    * UDO can perform its action, abducts from city below
    *   This makes the city lose a population
    * if city reaches 0 the city is removed. Extra terrain stuff. */

    @Test
    public void ufoCanHoverOverCityWithoutConqueringIfNoUnitsInCity(){
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        game.units.put(ufoHomePos, ufoUnit);

        // make a city with no units in it
        CityImpl emptyCity = new CityImpl(Player.BLUE);
        Position cityPosition = new Position(4,1);
        game.cities.put(cityPosition, emptyCity);

        // city is new city with no units on it
        boolean canMove = game.moveUnit(ufoHomePos, cityPosition);
        assertThat(canMove, is(true));

        // show that the city is still owned by blue player
        Player cityOwner = emptyCity.getOwner();
        assertThat(cityOwner, is(Player.BLUE));
    }

    // Stubs are allowed to be used here, lets use the stronger attacker stub from the epsilonCiv
    @Test
    public void ufoHoverOverCityWinsBattle(){
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        strongerAttackerGame.units.put(ufoHomePos, ufoUnit);

        // make a city with no units in it
        CityImpl newCity = new CityImpl(Player.BLUE);
        Position cityPosition = new Position(4,1);
        strongerAttackerGame.cities.put(cityPosition, newCity);

        // add units to the city
        UnitImpl blueLegion = new UnitImpl(GameConstants.LEGION, Player.BLUE);
        strongerAttackerGame.units.put(cityPosition, blueLegion);

        // city is new city with enemy units on it
        boolean canMove = strongerAttackerGame.moveUnit(ufoHomePos, cityPosition);
        assertThat(canMove, is(true)); // should be true because we are doing battle

        // show that the city is now conquered
        Player cityOwner = newCity.getOwner();
        assertThat(cityOwner, is(Player.RED));
    }

    // want to use the stronger defender stub now
    @Test
    public void ufoHoverOverCityLossesBattle(){
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        strongerDefenderGame.units.put(ufoHomePos, ufoUnit);

        // make a city with no units in it
        CityImpl newCity = new CityImpl(Player.BLUE);
        Position cityPosition = new Position(4,1);
        strongerDefenderGame.cities.put(cityPosition, newCity);

        // add units to the city
        UnitImpl blueLegion = new UnitImpl(GameConstants.LEGION, Player.BLUE);
        strongerDefenderGame.units.put(cityPosition, blueLegion);

        // city is new city with enemy units on it
        boolean canMove = strongerDefenderGame.moveUnit(ufoHomePos, cityPosition);
        assertThat(canMove, is(false)); // should be false because attacker is defeated

        // show that the city is still owned by blue player
        Player cityOwner = newCity.getOwner();
        assertThat(cityOwner, is(Player.BLUE));
    }
}
