/* Class that uses brute force to determine how many non-degenerate non-duplicate 4-point lines exist within points. For this class, it can be assumed that points
 * will only contain lines of 4 points maximum. BruteCollinearPoints iterates through every possible combination of points that exists within points and adds a new
 * LineSegment that contains those four points */

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int n;
    
    public BruteCollinearPoints(Point[] points) {        
    /* To greatly reduce the cost of each individual call to numberOfSegments() and segments(), all valid line segments
     * are found during the initialization of BruteCollinearPoints */
        if (points == null) throw new NullPointerException();
        if (containsDupe(points)) throw new IllegalArgumentException();
        
        n = 0;
        segments = new LineSegment[2];
        
        if (points.length < 4) return;
        
        for (int i = 0; i < points.length - 3; i++) {
        // Point collinearity is determined by their slopes to each other
            Point p = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                Point q = points[j];
                for (int k = j + 1; k < points.length - 1; k++) {                    
                    Point r = points[k];
                    if (p.slopeTo(q) != p.slopeTo(r)) continue;
                    for (int l = k + 1; l < points.length; l++) {
                        Point s = points[l];
                        if (q.slopeTo(r) == q.slopeTo(s)) {
                            Point[] colPoints = new Point[]{p, q, r, s};
                            Arrays.sort(colPoints);
                   
                            segments[n++] = new LineSegment(colPoints[0], colPoints[3]);
                            if (n == segments.length) resize(2 * n);
                            
                        }
                    }
                }
            }
        }                
    }   
    
    public int numberOfSegments()
    // Returns number of valid segments
    {    return n;  }
    
    public LineSegment[] segments() {
    // Returns an array of all valid segments
        LineSegment[] copy = new LineSegment[n];
        for (int i = 0; i < n; i++) {
            copy[i] = segments[i];
        }
        return copy;
        
    }
    
    private boolean containsDupe(Point[] points) {
    // Does points contain a duplicate point?
        for (int i = 0; i < points.length - 1; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0) return true;
        return false;
    }

    private void resize(int capacity) {
    // Efficiently resize a fixed length array
        LineSegment[] copy = (new LineSegment[capacity]);
        for (int i = 0; i < n; i++) copy[i] = segments[i];
        segments = copy;
    }
    
    public static void main(String[] args) {        
        
    }
    
}
