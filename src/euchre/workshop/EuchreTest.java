package euchre.workshop;

import java.util.ArrayList;
import java.util.List;

import euchre.EuchreGame;
import euchre.EuchrePlayer;

public class EuchreTest extends EuchreGame {
    public EuchreTest() {
        super();
        
        verbose = true;
        
        List<EuchrePlayer> players = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            EuchrePlayer player = new EuchrePlayer(i);
            players.add(player);
        }
        setPlayers(players);
    }
    
    public static void main(String[] args) {
        EuchreTest g = new EuchreTest();
        g.newGame();
    }
}
