package hotciv.standard;

import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Unit;

// Set up the template to be implemented by the generic and gamma variants
public interface UnitAction {
    void performAction(UnitImpl currentUnit, Position p, GameImpl currentGame);
    public boolean moveUnit( Position from, Position to, GameImpl currentGame);

}
