package markov;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ml.Learner;
import ml.LossFunction;
import ml.MeanSquaredError;
import ml.Vector;

public class MarkovLearner extends Learner {
    public LinkedList<LinkedList<Vector>> data = new LinkedList<>();
    public double outputInf = 0;
    public double outputSup = 1;
    public double range = 1;
    public LossFunction lossFunc = new MeanSquaredError();
    
    public String filePath;
    public File file;
    
    private boolean dirExists;
    
    public void pushDatum(LinkedList<Vector> datum) {
        if (outputInf == 0 && outputSup == 1) {
            data.push(datum);
        } else {
            LinkedList<Vector> copy = new LinkedList<>();
            copy.add(datum.getFirst());
            Vector newOut = datum.getLast().copy();
            newOut.add(-outputInf);
            newOut.scale(1 / range);
            copy.add(newOut);
            data.push(copy);
        }
    }
    
    @Override
    public LinkedList<Vector> getDatum(Vector in) {
        if (in == null) {
            return data.pop();
        } else {
            return new LinkedList<>(Arrays.asList(in, null));
        }
    }
    
    public List<double[]> doEpoch(double wEta, double bEta, boolean computeSizes) {
        if (data.isEmpty()) {
            return null;
        } else {
            List<double[]> ans = super.doEpoch(wEta, bEta, data.size(), lossFunc, computeSizes, computeSizes);
            data = new LinkedList<>();
            return ans;
        }
    }
    
    public void setRange(double inf, double sup) {
        outputInf = inf;
        outputSup = sup;
        range = sup - inf;
    }
    
    public Vector evaluate(Vector in) {
        Vector out = testValue(in).get(1);
        if (!(outputInf == 0 && outputSup == 1)) {
            out.scale(range);
            out.add(outputInf);
        }
        return out;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        file = new File(filePath);
        dirExists = file.getParentFile().exists();
    }
    
    public void load() {
        if (!dirExists) {
            throw new MarkovException("MarkovLearner file " + filePath + " does not exist.");
        }
        openFromFile(filePath);
    }
    
    public void save() {
        if (!dirExists) {
            file.getParentFile().mkdirs();
            dirExists = file.getParentFile().exists();
        }
        saveToFile(file);
    }
}
