package tictactoe;

import java.util.Random;

import core.Player;
import markov.MarkovElement;
import tictactoe.TicTacToeGameState.TicTacToeStateTransition;

public abstract class TicTacToePlayer extends Player {
    public Random random = new Random();
    
    public TicTacToePlayer(int index) {
        super(index);
    }
    
    public TicTacToeStateTransition play(TicTacToeGameState state) {
        int i = 0;
        int j = 0;
        do {
            i = random.nextInt(state.board.length);
            j = random.nextInt(state.board.length);
        } while (state.board[i][j] != null);
        return state.new TicTacToeStateTransition(state, new int[] {i, j});
    }
    
    public void notifyMarkovElement(MarkovElement e) {}
}
