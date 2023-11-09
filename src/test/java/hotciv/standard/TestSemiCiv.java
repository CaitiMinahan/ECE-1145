package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.Factories.*;
import hotciv.standard.Interfaces.*;
import hotciv.standard.TestStubs.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
public class TestSemiCiv {

    // Game and GameFactory Setup
    private MutableGame game;
    private GameFactory gameFactory;

    // Setup: SPECIFIC FOR EPSILONCIV
    private GameImpl strongerAttackerGame; // game object where the epsilonCivFactory has a strongerAttackerStub
    private GameImpl strongerDefenderGame; // game object where the epsilonCivFactory has a strongerDefenderStub
    private GameImpl attackerHasTerrainAdvantageGame;
    private GameImpl defenderHasTerrainAdvantageGame;
    private GameImpl attackerHasMoreNeighborsGame;
    private GameImpl defenderHasMoreNeighborsGame;


    // variables for the generic unitAttack and Stubs

    // StrongerAttacker implementation of UnitAttacking
    private StrongerAttackerStubEpsilonCiv strongerAttackerStubEpsilonCiv = new StrongerAttackerStubEpsilonCiv();
    // StrongerDefender implementation of UnitAttacking
    private StrongerDefenderStubEpsilonCiv strongerDefenderStubEpsilonCiv = new StrongerDefenderStubEpsilonCiv();

    // Attacker has terrain advantage
    private AttackerHasTerrainAdvantageStubEpsilonCiv attackerHasTerrainAdvantageStubEpsilonCiv = new AttackerHasTerrainAdvantageStubEpsilonCiv();
    // Defender has terrain advantage
    private DefenderHasTerrainAdvantageStubEpsilonCiv defenderHasTerrainAdvantageStubEpsilonCiv = new DefenderHasTerrainAdvantageStubEpsilonCiv();
    // Attacker has neighbor advantage
    private AttackerHasMoreNeighborsStubEpsilonCiv attackerHasMoreNeighborsStubEpsilonCiv = new AttackerHasMoreNeighborsStubEpsilonCiv();
    // Defender has neighbor advantage
    private DefenderHasMoreNeighborsStubEpsilonCiv defenderHasMoreNeighborsStubEpsilonCiv = new DefenderHasMoreNeighborsStubEpsilonCiv();

    @Before
    public void setUp() {

        // for each stub, create a new instantiation
        //GameFactory genericGameFactory = new EpsilonCivFactory(); // could pass in GenericUnitAttacking or not since constructor
        GameFactory strongerAttackerStubGameFactory = new EpsilonCivFactory(strongerAttackerStubEpsilonCiv);
        GameFactory strongerDefenderStubGameFactory = new EpsilonCivFactory(strongerDefenderStubEpsilonCiv);
        GameFactory attackerHasTerrainAdvantageStubGameFactory = new EpsilonCivFactory(attackerHasTerrainAdvantageStubEpsilonCiv);
        GameFactory defenderHasTerrainAdvantageStubGameFactory = new EpsilonCivFactory(defenderHasTerrainAdvantageStubEpsilonCiv);
        GameFactory attackerHasMoreNeighborsStubGameFactory = new EpsilonCivFactory(attackerHasMoreNeighborsStubEpsilonCiv);
        GameFactory defenderHasMoreNeighborsStubGameFactory = new EpsilonCivFactory(defenderHasMoreNeighborsStubEpsilonCiv);

        // function is overloaded
        //genericGame = new GameImpl(genericGameFactory); // use for main implementation testing
        strongerAttackerGame = new GameImpl(strongerAttackerStubGameFactory);
        strongerDefenderGame = new GameImpl(strongerDefenderStubGameFactory);
        attackerHasTerrainAdvantageGame = new GameImpl(attackerHasTerrainAdvantageStubGameFactory);
        defenderHasTerrainAdvantageGame = new GameImpl(defenderHasTerrainAdvantageStubGameFactory);
        attackerHasMoreNeighborsGame = new GameImpl(attackerHasMoreNeighborsStubGameFactory);
        defenderHasMoreNeighborsGame = new GameImpl(defenderHasMoreNeighborsStubGameFactory);

        gameFactory = new SemiCivFactory();
        game = new GameImpl(gameFactory);
    }

    // Testing BetaCiv's Aging Algorithm
    @Test
    public void shouldBe100BCAfter39Turns() {
        // Between 4000 BC and 100 BC, age should increase by 100
        // BC is represented by a negative number
        for (int i = 0; i < 39; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(-100));
    }
    @Test
    public void shouldBe1BCAfter100BC() {
        for (int i = 0; i < 40; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(-1));
    }
    @Test
    public void shouldBe1ADAfter1BC() {
        for (int i = 0; i < 41; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1));
    }
    @Test
    public void shouldBe50ADAfter1AD() {
        for (int i = 0; i < 42; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(50));
    }
    @Test
    public void shouldBe1750After76Rounds() {
        // After the year 50, age should increase by 50
        for (int i = 0; i < 76; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1750));
    }
    @Test
    public void shouldBe1900After82Rounds() {
        // After the year 1750, age should increase by 25
        for (int i = 0; i < 82; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1900));
    }
    @Test
    public void shouldBe1970After96Rounds() {
        // After the year 1900, age should increase by 5
        for (int i = 0; i < 96; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1970));
    }
    @Test
    public void shouldBe1980After106Rounds() {
        // After the year 1970, age should increase by 1
        for (int i = 0; i < 106; i++){
            game.endOfTurn();
            game.endOfTurn();
        }
        assertThat(game.getAge(), is(1980));
    }

    // Testing DeltaCiv's World Layout
    @Test
    public void setDeltaCivWorldLayoutCorrectlyRED() {
        Position RedPlayerCity = new Position(8, 12);
        assertThat(game.getCityAt(RedPlayerCity).getOwner(), is(Player.RED));
    }

    @Test
    public void setDeltaCivWorldLayoutCorrectlyBLUE() {
        // End the turn, and it should become BLUE player's turn
        game.endOfTurn();

        Position BluePlayerCity = new Position(4, 5);

        assertThat(game.getPlayerInTurn(), is(Player.BLUE));
        assertThat(game.getCityAt(BluePlayerCity).getOwner(), is(Player.BLUE));
    }

    // EpsilonCiv's Tests
    @Test
    public void NoCurrentWinnerYieldsNullPlayer(){
        Player winner = game.getWinner();
        assertThat(winner, is(nullValue()));
        // ensure that the successfulPlayerAttacks map is not zero
        int numRedWins = ((GameImpl)game).playerSuccessfulAttacks.get(Player.RED);
        assertThat(numRedWins, is(0));
    }

    // Show that a successful attack increases the player's win count
    // Defense was set to be higher than defense in the stub
    @Test
    public void AttackIsResolvedAsWin(){
        // attacking unit - archer at 0,0 red
        Position redArcherPos = new Position(0,0);
        // defending unit - legion at 1,2 blue
        Position blueLegionPos = new Position(1,2);

        // get the status of the win for Red player
        int numRedWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.RED);
        // fight
        boolean didRedWin = strongerAttackerGame.moveUnit(redArcherPos, blueLegionPos);
        assertThat(didRedWin, is(true));
        // test that the hash map has incremented
        int updatedNumRedWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.RED);
        assertThat(updatedNumRedWins > numRedWins, is(true));
    }

    // show that with three successful attacks, a Player has won the whole game
    @Test
    public void ThreeSuccessfulAttacksYieldsWin(){
        // there are 5 Red player units on the board now using the
        // use the strongerAttackerWithManyUnitsGame game
        strongerAttackerGame.units.put(new Position(2,2), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerAttackerGame.units.put(new Position(2,3), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerDefenderGame.units.put(new Position(2,4), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerAttackerGame.units.put(new Position(3,0), new UnitImpl((GameConstants.ARCHER), Player.BLUE));
        Position redArcher1Pos = new Position(0,0);
        Position redArcher2Pos = new Position(2,2);
        Position redArcher3Pos = new Position(2,3);
        Position blueArcherPos = new Position(3,0); // Blue will be doing the attacking

        // no winner to start
        boolean didBlueWin = strongerAttackerGame.moveUnit(blueArcherPos, redArcher1Pos);
        didBlueWin = strongerAttackerGame.moveUnit(redArcher1Pos, redArcher2Pos);
        didBlueWin = strongerAttackerGame.moveUnit(redArcher2Pos, redArcher3Pos);

        int blueWins = strongerAttackerGame.playerSuccessfulAttacks.get(Player.BLUE);
        Player winner = strongerAttackerGame.getWinner();

        // assert the following
        assertThat(winner, is(Player.BLUE));
        assertThat(blueWins, is(3));
    }

    // TODO: need to change the last two test case names to reflect what is happening

    // An attacker with > attacking strength beats defender
    @Test
    public void strongerAttackerBeatsWeakDefender(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = strongerAttackerGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
    }

    // An attacker with < attacking strength loses to defender
    @Test
    public void strongerDefenderBeatsWeakAttacker(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = strongerDefenderGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
    }

    // want to test with stub where the defensive and attacking strengths are equal but the terrain of attacker is advantage
    @Test
    public void AttackerHasTerrainAdvantage(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = attackerHasTerrainAdvantageGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
    }
    // same but reverse case
    @Test
    public void DefenderHasTerrainAdvantage(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = defenderHasTerrainAdvantageGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
    }

    // want to test where attacker and defender are equal but attacker has more friendly neighbor units
    @Test
    public void AttackerHasMoreNeighbors(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = attackerHasMoreNeighborsGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
    }
    // same but defender
    @Test
    public void DefenderHasMoreNeighbors(){
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = defenderHasMoreNeighborsGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
    }
}
