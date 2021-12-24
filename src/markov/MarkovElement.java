package markov;

import ml.Vector;

public interface MarkovElement {
    public boolean concursWith(MarkovElement e);
    
    public Vector truth(MarkovElement e);
    
    public Vector vector();
    
    public MarkovElement freeze();
}
