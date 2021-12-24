package tictactoe;

import java.util.ArrayList;
import java.util.List;

import markov.MarkovElement;
import markov.Node;
import markov.Transition;

public class TicTacToeGameState extends Node {
    public Integer[][] board;
    public int turn;
    public int numPlayers;
    public int myIndex;
    public int roundNumber;
    public int winLength;
    public int winner;
    
    public TicTacToeGameState(Integer[][] board, int turn, int numPlayers, int myIndex, int roundNumber, int winLength) {
        this.board = board;
        this.turn = turn;
        this.numPlayers = numPlayers;
        this.myIndex = myIndex;
        this.roundNumber = roundNumber;
        this.winLength = winLength;
        this.winner = -1;
    }
    
    public TicTacToeStateTransition makeTransition(int[] play) {
        return new TicTacToeStateTransition(this, play);
    }
    
    @Override
    public List<? extends Transition> getTransitions() {
        List<int[]> empties = TicTacToeUtil.getEmptySpaces(board);
        List<TicTacToeStateTransition> ans = new ArrayList<>(empties.size());
        for (int[] empty : empties) {
            ans.add(makeTransition(empty));
        }
        return ans;
    }
    
    @Override
    public List<? extends Transition> getChoices() {
        return getTransitions();
    }

    @Override
    public boolean concursWith(MarkovElement e) {
        return e instanceof TicTacToeGameState;
    }

    @Override
    public MarkovElement freeze() {
        return new TicTacToeGameState(null, turn, numPlayers, myIndex, roundNumber, winLength);
    }
    
    public class TicTacToeStateTransition extends Transition {
        public TicTacToeGameState source;
        public int roundNumber;
        public int[] play;
        
        public TicTacToeStateTransition(TicTacToeGameState source, int[] play) {
            this.source = source;
            this.roundNumber = TicTacToeGameState.this.roundNumber;
            this.play = play;
        }
        
        @Override
        public void applyChanges() {
            if (board[play[0]][play[1]] != null) {
                throw new TicTacToeException("Player tried to play in an occupied space.");
            }
            
            board[play[0]][play[1]] = turn;
            turn = (turn + 1) % numPlayers;
            TicTacToeGameState.this.roundNumber++;
            winner = TicTacToeUtil.winner(board, play, winLength);
        }
        
        @Override
        public void undoChanges() {
            board[play[0]][play[1]] = null;
            turn = (turn + numPlayers - 1) % numPlayers;
            TicTacToeGameState.this.roundNumber--;
            winner = -1;
        }

        @Override
        public Node source() {
            return source;
        }

        @Override
        public boolean concursWith(MarkovElement e) {
            if (!(e instanceof TicTacToeStateTransition)) {
                return false;
            }
            TicTacToeStateTransition t = (TicTacToeStateTransition)e;
            return roundNumber == t.roundNumber;
        }
        
        @Override
        public boolean equals(Object e) {
            if (!(e instanceof TicTacToeStateTransition)) {
                return false;
            }
            TicTacToeStateTransition t = (TicTacToeStateTransition)e;
            return roundNumber == t.roundNumber && play[0] == t.play[0] && play[1] == t.play[1];
        }

        @Override
        public Transition translate(Node node) {
            TicTacToeGameState state = (TicTacToeGameState)node;
            return state.new TicTacToeStateTransition(state, play);
        }

        @Override
        public MarkovElement freeze() {
            return new TicTacToeStateTransition(source, new int[] {play[0], play[1]});
        }
    }
}
