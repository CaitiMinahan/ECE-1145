package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.PlayerSetup;

public class GenericPlayerSetup implements PlayerSetup {
    @Override
    public void setupPlayer(MutableGame game){
        // setup for two players for now
        game.playerSuccessfulAttacks.put(Player.RED, 0);
        game.playerSuccessfulAttacks.put(Player.BLUE, 0);
    }
}
