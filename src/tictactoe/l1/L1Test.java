package tictactoe.l1;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import markov.CacheForest;
import markov.MarkovLearner;
import tictactoe.TicTacToeGame;
import tictactoe.TicTacToeHumanPlayer;
import tictactoe.TicTacToePlayer;
import util.Dashboard;

public class L1Test extends TicTacToeGame {
    // configs
    public int reps = 1000000;
    public double eta = 0.1;
    public int groupings = 100;
    public int progress = 1000;
    
    public boolean lastGameHuman = true;
    public int humanIndex = 0;
    
    public boolean showDash = true;
    public int updateLog = 1000;
    
    public boolean saveToFile = true;
    public boolean loadFromFile = false;
    public String baseDir = "E:/data/tic_tac_toe/L1";
    
    // rest
    public List<MarkovLearner> learners;
    public Dashboard dash;
    
    public File nnn;
    public File tnn;
    
    public L1Test() {
        super();
        
        verbose = true;
        
        numPlayers = 2;
        boardSize = 3;
        
        baseDir += "/N_" + numPlayers + "/b_" + boardSize + "/w_" + winLength;

        L1NodeNN nodeNN = new L1NodeNN(boardSize, numPlayers, new int[] {100});
        L1TransitionNN transitionNN = new L1TransitionNN(boardSize, numPlayers, new int[] {100});
        learners = Arrays.asList(nodeNN, transitionNN);
        nodeNN.setFilePath(baseDir + "/n.nn");
        transitionNN.setFilePath(baseDir + "/t.nn");
        
        if (loadFromFile) {
            nodeNN.load();
            transitionNN.load();
        }
        
        if (saveToFile) {
            nnn = new File(baseDir + "/n.nn");
            tnn = new File(baseDir + "/t.nn");
            nnn.getParentFile().mkdirs();
            tnn.getParentFile().mkdirs();
        }
        
        players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            TicTacToePlayer player = new L1Player(
                i, nodeNN, transitionNN, new CacheForest()
            );
            players.add(player);
        }
        
        if (showDash) {
            dash = new Dashboard();
            dash.execute();
            dash.setGraphCount(2);
            dash.setGraphLabel(0, "L1TransitionNN CEE");
            dash.setGraphLabel(1, "L1NodeNN MSE");
            dash.setGraphColor(0, 0, Color.RED);
            dash.setGraphColor(1, 0, Color.GREEN);
        }
    }
    
    @Override
    public void log(String text) {
        if (dash != null) {
            dash.addLog(text);
        }
        super.log(text);
    }
    
    public void run() {
        int[] wins = new int[numPlayers + 1];
        
        for (int i = 0; i <= reps; i++) {
            if (i % progress == 0) {
                System.out.println(i + "/" + reps);
            }
            
            for (TicTacToePlayer player : players) {
                if (player instanceof L1Player) {
                    ((L1Player)player).r0 = i < reps ? 0.1 : 0.0;
                }
            }
            
            verbose = (dash != null && i % updateLog == 0) || (i == reps);
            
            if (i == reps) {
                for (TicTacToePlayer player : players) {
                    if (player instanceof L1Player) {
                        ((L1Player)player).verbose = true;
                    }
                }
                
                if (lastGameHuman) {
                    TicTacToePlayer human = new TicTacToeHumanPlayer(humanIndex);
                    players.set(humanIndex, human);
                    i--;
                }
            }
            
            newGame();
            wins[state.winner + 1]++;
            
            if (dash != null && i % updateLog == 0) {
                dash.updateLog();
            }
            
            if ((i + 1) % groupings == 0) {
                int j = 0;
                for (MarkovLearner learner : learners) {
                    List<double[]> error = learner.doEpoch(eta, eta, false);
                    
                    if (dash != null) {
                        dash.addGraphData(j, 0, error.get(0)[0]);
                    }
                    
                    if (saveToFile) {
                        learner.save();
                    }
                    
                    j++;
                }
            }
        }
        for (int i = -1; i < numPlayers; i++) {
            System.out.println("Wins by player " + i + ": " + wins[i + 1]);
        }
    }
    
    public static void main(String[] args) {
        L1Test game = new L1Test();
        game.run();
    }
}
