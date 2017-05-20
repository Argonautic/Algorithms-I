/* Class that checks for whether or not an n-by-n grid of open and closed sites percolates - that is, whether or not any site on the top row is connected with any 
 * site in the bottom row by a path of open sites. Uses a union-find mechanism (supplied by the class WeightedQuickUnionUF) to tie open sites together by a common 
 * root. Used by PercolationVisualizer to run a Monte Carlo simulation on percolating an n-by-n grid. Does not account for backwash */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	private WeightedQuickUnionUF id;
	private int[][] grid;
	private int n;
	private int openSites = 0;
	
	public Percolation(int n) {
	/* Initializes Percolation as an n-by-n grid. id is initialized as a UnionFind object that keeps track of which sites are connected to
	 * each other, while grid keeps track of which grids are open */
		if (n <= 0) throw new IllegalArgumentException();
		this.n = n;
		id = new WeightedQuickUnionUF((n * n) + 2); // comes with two virtual sites for quick connectivity check between top and bottom row
		grid = new int[n][n];
		
		for (int i = 1; i <= n; i++)
		{	id.union(0, i);	}   // to connect the top row to the virtual top site
		
		for (int i = (n * (n - 1) + 1); i <= (n * n); i++) 
		{	id.union(i, (n * n) + 1);	}   // to connect the bottom row to the bottom virtual site
	}
		
	public void open(int row, int col) {   
	/* To open a random site and connect with any adjacent open sites. Row and col inputs begin at 1 instead of 0, so
	 * a call to open the first site in the grid will take the input form of (row 1, col 1) */
		if (!validate(row, col)) throw new IndexOutOfBoundsException();
		
		if (!isOpen(row, col)) {
			grid[row - 1][col - 1] = 1;
			openSites++;
			
			for (int i = -1; i < 2; i += 2) {
				if (validate(row + i, col) && isOpen(row + i, col)) {
					id.union(xyto1D(row - 1, col), xyto1D(row + i - 1, col));
				} if (validate(row, col + i) && isOpen(row, col + i)) {
					id.union(xyto1D(row - 1, col), xyto1D(row - 1, col + i));
				}
			}
		}
	}
	
    private int xyto1D(int x, int y) {
        // Converts a 2D index value to its corresponding 1D index value
            return (x * n) + y;   
    }
       
    private boolean validate(int row, int col) {
    // Ensure a site at index [row][col] on grid falls within a valid site
        return (row <= n && col <= n && row >= 1 && col >= 1);
    }
	
	public boolean isOpen(int row, int col) {   
	// Check if a given site is open
		if (!validate(row, col)) throw new IndexOutOfBoundsException();	
		else return grid[row - 1][col - 1] == 1;
	}
	
	public boolean isFull(int row, int col) {   
	// Check if a given site is full
		if (!validate(row, col)) throw new IndexOutOfBoundsException();	
		else return id.connected(xyto1D(row - 1, col), 0) && isOpen(row, col);
	}
	
	public int numberOfOpenSites()   
	// Total number of open sites
	{	return openSites;	}
	
	public boolean percolates()    
	// does the object percolate?
	{	return id.connected(0, (n * n) + 1);    }
	
	public static void main(String[] args) {
		
	}

}
