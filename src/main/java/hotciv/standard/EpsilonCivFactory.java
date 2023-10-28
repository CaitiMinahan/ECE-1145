package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.*;

public class EpsilonCivFactory implements GameFactory {
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
        return new AlternatingWinnerStrategy();
    }

    @Override
    public UnitAction createUnitAction() {
        return new EpsilonCivUnitAction();
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new EpsilonCivPlayerSetup();
    }
}
