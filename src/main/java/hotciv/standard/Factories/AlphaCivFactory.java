package hotciv.standard.Factories;

import hotciv.standard.*;
import hotciv.standard.Interfaces.*;
public class AlphaCivFactory implements GameFactory {

//    @Override
//    public Game createGame() {
//        return new GameImpl(this);
//    }

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
        return new GenericUnitAction();
    }

    @Override
    public PlayerSetup createPlayerSetup() { return new GenericPlayerSetup();}

    @Override
    public ChangeProduction changeProduction() { return new GenericChangeProduction();}

    @Override
    public SetFocus setFocus() { return new GenericSetFocus();}
}
