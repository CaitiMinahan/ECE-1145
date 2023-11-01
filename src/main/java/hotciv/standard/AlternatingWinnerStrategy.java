package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Interfaces.Winner;

public class AlternatingWinnerStrategy implements Winner {
    private Winner currentWinnerState;
    private int round;
    public AlternatingWinnerStrategy(){
        this.currentWinnerState = new BetaCivWinner();
        this.round = 0;
    }

    @Override
    public Player gameWinner(GameImpl game) {
        round++;

        if (round <= 20) {
            //currentWinnerState = new BetaCivWinner();
            return currentWinnerState.gameWinner(game);
        } else {
            // currentWinnerState = new EpsilonCivWinner();  // TODO: get EpsilonCivWinner when it is ready
            currentWinnerState = new GenericWinner(); // TODO: remove this. placeholder so I can commit my changes
            return currentWinnerState.gameWinner(game);
        }
    }
}
