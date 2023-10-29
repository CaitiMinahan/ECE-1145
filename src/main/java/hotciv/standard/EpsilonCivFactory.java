package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.*;

public class EpsilonCivFactory implements GameFactory {
    // this override requires param
    public UnitAttacking unitAttacking;
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
