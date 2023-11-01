package hotciv.standard.Factories;
import hotciv.standard.*;
import hotciv.standard.Interfaces.*;

public class ThetaCivFactory implements GameFactory {
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
        return new GammaCivUnitAction();
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new GenericPlayerSetup();
    }
}
