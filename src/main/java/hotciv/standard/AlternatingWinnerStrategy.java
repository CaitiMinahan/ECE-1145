package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.Winner;

public class AlternatingWinnerStrategy implements Winner {
    private Winner currentWinnerState;
    public AlternatingWinnerStrategy(){
        this.currentWinnerState = new BetaCivWinner();
    }

    @Override
    public Player gameWinner(MutableGame game) {

        if (game.getTurnCount() <= 20) {
            return currentWinnerState.gameWinner(game);
        } else {
            currentWinnerState = new EpsilonCivWinner();
            return currentWinnerState.gameWinner(game);
        }
    }
}
