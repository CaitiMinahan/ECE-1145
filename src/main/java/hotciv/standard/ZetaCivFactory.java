package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.*;

public class ZetaCivFactory implements GameFactory {
    // step 2 in refactoring for abstract factory: create concrete factory classes for each game variant
    // these concrete classes implement the GameFactory interface
    // also, these concrete factories should create the objects necessary for the variant's implementation
    @Override
    public WorldLayout createWorldLayout() {
        // Use the generic WorldLayout for ZetaCiv
        return new GenericWorldLayout();
    }

    @Override
    public WorldAging createWorldAging() {
        // Use the generic WorldAging for ZetaCiv
        return new GenericWorldAging();
    }

    @Override
    public Winner createWinnerStrategy() {
        // Create and return the Winner strategy for ZetaCiv
        return new AlternatingWinnerStrategy();
    }

    @Override
    public UnitAction createUnitAction() {
        // Use the generic UnitAction for ZetaCiv
        return new GenericUnitAction();
    }
    @Override
    public PlayerSetup createPlayerSetup() { return new GenericPlayerSetup();}
}
