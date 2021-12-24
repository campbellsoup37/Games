package markov;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.Vector;

public abstract class Node implements MarkovElement {
    public CacheForest cache;

    @Override
    public Vector truth(MarkovElement e) {
        throw new MarkovException("truth not implemented.");
    }

    @Override
    public Vector vector() {
        throw new MarkovException("vector not implemented.");
    }
    
    public List<? extends Transition> getTransitions() {
        return new ArrayList<>();
    }
    
    public double getE() {
        double p = 0;
        List<? extends Transition> transitions = getTransitions();
        for (Transition t : transitions) {
            double q = t.getP();
            qOverride(t, q);
            t.applyChanges();
            double r = t.source().getE();
            rOverride(t, r);
            p += q * r;
            t.undoChanges();
            if (cache != null) {
                cache.backtrack();
            }
        }
        return p;
    }

    public void qOverride(Transition t, double q) {}
    
    public void rOverride(Transition t, double r) {}
    
    public List<? extends Transition> getChoices() {
        return new ArrayList<>();
    }
    
    public Transition choose(double r0, Random random) {
        double bestP = Integer.MIN_VALUE;
        Transition best = null;
        List<? extends Transition> choices = getChoices();
        for (Transition t : choices) {
            if (cache != null) {
                cache.add(null, t, null);
            }
            t.applyChanges();
            double p = t.source().getE();
            pOverride(t, p);
            if (p > bestP) {
                bestP = p;
                best = t;
            }
            t.undoChanges();
            if (cache != null) {
                cache.finishTree();
            }
        }
        
        if (random != null) {
            double r = random.nextDouble();
            if (r < r0) {
                return choices.get(random.nextInt(choices.size()));
            }
        }
        
        return best;
    }
    
    public Transition choose() {
        return choose(0, null);
    }
    
    public void pOverride(Transition t, double p) {}
}
