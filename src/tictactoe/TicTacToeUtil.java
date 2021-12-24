package tictactoe;

import java.util.ArrayList;
import java.util.List;

import ml.SparseVector;

public class TicTacToeUtil {
    public static List<int[]> getEmptySpaces(Integer[][] board) {
        int boardSize = board.length;
        List<int[]> ans = new ArrayList<>(boardSize * boardSize);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == null) {
                    ans.add(new int[] {i, j});
                }
            }
        }
        return ans;
    }
    
    public static int winner(Integer[][] board, int[] play, int winLength) {
        Integer index = board[play[0]][play[1]];
        
        int up, down, left, right, upleft, downright, upright, downleft;
        
        for (up = 0; play[0] - up >= 0 && board[play[0] - up][play[1]] == index; up++);
        for (down = 0; play[0] + down < board.length && board[play[0] + down][play[1]] == index; down++);
        if (up + down - 1 >= winLength) {
            return index;
        }
        
        for (left = 0; play[1] - left >= 0 && board[play[0]][play[1] - left] == index; left++);
        for (right = 0; play[1] + right < board.length && board[play[0]][play[1] + right] == index; right++);
        if (left + right - 1 >= winLength) {
            return index;
        }
        
        for (upleft = 0; play[0] - upleft >= 0 && play[1] - upleft >= 0 && board[play[0] - upleft][play[1] - upleft] == index; upleft++);
        for (downright = 0; play[0] + downright < board.length && play[1] + downright < board.length && board[play[0] + downright][play[1] + downright] == index; downright++);
        if (upleft + downright - 1 >= winLength) {
            return index;
        }
        
        for (upright = 0; play[0] - upright >= 0 && play[1] + upright < board.length && board[play[0] - upright][play[1] + upright] == index; upright++);
        for (downleft = 0; play[0] + downleft < board.length && play[1] - downleft >= 0 && board[play[0] + downleft][play[1] - downleft] == index; downleft++);
        if (upright + downleft - 1 >= winLength) {
            return index;
        }
        
        return -1;
    }
    
    public static void boardToVector(Integer[][] board, int shift, int numPlayers, SparseVector vec) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int val = -1;
                if (board[i][j] != null) {
                    val = (board[i][j] + numPlayers - shift) % numPlayers;
                }
                vec.addOneHot(i + "," + j, val, -1, numPlayers - 1);
            }
        }
    }
}
