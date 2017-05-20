/* Class that runs a MonteCarlo simulation on an n-by-n grid of closed and open sites, to computationally test the threshold at which that grid will mostly likely
 * percolate. Threshold is represented as the percentage of sites in the grid that are open when it Percolates. Other methods return useful statistics related to 
 * that grid's percolation. Draws from the class Percolation */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {   // Runs statistical analysis on Percolation objects
	private int myTrials;   // to keep track of sample size as instance variable
	private double[] thresholds;   // to store the results of when objects percolate
	private double myMean;   // to store mean as instance variable
	private double stdDeviation;   // to store standard deviation as instance variable
	private double confidenceLo;
	private double confidenceHi;
	
	public PercolationStats(int n, int trials) {
	// Initializes PercolationStats to randomly open sites on an n-by-n grid until that grid Percolates, and repeats a total of trials times
		if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
		
		myTrials = trials;
		thresholds = new double[trials];
		
		for (int i = 0; i < trials; i++) {   
		// Runs trials number of tests on Percolation objects and keep track of when they percolate
			Percolation percolator = new Percolation(n);
			while (!percolator.percolates()) 
			{	percolator.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);	}
			thresholds[i] = percolator.numberOfOpenSites() / Math.pow(n, 2); //threshold is percentage of sites open when object percolates
		}
		
	    myMean = StdStats.mean(thresholds);
	    stdDeviation = StdStats.stddev(thresholds);
	    confidenceLo = myMean - (1.96 * stdDeviation) / Math.sqrt(myTrials);
	    confidenceHi = myMean + (1.96 * stdDeviation) / Math.sqrt(myTrials);
	}
	
	public double mean()
	// Returns the mean threshold at which the Percolation grid percolates
	{	return myMean;   }
	
	public double stddev()
	// Returns standard deviation of threshold
	{	return stdDeviation;    }
	
	public double confidenceLo()
	// Returns the low confidence interval of percolation
	{	return confidenceLo;   }
	
	public double confidenceHi()
	// Returns the high confidence interval of percolation
	{	return confidenceHi;   }
	
	public static void main(String[] args) {

	}
}
