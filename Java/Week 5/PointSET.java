/* A brute-force model that uses the java Red Black TreeSet class to support range search and nearest neighbor search operations of all points contained within
 * the PointSET. Points are represented by the provided java class Point2D and range searches are represented by the provided class RectHv */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET {
    private TreeSet<Point2D> points;
    private int n;
    
    public PointSET() {
    // Initializes the PointSET
        points = new TreeSet<Point2D>();        
    }
    
    public boolean isEmpty() {
    // Is the PointSET empty?
        return points.isEmpty();
    }
    
    public int size() {
    // Returns the number of points in the PointSET
        return n;
    }
    
    public void insert(Point2D p) {
    // Inserts a new point into the PointSET
        if (p == null) throw new NullPointerException();
        
        if (points.add(p)) n++;
    }
    
    public boolean contains(Point2D p) {    
    // Does the PointSET contain the point p?
        if (p == null) throw new NullPointerException();        
        return points.contains(p);
    }
    
    public void draw() {
    // Draws out every point in the PointSET (for debugging purposes)
        for (Point2D point : points)
            point.draw();
    }
    
    public Iterable<Point2D> range(RectHV rect) {
    // Returns every point in the PointSET that is contained with the range provided by rect
        if (rect == null) throw new NullPointerException();
        
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        for (Point2D point : points)
            if (rect.contains(point))
                result.add(point);
        return result;
    }      
    
    public Point2D nearest(Point2D p) {
    // Returns the point in PointSET closest to point p
        if (p == null) throw new NullPointerException();
   
        if (isEmpty()) return null;
        
        Point2D nearest = null;                
        for (Point2D point : points)
            if (nearest == null || point.distanceTo(p) < nearest.distanceTo(p))
                nearest = point;
        return nearest;
    }
    
    public static void main(String[] args) {

    }
    
}
