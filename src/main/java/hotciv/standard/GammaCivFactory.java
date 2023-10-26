package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.*;

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
}
