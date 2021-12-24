package tictactoe;

import java.util.List;

import markov.MarkovElement;
import tictactoe.TicTacToeGameState.TicTacToeStateTransition;

public class TicTacToeGame {
    // configs
    public boolean verbose = false;
    public int boardSize = 3;
    public int winLength = 3;
    public int numPlayers = 2;
    
    // game constants
    public List<TicTacToePlayer> players;
    
    // game variables
    public TicTacToeGameState state;
    
    public TicTacToeGame() {}
    
    public void log(String text) {
        System.out.print(text);
    }
    
    public void newGame() {
        state = new TicTacToeGameState(
            new Integer[boardSize][boardSize],
            0, numPlayers, -1, 0, winLength
        );
        
        play();
    }
    
    // Generate a copy state with the player's index for their use.
    public TicTacToeGameState generateStateForPlayer() {
        Integer[][] board = new Integer[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = state.board[i][j];
            }
        }
        return new TicTacToeGameState(board, state.turn, numPlayers, state.turn, state.roundNumber, winLength);
    }
    
    public void announce(MarkovElement e) {
        for (TicTacToePlayer player : players) {
            player.notifyMarkovElement(e);
        }
    }
    
    public void play() {
        TicTacToeStateTransition t = players.get(state.turn).play(generateStateForPlayer());
        announce(t);
        
        if (verbose) {
            log("Player " + state.turn + " played " + t.play[0] + ", " + t.play[1] + "\n");
        }
        
        t.translate(state).applyChanges();
        
        if (verbose) {
            printBoard();
            log("\n");
        }
        
        if (state.winner != -1) {
            gameOver(state.winner);
        } else if (state.roundNumber == boardSize * boardSize) {
            gameOver(-1);
        } else {
            play();
        }
    }
    
    public void gameOver(int index) {
        announce(state);
        if (verbose) {
            if (index == -1) {
                log("Nobody won.\n");
            } else {
                log("Player " + index + " won.\n");
            }
        }
    }
    
    public void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                String val = state.board[i][j] == null ? "_" : state.board[i][j] + "";
                log(val + " ");
            }
            log("\n");
        }
    }
}
