package euchre;

import java.util.List;

import core.Card;
import core.Player;

public class EuchrePlayer extends Player {
    public EuchreTeam team;
    
    public List<Card> hand;
    
    public boolean bidded;
    public boolean played;
    
    public EuchrePlayer(int index) {
        super(index);
    }
    
    public void newGame() {
        bidded = false;
        played = false;
    }
    
    public void newRound() {
        bidded = false;
        played = false;
    }
    
    public void secondBid() {
        bidded = false;
    }
    
    public void newTrick() {
        played = false;
    }
}
