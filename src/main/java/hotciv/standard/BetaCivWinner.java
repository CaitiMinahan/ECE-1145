package hotciv.standard;
import hotciv.framework.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BetaCivWinner implements Winner{
    @Override
    public Player gameWinner(GameImpl game){
        Player owner = null;

        for (City city: game.cities.values()){
            if(owner == null){
                owner = city.getOwner();
            }
            else {
                if(city.getOwner() != owner){
                    return null;
                }
            }
        }
        return owner;
    }
}
