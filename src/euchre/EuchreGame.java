package euchre;

import java.util.List;
import java.util.Random;

import core.Card;
import core.Util;

public class EuchreGame {
    // configs
    public boolean verbose = false;
    
    // game constants
    public Random random;
    public EuchreDeck deck;
    public List<EuchrePlayer> players;
    public EuchreTeam[] teams;
    
    // game variables
    public int roundNumber;
    public Card upCard;
    public int dealer;
    public int leader;
    
    public EuchreGame() {
        random = new Random();
        deck = new EuchreDeck();
    }
    
    public void setPlayers(List<EuchrePlayer> players) {
        this.players = players;
        
        teams = new EuchreTeam[] {new EuchreTeam(0), new EuchreTeam(1)};
        for (int i = 0; i < players.size(); i++) {
            EuchrePlayer player = players.get(i);
            EuchreTeam team = teams[i & 1];
            
            player.index = i;
            player.team = team;
            team.members.add(player);
        }
    }
    
    public void newGame() {
        if (verbose) {
            System.out.println("Starting game");
        }
        
        for (EuchrePlayer player : players) {
            player.newGame();
        }
        for (EuchreTeam team : teams) {
            team.newGame();
        }
        
        roundNumber = -1;
        upCard = null;
        dealer = -1;
        leader = -1;
        
        newRound();
    }
    
    public void newRound() {
        roundNumber++;
        if (verbose) {
            System.out.println("Starting round " + roundNumber);
        }
        for (EuchrePlayer player : players) {
            player.newRound();
        }
        
        deck.initialize();
        List<List<Card>> hands = deck.deal(players.size(), 5, random);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).hand = hands.get(i);
        }
        upCard = deck.drawRandom(random);
        if (verbose) {
            System.out.println("Dealt:");
            for (EuchrePlayer player : players) {
                System.out.println(
                    " player " + player.index + ": " 
                    + Util.handToString(player.hand, false)
                );
            }
            System.out.println("Up card: " + upCard);
        }
        
        dealer = nextIndex(dealer);
        leader = nextIndex(dealer);
        
        bid();
    }
    
    public void bid() {
        
    }
    
    public int nextIndex(int i) {
        return (i + 1) % players.size();
    }
}
