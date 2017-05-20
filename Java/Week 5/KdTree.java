/* A generalization of a Binary Search Tree that organizes 2D Points (represented with the class Point2D) within a unit square and supports range 
 * search & nearest neighbor operations. This specific type of KdTree is a 2D tree - the unit square is partitioned into halves every time a new 
 * Node (containing a Point2D) is inserted into the tree.
 * 
 * Each Point2D corresponds to the orthogonal rectangle it inherited (e.g. initial Point2D corresponds to the unit square rectangle). Nodes on an 
 * even height of the KdTree split their rectangle vertically, while nodes on an odd height split their rectangle horizontally (e.g. the root Node 
 * of the tree splits the unit rectangle vertically, based on the x-value of the root point). When inserting new Nodes into the tree, comparisons on
 * even heights are performed using x values, while comparisons on odd heights are performed using y values. For instance, with a tree that only has a 
 * root node with a Point2D of (0.5, 0.4) (that will split the unit rectangle vertically because it has an even height of 0), inserting a Node with the 
 * Point2D (0.4, 0.3) will put that Node in the left subtree, as 0.4 < 0.5. The rectangle that that Node then corresponds to is represented by the 
 * RectHV (0.0, 0.5, 0.0, 0.4) - it corresponds to the rectangle to the left of the vertical split made by Point2D (0.5, 0.4). */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int n;
    
    public KdTree() {
    // Initializes the tree with null root. n represents # of nodes for quick size() return
        root = null;
        n = 0;
    }
    
    private class Node {    
    /* A node within the KdTree. The key depends on the node height and the values stored are a 2D point and the rectangle that encloses all the points
     * in that point's subtrees. rect is stored to facilitate quick draw()*/
        private Node left, right;
        private Point2D point;
        private RectHV rect;
        private int height;
        
        public Node(double x, double y, int height, RectHV rect) {
            point = new Point2D(x, y);
            this.height = height;
            this.rect = rect;
        }
    }
    
    public boolean isEmpty() {
    // Is the tree empty?
        return root == null;
    }
    
    public int size() {
    // How many nodes?
        return n;
    }
    
    public void insert(Point2D p) {
    // Insert a new point into the KdTree
        root = insert(root, p, 0, 0, 0, 1, 1);
    }
    
    private Node insert(Node node, Point2D p, int height, double xmin, double ymin, double xmax, double ymax) {         
    /* Helper function for insert(Point2D p). Recursively keeps track of the current node, height, and rect
     * dimensions, so each node can be inserted in logN time without creating a new RectHV object every call.
     * At even heights, insert comparison between nodes is determined by x value of p and node.point.x. At
     * odd heights, insert comparison is determined by y value instead. */
        if (node == null) {
            n++;
            return new Node(p.x(), p.y(), height, new RectHV(xmin, ymin, xmax, ymax));
        }
        if (node.point.equals(p)) return node;       
        
        int cmp;        
        if (height % 2 == 0) {
            cmp = Double.compare(p.x(), node.point.x());            
            if (cmp < 0) node.left  = insert(node.left, p, height + 1,
                                             xmin, ymin, node.point.x(), ymax);
            else         node.right = insert(node.right, p, height + 1,
                                             node.point.x(), ymin, xmax, ymax);
        }               
        else {
            cmp = Double.compare(p.y(), node.point.y());
            if (cmp < 0) node.left  = insert(node.left, p, height + 1,
                                             xmin, ymin, xmax, node.point.y());
            else         node.right = insert(node.right, p, height + 1,
                                             xmin, node.point.y(), xmax, ymax);
        }
        
        return node;
    }
    
    public boolean contains(Point2D p) {
    // Check if the KdTree contains point p
        Node node = root;
        while (node != null) {
            if (node.point.equals(p)) return true;
            
            int cmp;
            if (node.height % 2 == 0) cmp = Double.compare(p.x(), node.point.x());
            else                      cmp = Double.compare(p.y(), node.point.y());
            
            if (cmp < 0) node = node.left;
            else         node = node.right;
        }
        return false;
    }
    
    public void draw() {
    // Draw out every point and rect division in the KdTree (for debugging, not optimized)
        draw(root);
    }
    
    private void draw(Node node) {
    // Helper function for draw. Points are black, horizontal lines are drawn blue, vertical lines are drawn red 
        if (node == null) return;
        
        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        StdDraw.setPenRadius();
        
        if (node.height % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());            
        }        
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        
        draw(node.left);
        draw(node.right);
    }
    
    public Iterable<Point2D> range(RectHV rect) {
    // Returns an iterable of every point in the KdTree that lies within the specific range
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        range(root, rect, result);
        return result;
    }
        
    private void range(Node node, RectHV rect, ArrayList<Point2D> result) {
    /* Helper function for range(RectHV rect). Recursively add all valid points to ArrayList result
     * If node's rectangle doesn't intersect at all with the range, there is no need to search node
     * or any of node's subtrees for points */
        if (node == null || !node.rect.intersects(rect)) return;
        
        if (rect.contains(node.point)) result.add(node.point);
        range(node.left, rect, result);
        range(node.right, rect, result);
    }
    
    public Point2D nearest(Point2D p) {
    // Returns the point in the KdTree nearest to point p, or null if the tree is empty
        if (root == null) return null;
        return nearest(root, p, root.point);
    }
    
    private Point2D nearest(Node node, Point2D p, Point2D nearestPoint) {
    /* Helper function for nearest(Point2D p). Keeps track of nearest point so far and compares that
     * against successive points recursively. If nearestPoint is <= the closest intersection between
     * p and node's rect, then there is no need to search node or any of node's subtrees*/       
        if (node == null || nearestPoint.distanceTo(p) <= node.rect.distanceTo(p)) return nearestPoint;               
        if (node.point.distanceTo(p) < nearestPoint.distanceTo(p)) 
            nearestPoint = node.point;
        
        if (node.right == null || (node.left != null && node.left.rect.distanceTo(p) <= node.right.rect.distanceTo(p))) {
            nearestPoint = nearest(node.left, p, nearestPoint);
            if (node.right != null && nearestPoint.distanceTo(p) > node.right.rect.distanceTo(p))
                nearestPoint = nearest(node.right, p, nearestPoint);
        }
        else {
            nearestPoint = nearest(node.right, p, nearestPoint);
            if (node.left != null && nearestPoint.distanceTo(p) > node.left.rect.distanceTo(p))
                nearestPoint = nearest(node.left, p, nearestPoint);
        }
        
        return nearestPoint;
    }
    
    public static void main(String[] args) {

    }
}
