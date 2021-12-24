package core;

public class Card {
    public static String[] suitNames = {"C", "D", "S", "H"};
    public static String[] valNames = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    
    public int suit;
    public int val;
    public int code;
    
    public Card(int suit, int val) {
        this.suit = suit;
        this.val = val;
        this.code = suit * 13 + val;
    }
    
    public int beats(Card card, int ledSuit, int trumpSuit) {
        if (code == card.code) {
            return 0;
        }
        
        if (suit == trumpSuit) {
            return card.suit != trumpSuit || val > card.val ? 1 : -1;
        }
        
        if (suit == ledSuit) {
            return card.suit != trumpSuit && (card.suit != ledSuit || val > card.val) ? 1 : -1;
        }
        
        return -1;
    }
    
    @Override
    public String toString() {
        return valNames[val] + suitNames[suit];
    }
}
