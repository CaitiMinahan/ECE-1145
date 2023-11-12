package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.PlayerSetup;

public class EpsilonCivPlayerSetup implements PlayerSetup {
    @Override
    public void setupPlayer(MutableGame game){
        // setup for just two players for now
        game.playerSuccessfulAttacks.put(Player.RED, 0);
        game.playerSuccessfulAttacks.put(Player.BLUE, 0);
    }
}
