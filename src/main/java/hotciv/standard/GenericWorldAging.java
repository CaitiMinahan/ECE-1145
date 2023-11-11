package hotciv.standard;

import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.WorldAging;

public class GenericWorldAging implements WorldAging {
    @Override
    public void gameAging(MutableGame game){
        if(game.getTurnCount() % 2 == 0){
            game.setAge(game.getAge()+100);
        }
    }
}
