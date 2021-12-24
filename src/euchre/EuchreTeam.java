package euchre;

import core.Team;

public class EuchreTeam extends Team<EuchrePlayer> {
    public int score;
    
    public EuchreTeam(int index) {
        super(index);
    }
    
    public void newGame() {
        score = 0;
    }
}
