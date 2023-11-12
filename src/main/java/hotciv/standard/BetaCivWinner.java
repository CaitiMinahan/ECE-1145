package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.Winner;

public class BetaCivWinner implements Winner {
    @Override
    public Player gameWinner(MutableGame game) {
        // Check if there are no cities
        if (game.cities.isEmpty()) {
            return null;
        }
        Player owner = null;
        for (City city : game.cities.values()) {
            if (owner == null) {
                // Assign the owner of the first city
                owner = city.getOwner();
            } else if (city.getOwner() != owner) {
                // If any city has a different owner, there is no winner yet
                return null;
            }
        }
        // If all cities have the same owner, return that owner as the winner
        return owner;
    }
}
