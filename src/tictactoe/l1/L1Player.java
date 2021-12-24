package tictactoe.l1;

import java.util.Random;

import markov.CacheForest;
import markov.MarkovElement;
import markov.Transition;
import ml.BasicVector;
import ml.SparseVector;
import ml.Vector;
import tictactoe.TicTacToeGameState;
import tictactoe.TicTacToePlayer;
import tictactoe.TicTacToeUtil;
import tictactoe.TicTacToeGameState.TicTacToeStateTransition;

public class L1Player extends TicTacToePlayer {
    public L1NodeNN nodeNN;
    public L1TransitionNN transitionNN;
    public CacheForest cache;
    
    public double r0 = 0;
    public Random random;
    
    public boolean verbose = false;
    
    public L1Player(int index, L1NodeNN nodeNN, L1TransitionNN transitionNN, CacheForest cache) {
        super(index);
        this.nodeNN = nodeNN;
        this.transitionNN = transitionNN;
        this.cache = cache;
        random = new Random();
    }
    
    public TicTacToeStateTransition play(TicTacToeGameState state) {
        L1State myState = new L1State(state);
        Transition t = myState.choose(r0, random);
        return (TicTacToeStateTransition)t;
    }
    
    public void notifyMarkovElement(MarkovElement e) {
        cache.collapse(e);
    }

    public class L1State extends TicTacToeGameState {
        public Vector vec;
        public Vector transitionP;
        
        public L1State(TicTacToeGameState state) {
            super(state.board, state.turn, state.numPlayers, state.myIndex, state.roundNumber, state.winLength);
            this.cache = L1Player.this.cache;
        }
        
        @Override
        public TicTacToeStateTransition makeTransition(int[] play) {
            int code = board.length * play[0] + play[1];
            double p = transitionP == null ? 0 : transitionP.get(code);
            return new L1Transition(this, play, vec, p);
        }
        
        @Override
        public Vector truth(MarkovElement e) {
            if (!(e instanceof TicTacToeGameState)) {
                return null;
            }
            TicTacToeGameState trueState = (TicTacToeGameState)e;
            if (trueState.winner == myIndex) {
                return new BasicVector(new double[] {1});
            } else if (trueState.winner != -1) {
                return new BasicVector(new double[] {-1});
            } else {
                return new BasicVector(new double[] {-0.1});
            }
        }
        
        @Override
        public Vector vector() {
            SparseVector vec = new SparseVector();
            TicTacToeUtil.boardToVector(board, myIndex, numPlayers, vec);
            return vec;
        }
        
        @Override
        public double getE() {
            if (winner == myIndex) {
                return 1;
            } else if (winner != -1) {
                return -1;
            } else if (roundNumber == board.length * board.length) {
                return -0.1;
            }

            vec = vector();
            
            if (turn == myIndex) {
                if (cache != null) {
                    cache.add(nodeNN, this, vec);
                    cache.backtrack();
                }
                return nodeNN.evaluate(vec).get(0);
            } else {
                transitionP = transitionNN.evaluate(vec);
                return super.getE();
            }
        }

        @Override
        public MarkovElement freeze() {
            return new L1State(this);
        }
        
        @Override
        public void qOverride(Transition t, double q) {
            if (verbose) {
                System.out.println("  " + t + ": " + q);
            }
        }
        
        @Override
        public void rOverride(Transition t, double r) {
            if (verbose) {
                System.out.println("    value: " + r);
            }
        }
        
        @Override
        public void pOverride(Transition t, double p) {
            if (verbose) {
                System.out.println(t + ": " + p);
            }
        }
        
        public class L1Transition extends TicTacToeStateTransition {
            public Vector vec;
            public double p;
            
            public L1Transition(TicTacToeGameState source, int[] play, Vector vec, double p) {
                super(source, play);
                this.vec = vec;
                this.p = p;
            }
            
            @Override
            public Vector truth(MarkovElement e) {
                if (!(e instanceof TicTacToeStateTransition)) {
                    return null;
                }
                if (equals(e)) {
                    TicTacToeStateTransition trueTransition = (TicTacToeStateTransition)e;
                    int code = board.length * trueTransition.play[0] + trueTransition.play[1];
                    SparseVector tru = new SparseVector();
                    tru.addOneHot("Play", code, -1, board.length * board.length - 1);
                    return tru;
                } else {
                    return null;
                }
            }
            
            @Override
            public Vector vector() {
                return vec;
            }
            
            @Override
            public double getP() {
                if (cache != null) {
                    cache.add(transitionNN, this, vec);
                }
                return p;
            }
            
            @Override
            public String toString() {
                return roundNumber + ": player " + turn + " plays " + play[0] + ", " + play[1];
            }

            @Override
            public MarkovElement freeze() {
                return new L1Transition(source, play, vec, p);
            }
        }
    }
}
