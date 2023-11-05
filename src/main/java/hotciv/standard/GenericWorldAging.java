package hotciv.standard;

import hotciv.standard.Interfaces.WorldAging;

public class GenericWorldAging implements WorldAging {
    @Override
    public void gameAging(GameImpl game){
        if(game.getTurnCount() % 2 == 0){
            game.setAge(game.getAge()+100);
        }
    }
}
