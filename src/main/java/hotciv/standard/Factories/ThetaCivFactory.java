package hotciv.standard.Factories;
import hotciv.standard.*;
import hotciv.standard.Interfaces.*;

public class ThetaCivFactory implements GameFactory {

    public UnitAttacking unitAttacking;

    // default constructor with no params
    public ThetaCivFactory(){
        this.unitAttacking = new GenericUnitAttacking();
    }
    public ThetaCivFactory(UnitAttacking unitAttacking) {
        this.unitAttacking = unitAttacking;
    }
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
        return new GenericWinner();
    }
    @Override
    public UnitAction createUnitAction() {
        return new ThetaCivUnitAction(unitAttacking);
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new GenericPlayerSetup();
    }
    @Override
    public ChangeProduction changeProduction() { return new GenericChangeProduction();}


}
