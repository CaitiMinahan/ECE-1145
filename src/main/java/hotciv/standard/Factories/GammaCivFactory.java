package hotciv.standard.Factories;
import hotciv.standard.*;
import hotciv.standard.Interfaces.*;

public class GammaCivFactory implements GameFactory {
    @Override
    public WorldLayout createWorldLayout() {
        // GammaCiv = generic layout
        return new GenericWorldLayout();
    }
    @Override
    public WorldAging createWorldAging() {
        // GammaCiv = generic aging
        return new GenericWorldAging();
    }
    @Override
    public Winner createWinnerStrategy() {
        // GammaCiv = generic winner
        return new GenericWinner();
    }
    @Override
    public UnitAction createUnitAction() {
        // GammaCiv = GammaCiv unit action
        return new GammaCivUnitAction();
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new GenericPlayerSetup();
    }
    @Override
    public ChangeProduction changeProduction() { return new GenericChangeProduction();}
}
