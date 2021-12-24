package core;

import java.util.ArrayList;

public class StandardDeck extends Deck<Card> {
    public void initialize() {
        cards = new ArrayList<>();
        
        for (int d = 0; d < D; d++) {
            for (int s = 0; s < 4; s++) {
                for (int v = 0; v < 13; v++) {
                    cards.add(new Card(s, v));
                }
            }
        }
    }
}
