package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.Winner;

public class GenericWinner implements Winner {
    @Override
    public Player gameWinner(GameImpl game){
        if(game.getAge() == -3000){
            return Player.RED;
        }
        return null;
    }
}
