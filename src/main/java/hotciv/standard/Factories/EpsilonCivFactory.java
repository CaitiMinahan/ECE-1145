package hotciv.standard.Factories;
import hotciv.framework.*;
import hotciv.standard.*;

public class EpsilonCivFactory implements GameFactory {
    public UnitAttacking unitAttacking;

    // default constructor with no params
    public EpsilonCivFactory(){
        this.unitAttacking = new GenericUnitAttacking();
    }
    public EpsilonCivFactory(UnitAttacking unitAttacking) {
        this.unitAttacking = unitAttacking;
    }

    // this override requires param
    @Override
    public WorldLayout createWorldLayout() {
        return new GenericWorldLayout();
    }
    @Override
    public WorldAging createWorldAging() {
        return new GenericWorldAging();
    }
    @Override
    public Winner createWinnerStrategy() {
        return new EpsilonCivWinner();
    }

    @Override
    public UnitAction createUnitAction() {
        return new EpsilonCivUnitAction(unitAttacking);
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new EpsilonCivPlayerSetup();
    }
}
