/* Models the A* search algorithm that finds the optimal solution to any variant of the 8-puzzle. The class SearchNode represents a Board, the # of moves it took 
 * to get to that Board, and the previous Board. Solver initializes two SearchNodes, a given initial and a twin that has two tiles swapped, and runs move operations
 * on both of those SearchNodes simultaneously. The next move for each of the two tracks (initial and twin) will be determined by a respective minimum priority
 * queue (again, initial and twin) that is determine by manhattan priority. For each board state of the two tracks, their neighbors (all possible board states 
 * within one move) will be added to their priority queues, and the board on the queue with the overall minimum manhattan priority will become the board of the 
 * next node. This process will repeat with successive nodes until either the twin or initial node track reaches the goal. From there, a number of methods
 * return useful properties, like whether or not the initial Board was solvable, and if so, how many moves it took.*/

import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;

public class Solver {
    private int movesToSolution;
    private ArrayList<Board> boardPath;
    private boolean solveable;
    
    public Solver(Board initial) {
    /* Initializes with a given Board as input, to determine is Board is solvable and return useful properties
     * if so. If the twin node track reaches goal instead of the initial node track, the initial node is not
     * solvable */
        if (initial == null) throw new NullPointerException();
        
        boardPath = new ArrayList<Board>(); // The board path from initial state to solution
        ArrayList<SearchNode> nodePath = new ArrayList<SearchNode>(); // The node path from initial state to solution
        
        SearchNode initialNode = new SearchNode(initial, 0, null);      
        MinPQ<SearchNode> initialPQ = new MinPQ<SearchNode>();
        initialPQ.insert(initialNode);       
        
        SearchNode twinNode = new SearchNode(initial.twin(), 0, null);
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        twinPQ.insert(twinNode);
        
        while (true) {            
            initialNode = initialPQ.delMin();
            nodePath.add(initialNode);
            
            if (initialNode.board.isGoal()) {
           //Once the goal is found, the goal board and all prev boards are added to boardPath in reverse order
               movesToSolution = initialNode.numberOfMoves;
               solveable = true;
               while (initialNode != null) {
                   boardPath.add(0, initialNode.board);
                   initialNode = initialNode.prev;
               }
               break;
            }
            
            twinNode = twinPQ.delMin();
            if (twinNode.board.isGoal()) {
                movesToSolution = -1;
                solveable = false;
                break;
            }
            
            insertNeighbors(initialNode, initialPQ, initialNode.numberOfMoves + 1);            
            insertNeighbors(twinNode, twinPQ, twinNode.numberOfMoves + 1);
        }
    }
       
    private class SearchNode implements Comparable<SearchNode> {
    /* Private class that represents one Search Node in the A* algorithm. Each node contains a board, the number of
     * moves taken to get to that board state, and the previous board. numberOfMoves is kept track of to help determine
     * priority (calculated by adding manhattan() to numberOfMoves) and prev is kept track of both to avoid adding in
     * redundant nodes, and to trace the winning path back to initial state */
        private Board board;
        private int priority;
        private int numberOfMoves;
        private SearchNode prev;
        
        public SearchNode(Board board, int numberOfMoves, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.numberOfMoves = numberOfMoves;
            priority = board.manhattan() + numberOfMoves;
        }
        
        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            else if (this.priority > that.priority) return 1;
            else return 0;
        }
    }
        
    private void insertNeighbors(SearchNode node, MinPQ<SearchNode> nodePQ, int move) {
    // Inserts all neighbors of node into nodePQ, with the exception of node's previous node
        Iterable<Board> neighbors = node.board.neighbors();
        for (Board neighbor : neighbors)
            if (node.prev == null || !neighbor.equals(node.prev.board))
                nodePQ.insert(new SearchNode(neighbor, move, node));
    }
    
    public boolean isSolvable()
    // Is the initial board given as input solvable?
    {    return solveable;  }
    
    public int moves()     
    // How many moves did it take to solve initial? Returns -1 if initial is unsolvable
    {    return movesToSolution;    }    
    
    public Iterable<Board> solution() {
    // Returns the path the initial board took to solved state. Returns null if unsolvable
        if (movesToSolution >= 0) return boardPath;
        else return null;
    }
            
    public static void main(String[] args) {   

    }
}
