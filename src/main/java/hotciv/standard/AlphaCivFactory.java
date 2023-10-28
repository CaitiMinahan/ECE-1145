package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.*;

public class AlphaCivFactory implements GameFactory {

    // @Override
    // public Game createGame() {
    // return new GameImpl(this);
    // }

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
    public PlayerSetup setupPlayer() {
        return new GenericPlayerSetup();
    }
}