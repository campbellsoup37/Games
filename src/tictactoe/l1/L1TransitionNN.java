package tictactoe.l1;

import java.util.LinkedList;
import java.util.List;

import markov.MarkovLearner;
import ml.ActivationFunction;
import ml.CrossEntropy;
import ml.Feature;
import ml.ReLuFunction;
import ml.SoftmaxFunction;

public class L1TransitionNN extends MarkovLearner {
    public L1TransitionNN(int boardSize, int numPlayers, int[] hiddenDims) {
        List<Feature> features = new LinkedList<>();
        
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                features.add(new Feature(i + "," + j, -1, numPlayers - 1));
            }
        }
        
        setFeatures(features);
        
        int[] ds = new int[2 + hiddenDims.length];
        ds[0] = features.stream().map(Feature::getDimension).reduce(0, Integer::sum);
        for (int i = 1; i < ds.length - 1; i++) {
            ds[i] = hiddenDims[i - 1];
        }
        ds[ds.length - 1] = boardSize * boardSize;
        
        ActivationFunction[] actFuncs = new ActivationFunction[ds.length - 1];
        for (int i = 0; i < actFuncs.length - 1; i++) {
            actFuncs[i] = new ReLuFunction();
        }
        actFuncs[actFuncs.length - 1] = new SoftmaxFunction();
        
        buildLayers(ds, actFuncs);
        
        lossFunc = new CrossEntropy();
    }
}
