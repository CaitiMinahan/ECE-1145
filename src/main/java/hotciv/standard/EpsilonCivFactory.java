package hotciv.standard;

public class EpsilonCivFactory implements GameFactory {
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
        return new EpsilonCivWinner();
    }
    @Override
    public UnitAction createUnitAction() {
        // GammaCiv = GammaCiv unit action
        return new EpsilonCivUnitAction();
    }
    @Override
    public PlayerSetup setupPlayer() {
        return new EpsilonCivPlayerSetup();
    }

}
