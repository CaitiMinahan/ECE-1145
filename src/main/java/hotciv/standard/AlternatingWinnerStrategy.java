package hotciv.standard;
import hotciv.framework.*;

public class AlternatingWinnerStrategy implements Winner{
    private Winner currentWinnerState;
    private int round;
    public AlternatingWinnerStrategy(){
        this.currentWinnerState = null;
        this.round = 0;
    }

    @Override
    public Player gameWinner(GameImpl game) {
        round++;

        // add conditions to handle when the initial state is set to null
        if (currentWinnerState != null) {
            Player winner = currentWinnerState.gameWinner(game);

            if (winner != null) {
                // A player has won, return the winner
                return winner;
            }

            if (round <= 20) {
                currentWinnerState = new BetaCivWinner();
                return currentWinnerState.gameWinner(game);
            } else {
                currentWinnerState = new EpsilonCivWinner();  // TODO: get EpsilonCivWinner when it is ready
                return currentWinnerState.gameWinner(game);
            }
        }
        return null;
    }
}
