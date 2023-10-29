package hotciv.standard;
import hotciv.framework.*;

public class EpsilonCivPlayerSetup implements PlayerSetup{
    @Override
    public void setupPlayer(GameImpl game){
        // setup for just two players for now
        game.playerSuccessfulAttacks.put(Player.RED, 0);
        game.playerSuccessfulAttacks.put(Player.BLUE, 0);
    }
}
