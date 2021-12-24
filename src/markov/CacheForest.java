package markov;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import ml.Vector;

public class CacheForest {
    public CacheNode cursor = null;
    public List<CacheNode> trees = new LinkedList<>();
    
    public void add(MarkovLearner learner, MarkovElement t, Vector vec) {
        if (cursor == null) {
            cursor = new CacheNode(learner, t, vec, null);
        } else {
            cursor = cursor.add(learner, t.freeze(), vec);
        }
    }
    
    public void backtrack() {
        cursor = cursor.parent;
    }
    
    public void finishTree() {
        while (cursor.parent != null) {
            backtrack();
        }
        trees.add(cursor);
        cursor = null;
    }
    
    public void collapse(MarkovElement t) {
        List<CacheNode> newTrees = new LinkedList<>();
        for (ListIterator<CacheNode> li = trees.listIterator(); li.hasNext();) {
            CacheNode tree = li.next();
            if (tree.t.concursWith(t)) {
                boolean match = tree.t.equals(t);
                if (tree.learner != null) {
                    Vector truth = tree.t.truth(t);
                    if (truth != null) {
                        LinkedList<Vector> datum = new LinkedList<>(Arrays.asList(tree.vec, truth));
                        tree.learner.pushDatum(datum);
                    }
                }
                if (match && tree.children != null) {
                    newTrees.addAll(tree.children);
                }
                li.remove();
            }
        }
        trees.addAll(newTrees);
    }
    
    public class CacheNode {
        public MarkovLearner learner;
        public MarkovElement t;
        public Vector vec;
        public CacheNode parent;
        public List<CacheNode> children;
        
        public CacheNode(MarkovLearner learner, MarkovElement t, Vector vec, CacheNode parent) {
            this.learner = learner;
            this.t = t;
            this.vec = vec;
            this.parent = parent;
        }
        
        public CacheNode add(MarkovLearner learner, MarkovElement t, Vector vec) {
            if (children == null) {
                children = new LinkedList<>();
            }
            CacheNode newNode = new CacheNode(learner, t, vec, this);
            children.add(newNode);
            return newNode;
        }
    }
}
