package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.Factories.ThetaCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.TestStubs.StrongerAttackerStubEpsilonCiv;
import hotciv.standard.TestStubs.StrongerDefenderStubEpsilonCiv;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDecoratorThetaCiv {
    private MutableGame transcribedGame;
    private MutableGame strongerAttackerGame; // this is a test stub from last iteration
    private MutableGame strongerDefenderGame; // this is a test stub from last iteration

    // factories for the attacking stubs
    private StrongerAttackerStubEpsilonCiv strongerAttackerStubThetaCiv = new StrongerAttackerStubEpsilonCiv();
    private StrongerDefenderStubEpsilonCiv strongerDefenderStubThetaCiv = new StrongerDefenderStubEpsilonCiv();

    @Before
    public void setUp() {

        GameFactory gameFactory = new ThetaCivFactory();
        // additional game factories
        GameFactory strongerAttackerStubGameFactory = new ThetaCivFactory(strongerAttackerStubThetaCiv);
        GameFactory strongerDefenderStubGameFactory = new ThetaCivFactory(strongerDefenderStubThetaCiv);

        transcribedGame = new GameDecorator(new GameImpl(gameFactory));
        // additional game modes
        strongerAttackerGame = new GameDecorator(new GameImpl(strongerAttackerStubGameFactory));
        strongerDefenderGame = new GameDecorator(new GameImpl(strongerDefenderStubGameFactory));

        //transcribedGame.units.clear();
        //transcribedGame.cities.clear();
        //transcribedGame.tiles.clear();
    }
    @After
    public void breakDown() {
        transcribedGame.cities.clear();
        transcribedGame.tiles.clear();
        transcribedGame.units.clear();
        strongerAttackerGame.cities.clear();
        strongerAttackerGame.tiles.clear();
        strongerAttackerGame.units.clear();
    }
    @Test
    public void UFOunitShouldHaveOneMoveAfterTravel(){
        System.out.println("Start of Test 1 \n");
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTravelDistace(), is(2));

        // put the ufo on the map
        Position ufoUnitPos1 = new Position(2,2);
        Position ufoUnitPos2 = new Position(2,3);
        Position ufoUnitPos3 = new Position(2,4);

        transcribedGame.units.put(ufoUnitPos1, ufoUnit);
        transcribedGame.moveUnit(ufoUnitPos1, ufoUnitPos2);
        assertThat(ufoUnit.getTravelDistace(), is(1));
        System.out.println("End of Test 1 \n");
    }

    @Test
    public void UFOunitCannotTravelThreeTimesPerTurn(){
        System.out.println("Start of Test 2 \n");
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTravelDistace(), is(2));

        // put the ufo on the map
        Position ufoUnitPos1 = new Position(2,2);
        Position ufoUnitPos2 = new Position(2,3);
        Position ufoUnitPos3 = new Position(2,4);
        Position ufoUnitPos4 = new Position(2,5);

        transcribedGame.units.put(ufoUnitPos1, ufoUnit);
        boolean canMove = transcribedGame.moveUnit(ufoUnitPos1, ufoUnitPos2);
        assertThat(ufoUnit.getTravelDistace(), is(1));
        assertThat(canMove, is(true));

        canMove = transcribedGame.moveUnit(ufoUnitPos2, ufoUnitPos3);
        assertThat(ufoUnit.getTravelDistace(), is(0));
        assertThat(canMove, is(true));
        // attempt illegal 3rd move
        canMove = transcribedGame.moveUnit(ufoUnitPos3, ufoUnitPos4);
        assertThat(canMove, is(false));
        System.out.println("End of Test 2 \n");
    }

    @Test
    public void nonUFOUnitsCannotMoveMoreThanOnce(){
        System.out.println("Start of Test 3 \n");
        // create a unit and some positions
        UnitImpl archerUnit = new UnitImpl(GameConstants.ARCHER, Player.BLUE);

        Position archerPos = new Position(3,1);
        Position archerPos2 = new Position(3,2);
        Position archerPos3 = new Position(3,3);

        transcribedGame.units.put(archerPos, archerUnit);

        // move the archer
        boolean canMove = transcribedGame.moveUnit(archerPos, archerPos2);
        assertThat(canMove, is(true));
        int getMoveCount = archerUnit.getTravelDistace();
        assertThat(getMoveCount, is(0));
        // try and move again
        canMove = transcribedGame.moveUnit(archerPos2,archerPos3);
        assertThat(canMove, is(false));
        System.out.println("End of Test 3 \n");
    }

    // Need tests for the following:
    /* UFO can fly over city and not conquer it if there are no units on it
     * UDO can perform its action, abducts from city below
     *   This makes the city lose a population
     * if city reaches 0 the city is removed. Extra terrain stuff. */

    @Test
    public void ufoCanHoverOverCityWithoutConqueringIfNoUnitsInCity(){
        System.out.println("Start of Test 4 \n");
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        transcribedGame.units.put(ufoHomePos, ufoUnit);

        // make a city with no units in it
        CityImpl emptyCity = new CityImpl(Player.BLUE);
        Position cityPosition = new Position(4,1);
        transcribedGame.cities.put(cityPosition, emptyCity);

        // city is new city with no units on it
        boolean canMove = transcribedGame.moveUnit(ufoHomePos, cityPosition);
        assertThat(canMove, is(true));

        // show that the city is still owned by blue player
        Player cityOwner = emptyCity.getOwner();
        assertThat(cityOwner, is(Player.BLUE));
        System.out.println("End of Test 4 \n");
    }

    // Stubs are allowed to be used here, lets use the stronger attacker stub from the epsilonCiv
    @Test
    public void ufoHoverOverCityWinsBattle(){
        System.out.println("Start of Test 5 \n");
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
        System.out.println("End of Test 5 \n");
    }

    // want to use the stronger defender stub now
    @Test
    public void ufoHoverOverCityLossesBattle(){
        System.out.println("Start of Test 6 \n");
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
        System.out.println("End of Test 6 \n");
    }

    @Test
    public void UFOCanAbductCitizenFromCityNonZero(){
        System.out.println("Start of Test 7 \n");
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);

        // make a city with no units in it
        CityImpl newCity = new CityImpl(Player.BLUE);
        Position cityPosition = new Position(4,1);
        // add population to the city
        newCity.setPopulationSize(100);

        // need to add the unit to the hash map
        transcribedGame.units.put(ufoHomePos, ufoUnit);
        transcribedGame.cities.put(cityPosition, newCity);

        // allow the UFO to abduct a person
        boolean didUFOMove = transcribedGame.moveUnit(ufoHomePos, cityPosition);
        assertThat(didUFOMove, is(true));
        // use the ufo's action
        transcribedGame.performUnitActionAt(cityPosition);
        // check the population
        assertThat(newCity.getPopulationSize(), is(99));
        System.out.println("End of Test 7 \n");
    }

    @Test
    public void UFOCanAbductCitizenFromCityWithPopOne(){
        System.out.println("Start of Test 8 \n");
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        // add the unit to the map
        transcribedGame.units.put(ufoHomePos, ufoUnit);

        // make a city with no units in it
        CityImpl newCity = new CityImpl(Player.BLUE);
        Position cityPosition = new Position(4,1);
        // add population to the city
        newCity.setPopulationSize(1);
        // add the city to the hashmap
        transcribedGame.cities.put(cityPosition, newCity);

        // allow the UFO to abduct a person
        boolean didUFOMove = transcribedGame.moveUnit(ufoHomePos, cityPosition);
        assertThat(didUFOMove, is(true));
        // use the ufo's action
        transcribedGame.performUnitActionAt(cityPosition);
        // check the population
        boolean getCity = false;
        City retrievedCity = transcribedGame.cities.get(cityPosition);
        if(retrievedCity != null)
            getCity = true;
        assertThat(getCity, is(false)); // proves that the city was deleted
        System.out.println("End of Test 8 \n");
    }

    @Test
    public void ufoCanChangeForrestToPlains(){
        System.out.println("Start of Test 9 \n");
        // create UFO
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        // add to the hash map
        transcribedGame.units.put(ufoHomePos, ufoUnit);

        // get the position of a forrest
        Position forrestTilePos = new Position(4,5);
        TileImpl forrestTile = new TileImpl(GameConstants.FOREST);
        transcribedGame.tiles.put(forrestTilePos, forrestTile);

        // move the ufo to that tile
        transcribedGame.moveUnit(ufoHomePos, forrestTilePos);

        // perform unit action
        transcribedGame.performUnitActionAt(forrestTilePos);
        // notice the terrain type change
        String terrainType = transcribedGame.tiles.get(forrestTilePos).getTypeString();
        assertThat(terrainType, is("plains"));
        System.out.println("End of Test 9 \n");
    }

    @Test
    public void ufoCantChangeOtherTerrains(){
        System.out.println("Start of Test 10 \n");
        // create UFO
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        // put the ufo on the map
        Position ufoHomePos = new Position(2,2);
        // add to the units hash map
        transcribedGame.units.put(ufoHomePos, ufoUnit);

        // get the positions of different tiles
        Position plainsTilePos = new Position(4,1);
        Position oceanTilePos = new Position(4,2);
        Position hillsTilePos = new Position(4,3);
        Position mountainTilePos = new Position(4,4);

        TileImpl plainsTile = new TileImpl(GameConstants.PLAINS);
        TileImpl oceanTile = new TileImpl(GameConstants.OCEANS);
        TileImpl hillsTile = new TileImpl(GameConstants.HILLS);
        TileImpl mountainTile = new TileImpl(GameConstants.MOUNTAINS);

        // update the tile hash map
        transcribedGame.tiles.put(plainsTilePos, plainsTile);
        transcribedGame.tiles.put(oceanTilePos, oceanTile);
        transcribedGame.tiles.put(hillsTilePos, hillsTile);
        transcribedGame.tiles.put(mountainTilePos, mountainTile);


        // move the ufo to that tile
        transcribedGame.moveUnit(ufoHomePos, plainsTilePos);
        // perform unit action
        transcribedGame.performUnitActionAt(plainsTilePos);
        // notice the terrain type change
        String terrainType = transcribedGame.tiles.get(plainsTilePos).getTypeString();
        assertThat(terrainType, is("plains"));

        // move the ufo to that tile
        transcribedGame.moveUnit(plainsTilePos, oceanTilePos);
        // perform unit action
        transcribedGame.performUnitActionAt(oceanTilePos);
        // notice the terrain type change
        terrainType = transcribedGame.tiles.get(oceanTilePos).getTypeString();
        assertThat(terrainType, is("ocean"));

        // UFO cant move more than twice
        UnitImpl ufo2 = new UnitImpl(GameConstants.UFO, Player.RED);
        // add to the units hash map
        transcribedGame.units.put(hillsTilePos, ufo2);

        // perform unit action
        transcribedGame.performUnitActionAt(hillsTilePos);
        // notice the terrain type change
        terrainType = transcribedGame.tiles.get(hillsTilePos).getTypeString();
        assertThat(terrainType, is("hills"));

        // move the ufo to that tile
        transcribedGame.moveUnit(hillsTilePos, mountainTilePos);
        // perform unit action
        transcribedGame.performUnitActionAt(mountainTilePos);
        // notice the terrain type change
        terrainType = transcribedGame.tiles.get(mountainTilePos).getTypeString();
        assertThat(terrainType, is("mountain"));
        System.out.println("End of Test 10 \n");
    }
}
