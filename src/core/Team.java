package core;

import java.util.ArrayList;
import java.util.List;

public class Team<PlayerT extends Player> {
    public int index;
    public List<PlayerT> members;
    
    public Team(int index) {
        this.index = index;
        members = new ArrayList<>();
    }
}
