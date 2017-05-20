/* Class that finds all maximal non-degenerate non-duplicate lines with at least 4 Points within the given array (called points) within (n^2)logn time 
 * (compared to n^4 time of the brute force variant). For each point p in points, FastCollearPoints sorts all the other points by the slope p makes with 
 * those points. All points with the same slope must exist on the same line, and FastCollinearPoints creates the longest non-degenerate line possible out 
 * of those points. To avoid duplicates, lines are only added if p is the smallest value in that line. Points are compared using the compareTo() method
 * from the class Points */

import java.util.Arrays;   

public class FastCollinearPoints {
    /* To greatly reduce the cost of each individual call to the getters numberOfSegments() and segments(), all maximal 
     * line segments are found during the initialization of FastCollinearPoints */
    private int n;
    private LineSegment[] segments;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();
        if (containsDupe(points)) throw new IllegalArgumentException();
        
        n = 0;
        segments = new LineSegment[2];
  
        findLines(points);
    }
        
    private void findLines(Point[] points) {
    // Finds all maximal lines within points
        int len = points.length;
        
        Point[] copy = new Point[len];        
        for (int i = 0; i < len; i++)
            copy[i] = points[i];
        // copy is used to sort points by slopeTo p, while points is maintained as the original array for index reference
        
        for (int i = 0; i < len; i++) {
        // Finds maximal lines by iterating through each point in points
            Point p = points[i];
            Arrays.sort(copy, p.slopeOrder());     
    
            int count = 1;
            for (int j = 1; j < len - 1; j++) {
            // Iterate through copy after it has been sorted by slopeTo p
                
                if (p.slopeTo(copy[j]) == p.slopeTo(copy[j + 1])) {
                // As long as the next value in the array is the same as the current, keep on counting
                    count++;
                    if (j == len - 2 && count >= 3) addLine(copy, p, count, j + 1);
                }
                
                else if (count >= 3) {
                // Once the chain of similar values in copy is broken, you have found the maximal line
                    addLine(copy, p, count, j);
                    count = 1;
                }
                else count = 1;
            }           
        }
    } 
    
    private void addLine(Point[] copy, Point p, int count, int pos) {
    /* Add a maximal line to LineSegment[] segments by iterating backwards through copy until you have found all
     * points that have the same slope. copy will always be the same array copy as in findLines, p is the initial
     * point p that is the current iteration value of points, count is the number of points with the same slope,
     * and pos is the index value of copy to begin counting backwards from*/
        Point[] coll = new Point[count + 1];
        
        for (int k = 0; k < count; k++)
        {   coll[k] = copy[pos - k]; }        
        coll[count] = p;        
        
        Arrays.sort(coll);
        
        if (p == coll[0]) {
        // To avoid duplicates, only add this line to segments if p is the smallest value based on Point's compareTo()
            segments[n++] = new LineSegment(coll[0], coll[coll.length - 1]);
            if (n == segments.length) resize(2 * n);
        }
    }
       
    private void resize(int capacity) {
    // Efficiently resize a fixed length array
        LineSegment[] copy = (new LineSegment[capacity]);
        for (int i = 0; i < n; i++) copy[i] = segments[i];
        segments = copy;
    }
    
    private boolean containsDupe(Point[] points) {
    // To check if points contains a dupe
        for (int i = 0; i < points.length - 1; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0) return true;
        return false;
    }
    
    public int numberOfSegments()
    // How many non-duplicate non-degenerate maximal line segments are there?
    {    return n;  }
    
    public LineSegment[] segments() {
    // Return all non-duplicate non-degenerate maximal line segments
        LineSegment[] copy = new LineSegment[n];
        for (int i = 0; i < n; i++)
            copy[i] = segments[i];
        return copy;
    }  
    
    public static void main(String[] args) {
        
    }
}
