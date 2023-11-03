package hotciv.standard.Factories;

import hotciv.standard.*;
import hotciv.standard.Interfaces.*;

public class SemiCivFactory implements GameFactory {

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

    @Override
    public ChangeProduction changeProduction() { return new GenericChangeProduction();}
}

