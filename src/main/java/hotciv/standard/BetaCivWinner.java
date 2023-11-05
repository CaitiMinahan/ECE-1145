package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.Winner;

public class BetaCivWinner implements Winner {
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
