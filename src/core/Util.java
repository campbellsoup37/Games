package core;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String handToString(List<Card> hand, boolean sorted) {
        List<Card> toPrint = hand;
        if (sorted) {
            toPrint = new ArrayList<Card>(hand);
            toPrint.sort((c1, c2) -> c1.code > c2.code ? 1 : -1);
        }
        StringBuilder sb = new StringBuilder();
        for (Card card : toPrint) {
            sb.append(" " + card);
        }
        return sb.substring(1).toString();
    }
}
