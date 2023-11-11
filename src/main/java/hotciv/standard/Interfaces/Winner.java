package hotciv.standard.Interfaces;
import hotciv.framework.*;
import hotciv.standard.GameImpl;

public interface Winner {
    Player gameWinner(MutableGame game);
}
