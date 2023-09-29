package hotciv.standard;
import hotciv.framework.*;
public class BetaCivWorldAging implements WorldAging {
    @Override
    public void gameAging(GameImpl game){
        if(game.getTurnCount() % 2 == 0){
            if((game.getAge() >= -4000) && (game.getAge() < -100)){
                game.setAge(game.getAge() + 100);
            }
            else if(game.getAge() == -100){
                // Once game age reaches 100 BC, start sequence from -100 -> -1 -> 1 -> 50
                // Then continue aging based on the requirements
                game.setAge(-1);
            }
            else if(game.getAge() == -1){
                game.setAge(1);
            }
            else if(game.getAge() == 1){
                game.setAge(50);
            }
            else if((game.getAge() >= 50) && (game.getAge() < 1750)){
                game.setAge(game.getAge() + 50);
            }
            else if((game.getAge() >= 1750) && (game.getAge() < 1900)){
                game.setAge(game.getAge() + 25);
            }
            else if((game.getAge() >= 1900) && (game.getAge() < 1970)){
                game.setAge(game.getAge() + 5);
            }
            else {
                game.setAge(game.getAge() + 1);
            }
        }
    }
}
