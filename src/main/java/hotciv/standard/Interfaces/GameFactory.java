package hotciv.standard.Interfaces;

public interface GameFactory {
    // step 1 in refactoring for abstract factory: create an interface for the abstract factory
    // this will lay out the strategies we need to reference
    // so far, HotCiv has the following variant strategies:


    WorldLayout createWorldLayout();
    WorldAging createWorldAging();
    Winner createWinnerStrategy();
    UnitAction createUnitAction();
    PlayerSetup createPlayerSetup();
    ChangeProduction changeProduction();
    SetFocus setFocus();

    // these call the interfaces created for the variant classes (i.e., interfaces: WorldLayout, WorldAging, Winner, UnitAction)
    // the createX() methods will be called in the GameImpl constructor to reference the factories
}

// TODO: ask if we need this createGame()
//    Game createGame();
