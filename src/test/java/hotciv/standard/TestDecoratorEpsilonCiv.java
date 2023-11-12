package hotciv.standard;

import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.Factories.EpsilonCivFactory;
import hotciv.standard.Interfaces.GameFactory;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.TestStubs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDecoratorEpsilonCiv {
    private MutableGame strongerAttackerGame; // game object where the epsilonCivFactory has a strongerAttackerStub
    private MutableGame strongerDefenderGame; // game object where the epsilonCivFactory has a strongerDefenderStub
    private MutableGame transcribedGame;
    private MutableGame attackerHasTerrainAdvantageGame;
    private MutableGame defenderHasTerrainAdvantageGame;
    private MutableGame attackerHasMoreNeighborsGame;
    private MutableGame defenderHasMoreNeighborsGame;

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
        GameFactory strongerAttackerStubGameFactory = new EpsilonCivFactory(strongerAttackerStubEpsilonCiv);
        GameFactory strongerDefenderStubGameFactory = new EpsilonCivFactory(strongerDefenderStubEpsilonCiv);
        GameFactory genericGameFactory = new EpsilonCivFactory(); // could pass in GenericUnitAttacking or not since constructor
        GameFactory attackerHasTerrainAdvantageStubGameFactory = new EpsilonCivFactory(attackerHasTerrainAdvantageStubEpsilonCiv);
        GameFactory defenderHasTerrainAdvantageStubGameFactory = new EpsilonCivFactory(defenderHasTerrainAdvantageStubEpsilonCiv);
        GameFactory attackerHasMoreNeighborsStubGameFactory = new EpsilonCivFactory(attackerHasMoreNeighborsStubEpsilonCiv);
        GameFactory defenderHasMoreNeighborsStubGameFactory = new EpsilonCivFactory(defenderHasMoreNeighborsStubEpsilonCiv);

        // function is overloaded
        transcribedGame = new GameDecorator(new GameImpl(genericGameFactory));
        strongerAttackerGame = new GameDecorator(new GameImpl(strongerAttackerStubGameFactory));
        strongerDefenderGame = new GameDecorator(new GameImpl(strongerDefenderStubGameFactory));
        attackerHasTerrainAdvantageGame = new GameDecorator(new GameImpl(attackerHasTerrainAdvantageStubGameFactory));
        defenderHasTerrainAdvantageGame = new GameDecorator(new GameImpl(defenderHasTerrainAdvantageStubGameFactory));
        attackerHasMoreNeighborsGame = new GameDecorator(new GameImpl(attackerHasMoreNeighborsStubGameFactory));
        defenderHasMoreNeighborsGame = new GameDecorator(new GameImpl(defenderHasMoreNeighborsStubGameFactory));
    }
    // Show that a successful attack increases the player's win count
    // Defense was set to be higher than defense in the stub
    @Test
    public void AttackIsResolvedAsWin(){
        System.out.println("Start of Test 1 \n");
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
        System.out.println("\n End of Test 1 \n");
    }
    // show that with three successful attacks, a Player has won the whole game
    @Test
    public void ThreeSuccessfulAttacksYieldsWin(){
        System.out.println("Start of Test 2 \n");
        // there are 5 Red player units on the board now using the
        // use the strongerAttackerWithManyUnitsGame game
        strongerAttackerGame.units.put(new Position(2,2), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerAttackerGame.units.put(new Position(2,3), new UnitImpl((GameConstants.ARCHER), Player.RED));
        strongerAttackerGame.units.put(new Position(2,4), new UnitImpl((GameConstants.ARCHER), Player.RED));
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
        System.out.println("\n End of Test 2 \n");
    }

    // TODO: need to change the last two test case names to reflect what is happening

    // An attacker with > attacking strength beats defender
    @Test
    public void strongerAttackerBeatsWeakDefender(){
        System.out.println("Start of Test 3 \n");
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = strongerAttackerGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
        System.out.println("\n End of Test 3 \n");
    }

    // An attacker with < attacking strength loses to defender
    @Test
    public void strongerDefenderBeatsWeakAttacker(){
        System.out.println("Start of Test 4 \n");
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = strongerDefenderGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
        System.out.println("\n End of Test 4 \n");
    }

    // want to test with stub where the defensive and attacking strengths are equal but the terrain of attacker is advantage
    @Test
    public void AttackerHasTerrainAdvantage(){
        System.out.println("Start of Test 5 \n");
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = attackerHasTerrainAdvantageGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
        System.out.println("\n End of Test 5 \n");
    }
    // same but reverse case
    @Test
    public void DefenderHasTerrainAdvantage(){
        System.out.println("Start of Test 6 \n");
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = defenderHasTerrainAdvantageGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
        System.out.println("\n End of Test 6 \n");
    }

    // want to test where attacker and defender are equal but attacker has more friendly neighbor units
    @Test
    public void AttackerHasMoreNeighbors(){
        System.out.println("Start of Test 7 \n");
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = attackerHasMoreNeighborsGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(true));
        System.out.println("\n End of Test 7 \n");
    }
    // same but defender
    @Test
    public void DefenderHasMoreNeighbors(){
        System.out.println("Start of Test 8 \n");
        Position RedArcherPos = new Position(0,0);
        Position BlueLegionPos = new Position(1,2);
        // attack
        boolean didRedWin = defenderHasMoreNeighborsGame.moveUnit(RedArcherPos, BlueLegionPos);
        assertThat(didRedWin, is(false));
        System.out.println("\n End of Test 8 \n");
    }
}
