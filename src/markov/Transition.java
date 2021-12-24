package markov;

import ml.Vector;

public abstract class Transition implements MarkovElement {
    public Node source() {
        throw new MarkovException("source not implemented.");
    }

    @Override
    public Vector truth(MarkovElement e) {
        throw new MarkovException("truth not implemented.");
    }

    @Override
    public Vector vector() {
        throw new MarkovException("vector not implemented.");
    }
    
    public double getP() {
        throw new MarkovException("getP not implemented.");
    }
    
    public void applyChanges() {
        throw new MarkovException("applyChanges not implemented.");
    }
    
    public void undoChanges() {
        throw new MarkovException("undoChanges not implemented.");
    }
    
    public Transition translate(Node node) {
        throw new MarkovException("translate not implemented.");
    }
}
