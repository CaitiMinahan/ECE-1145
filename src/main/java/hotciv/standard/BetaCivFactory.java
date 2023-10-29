package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.*;

public class BetaCivFactory implements GameFactory {
    @Override
    public WorldLayout createWorldLayout() {
        // BetaCiv = generic layout
        return new GenericWorldLayout();
    }
    @Override
    public WorldAging createWorldAging() {
        // BetaCiv = betaCiv aging
        return new BetaCivWorldAging();
    }
    @Override
    public Winner createWinnerStrategy() {
        // BetaCiv = betaCiv winner
        return new BetaCivWinner();
    }
    @Override
    public UnitAction createUnitAction() {
        // BetaCiv = generic unit action
        return new GenericUnitAction();
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new GenericPlayerSetup();
    }
}
