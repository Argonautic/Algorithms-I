/* Model a generalization of the 8 puzzle - tiles of the numbers 1 through 8 and one empty space on a 3-by-3 grid, where you must move tiles around using
 * the empty space until they are in natural order. Board initializes an n-by-n game board of the puzzle (the number of tiles to move around being
 * (n * n) - 1) and sets up methods for the A* search algorithm in Solver to find an optimal solution to the puzzle.*/

import java.util.Arrays;
import java.util.ArrayList;

public class Board {
    private int n;
    private int[][] blocks;
    
    public Board(int[][] blocks) {
    /* Initializes an n-by-n game board for the puzzle. Board contains blocks, which represents 
     * the actual n-by-n array of tiles */
        this.n = blocks.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++)
            copy[i] = Arrays.copyOf(blocks[i], n);
        this.blocks = copy;
    }
    
    public int dimension()
    // For use in Solver calculations
    {   return n;   }    
    
    private int xyTo1D(int x, int y)
    // Helper function for various methods - returns the 1D representation of a tile in blocks
    {   return n * x + y + 1;   }
    
    public int hamming() {
    /* Returns the hamming distance from the board's current state to the solved state. Hamming
     * distance is calculated by returning the number of tiles that are out of place */
        int result = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != xyTo1D(i, j) && blocks[i][j] != 0) result++;
        return result;
    }
        
    public int manhattan() {
    /* Returns the manhattan distance from the board's current state to the solved state. Manhattan
     * distance is calculated by returning the sum of the vertical and horizontal distance of each tile
     * to its solved location. For example, on a 3-by-3 grid, the tile 5 that is located on the upper
     * left corner will have a Manhattan distance of 2, as it is one horizontal and one vertical move
     * away from its solved location in the center */
        int result = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != xyTo1D(i, j)) {
                    int max = Math.max(blocks[i][j], xyTo1D(i, j));
                    int min = Math.min(blocks[i][j], xyTo1D(i, j));

                    while ((max - 1) / n != (min - 1) / n) {
                        max -= n;
                        result++;
                    } 
                    result += Math.abs(max - min);
                }
            }
        return result;
    }
            
    public boolean isGoal() {
    /* Is this board solved? Used for Solver calculations */
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n && !(j == n - 1 && i == n - 1); j++)
                if (blocks[i][j] != xyTo1D(i, j)) return false;
        return true;
    }
    
    public Board twin() {
    /* Returns a twin of Board, with the places of two tiles switched. Used in Solver calculations,
     * as any solvable Board will not be solvable if any two tiles are switched */
        int[][] twinBlock = new int[n][n];
        for (int i = 0; i < n; i++)
            twinBlock[i] = Arrays.copyOf(blocks[i], n);
        
        int[] a = new int[]{0, 0};
        int[] b = new int[]{0, 1};
        if (twinBlock[0][0] == 0) a[0] = 1;
        else if (twinBlock[0][1] == 0) b[0] = 1;

        exch(twinBlock, a[0], a[1], b[0], b[1]);
        
        return new Board(twinBlock);
    }
    
    private void exch(int[][] blockState, int i, int j, int x, int y) {
    // Helper function. Exchanges the position of two tiles
        int temp = blockState[i][j];
        blockState[i][j] = blockState[x][y];
        blockState[x][y] = temp;
    }
    
    public boolean equals(Object y) {
    /* Used for solver calculations */
        if (y == this) return true;
        
        if (y == null) return false;
        
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        return Arrays.deepEquals(blocks, that.blocks);
     
    }
    
    public Iterable<Board> neighbors() {
    /* Returns an Iterable of all possible Boards that are the result of one move from the
     * current board. Used for Solver calculations. */
        ArrayList<Board> result = new ArrayList<Board>(2);            
        int x = 0;
        int y = 0;
    
        outerloop:
        // To find where 0 is on the board
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] == 0) {
                    x = i;
                    y = j;
                    break outerloop;
                }
        
        for (int i = -1; i < 2; i += 2) {
        // To swap two nonzero tiles with each other
            if (x + i >= 0 && x + i < n) {
                int[][] newblocks = new int[n][n];
                
                for (int j = 0; j < n; j++) 
                    newblocks[j] = Arrays.copyOf(blocks[j], n);
                
                exch(newblocks, x, y, x + i, y);
                result.add(new Board(newblocks));
            }
            
            if (y + i >= 0 && y + i < n) {
                int[][] newblocks = new int[n][n];
                
                for (int j = 0; j < n; j++)
                    newblocks[j] = Arrays.copyOf(blocks[j], n); 
                
                exch(newblocks, x, y, x, y + i);
                result.add(new Board(newblocks));
            }
        }         
        return result;        
        
    }
    
    public String toString() {
    // String representation of the current Board, First prints n, then the actual board
        String initial = String.format("%s \n", n);
        StringBuilder result = new StringBuilder(initial);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                result.append(Integer.toString(blocks[i][j]) + " ");
            result.append("\n");
        }
        return result.toString();    
    }
        
    public static void main(String[] args) {      
        
    }

}
