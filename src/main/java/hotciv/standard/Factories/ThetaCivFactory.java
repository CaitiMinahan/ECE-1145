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
        return new ThetaCivUnitAction();
    }
    @Override
    public PlayerSetup createPlayerSetup() {
        return new GenericPlayerSetup();
    }

    public static class SemiCivFactory implements GameFactory {

        public UnitAttacking unitAttacking = new GenericUnitAttacking();
        @Override
        public WorldLayout createWorldLayout() {
            // SemiCiv = DeltaCiv layout
            return new DeltaCivWorldLayout();
        }
        @Override
        public WorldAging createWorldAging() {
            // SemiCiv = BetaCiv aging
            return new BetaCivWorldAging();
        }
        @Override
        public Winner createWinnerStrategy() {
            // SemiCiv = EpsilonCiv winner
            return new EpsilonCivWinner();
        }
        @Override
        public UnitAction createUnitAction() {
            // SemiCiv = Epsilon action
            return new EpsilonCivUnitAction(unitAttacking);
        }
        @Override
        public PlayerSetup createPlayerSetup() {
            return new GenericPlayerSetup();
        }
    }
}
