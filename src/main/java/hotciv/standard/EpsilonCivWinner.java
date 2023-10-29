package hotciv.standard;
import hotciv.framework.*;

import java.util.Map;

public class EpsilonCivWinner implements Winner {
    @Override
    public Player gameWinner(GameImpl game){
        // TODO: need to find all the players and their win count. Find the first player with 3 successful attacks
        for(Map.Entry<Player, Integer> entry: game.playerSuccessfulAttacks.entrySet())
        {
            Player player = entry.getKey();
            Integer playerWins = entry.getValue();
            if(playerWins >= 3){
                return player;
            }
        }
        return null; // there is no winner yet
    }
}