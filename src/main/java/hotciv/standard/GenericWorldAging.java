package hotciv.standard;

public class GenericWorldAging implements WorldAging{
    @Override
    public void gameAging(GameImpl game){
        if(game.getTurnCount() % 2 == 0){
            game.setAge(game.getAge()+100);
        }
    }
}
