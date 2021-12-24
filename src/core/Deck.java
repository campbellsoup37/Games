package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Deck<CardT extends Card> {
    public List<CardT> cards;
    public int D;
    
    public Deck(int D) {
        this.D = D;
    }
    
    public Deck() {
        this(1);
    }
    
    public void initialize() {
        throw new CoreException("initialize must be overridden.");
    }
    
    public CardT drawRandom(Random random) {
        return cards.remove(random.nextInt(cards.size()));
    }
    
    public List<List<CardT>> deal(int numHands, int handSize, Random random) {
        List<List<CardT>> hands = new ArrayList<>(numHands);
        for (int i = 0; i < numHands; i++) {
            List<CardT> hand = new ArrayList<>(handSize);
            for (int j = 0; j < handSize; j++) {
                hand.add(drawRandom(random));
            }
            hands.add(hand);
        }
        return hands;
    }
}
