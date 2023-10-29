package hotciv.standard;
import hotciv.framework.*;

public class AlternatingWinnerStrategy implements Winner{
    private Winner currentWinnerState;
    public AlternatingWinnerStrategy(){
        this.currentWinnerState = new BetaCivWinner();
    }

    @Override
    public Player gameWinner(GameImpl game) {

        if (game.getTurnCount() <= 20) {
            return currentWinnerState.gameWinner(game);
        } else {
            currentWinnerState = new EpsilonCivWinner();
            return currentWinnerState.gameWinner(game);
        }
    }
}
