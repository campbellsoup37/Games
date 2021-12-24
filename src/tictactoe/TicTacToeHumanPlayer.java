package tictactoe;

import java.util.Scanner;

import tictactoe.TicTacToeGameState.TicTacToeStateTransition;

public class TicTacToeHumanPlayer extends TicTacToePlayer {
    public Scanner scanner;
    
    public TicTacToeHumanPlayer(int index) {
        super(index);
        scanner = new Scanner(System.in);
    }
    
    public TicTacToeStateTransition play(TicTacToeGameState state) {
        System.out.println("Make a play (type <row>,<column>).");
        int[] play = {-1, -1};
        
        while (play.length != 2 || play[0] < 0 || play[1] < 0 || play[0] > state.board.length || play[1] > state.board.length) {
            try {
                String line = scanner.nextLine();
                String[] vals = line.split(",");
                play[0] = Integer.parseInt(vals[0]);
                play[1] = Integer.parseInt(vals[1]);
            } catch (Exception e) {}
        }
        
        return state.new TicTacToeStateTransition(state, play);
    }
}
