package hotciv.standard;
import hotciv.framework.*;

public class GenericPlayerSetup implements PlayerSetup{
    @Override
    public void setupPlayer(GameImpl game){
        // setup for two players for now
        game.playerSuccessfulAttacks.put(Player.RED, 0);
        game.playerSuccessfulAttacks.put(Player.BLUE, 0);
    }
}
