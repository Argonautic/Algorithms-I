/* Models a standard point in a 2D Cartesian graph, to be used with BruteCollinearPoints and FastCollinearPoints to determine the number of maximal non-degenerate
 * non-duplicate lines with at least four points that exist within a given input of points. All Points have an x and y value that can be any integer between 0 and 
 * 32,767, and Point contains a compareTo satisfies the Comparable interface */

import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
    // Initializes Point
        this.x = x;
        this.y = y;
    }
    
    public void draw()
    /* Draws Point within edu.princeton.cs.algs4.StdDraw for debugging purposes. By default StdDraw
     * is configured to be a unit square ((0, 0) - > (1, 1))*/
    {   StdDraw.point(x, y);   }
    
    public void drawTo(Point that)
        // Draws a line from this Point to that Point
    {    StdDraw.line(this.x, this.y, that.x, that.y);  }
    
    public String toString()
    // String representation of this Point
    {    return "(" + x + ", " + y + ")";   }    
    
    public int compareTo(Point that) {
        if (y > that.y) return 1;
        if (y < that.y) return -1;
        if (x > that.x) return 1;
        if (x < that.x) return -1;
        return 0;
    }
    
    public double slopeTo(Point that) {
        if (that.y == y && that.x == x) return Double.NEGATIVE_INFINITY;
        else if (that.x == x) return Double.POSITIVE_INFINITY;
        else if (that.y == y) return +0.0;
        else return ((double) (that.y - y) / (that.x - x));
    }
    
    public Comparator<Point> slopeOrder() {
        return new BySlope();
    }
    
    private class BySlope implements Comparator<Point> {
        public int compare(Point v, Point w) {
            if (slopeTo(v) > slopeTo(w)) return 1;
            if (slopeTo(v) < slopeTo(w)) return -1;
            return 0;
        }
    }
    
    public static void main(String[] args) {        
        
    }
    
}
