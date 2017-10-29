import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A mutable data type that represents a set of points in the unit square.
 * Created by aonnikov on 22/10/2017.
 */
public class KdTree {

    // The root node
    private KdNode root;

    // Number of the nodes in the tree
    private int size;

    private static class KdNode {
        private final Point2D point; // The point

        private final RectHV rect; // The axis-aligned rectangle corresponding to this node
        private KdNode left;  // The left/bottom subtree

        private KdNode right; // The right/top subtree

        public KdNode(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }

    }

    /**
     * Construct an empty set of points
     */
    public KdTree() {
        this.size = 0;
    }

    /**
     * Is the set empty?
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Number of points in the set
      * @return number of points in the set
     */
    public int size() {
        return this.size;
    }

    /**
     * Add the point to the set (if it is not already in the set)
     * @param p the point
     */
    public void insert(Point2D p) {
        if (contains(p)) return;

        this.root = this.insert(this.root, p, true, new RectHV(0, 0, 1, 1));
        this.size++;
    }

    /**
     * Insert new point
     * @param node the {@link KdNode}
     * @param point the {@link Point2D}
     * @param horizontal true if the orientation is horizontal
     */
    private KdNode insert(KdNode node, Point2D point, boolean horizontal, RectHV rect) {
        if (node == null) return new KdNode(point, rect);

        boolean left = isLeft(node, point, horizontal);

        if (left) {
            if (horizontal) rect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
            else            rect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
            node.left  = insert(node.left,  point, !horizontal, rect);
        }
        else {
            if (horizontal) rect = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            else            rect = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
            node.right = insert(node.right, point, !horizontal, rect);
        }

        return node;
    }

    /**
     * Does the set contain point p?
     * @param p the point
     * @return true if the set contains the point
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point must be not null!");
        }
        return this.get(p) != null;
    }

    private KdNode get(Point2D point) {
        return get(this.root, point, true);
    }

    private KdNode get(KdNode node, Point2D point, boolean horizontal) {
        if (node == null) return null;
        if (node.point.equals(point)) return node;

        boolean left = isLeft(node, point, horizontal);

        if (left) return get(node.left,  point, !horizontal);
        else      return get(node.right, point, !horizontal);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {

        // Draw border;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);

        if (this.root != null) drawVNode(this.root);
    }

    private void drawVNode(KdNode node) {
        // Draw line
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());

        // Draw point
        drawPoint(node.point);

        if (node.left != null) this.drawHNode(node.left);
        if (node.right != null) this.drawHNode(node.right);
    }

    private void drawHNode(KdNode node) {
        // Draw line
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());

        // Draw point
        drawPoint(node.point);

        if (node.left != null) this.drawVNode(node.left);
        if (node.right != null) this.drawVNode(node.right);
    }

    private void drawPoint(Point2D point) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(point.x(), point.y());
    }

    /**
     * All points that are inside the rectangle (or on the boundary)
     * @param rect the rectangle
     * @return all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle must be not null!");
        }

        return range(this.root, rect, new HashSet<>(), true);
    }

    private Iterable<Point2D> range(KdNode node, RectHV rect, Set<Point2D> pointSet, boolean horizontal) {
        if (node == null) return pointSet;

        if (rect.contains(node.point)) {
            pointSet.add(node.point);
        }

        if (horizontal) {
            if (rect.xmin() < node.point.x())  range(node.left,  rect, pointSet, !horizontal);
            if (rect.xmax() >= node.point.x()) range(node.right, rect, pointSet, !horizontal);
        }
        else {
            if (rect.ymin() < node.point.y())  range(node.left,  rect, pointSet, !horizontal);
            if (rect.ymax() >= node.point.y()) range(node.right, rect, pointSet, !horizontal);
        }

        return pointSet;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * @param point the point
     * @return a nearest neighbor to the point
     */
    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Point must be not null!");
        if (this.root == null) throw new IllegalArgumentException("Tree is empty!");

        return nearest(this.root, point, true);
    }

    private Point2D nearest(KdNode node, Point2D point, boolean horizontal) {
        if (node == null) return null;

        boolean left = isLeft(node, point, horizontal);

        Point2D other;
        if (left) other = nearest(node.left, point, !horizontal);
        else other = nearest(node.right, point, !horizontal);

        if (other != null) {
            double d1 = node.point.distanceSquaredTo(point);
            double d2 = other.distanceSquaredTo(point);
            if (d2 < d1) return other;
        }

        return node.point;
    }

    private boolean isLeft(KdNode node, Point2D point, boolean horizontal) {
        if (horizontal) return point.x() < node.point.x();
        else            return point.y() < node.point.y();
    }

    public static void main(String[] args) {

        Point2D point0 = new Point2D(0.7, 0.2);
        Point2D point1 = new Point2D(0.5, 0.4);
        Point2D point2 = new Point2D(0.2, 0.3);
        Point2D point3 = new Point2D(0.4, 0.7);
        Point2D point4 = new Point2D(0.9, 0.6);
        Point2D point5 = new Point2D(0.2, 0.3); // Duplicated

        KdTree tree = new KdTree();
        tree.insert(point0);
        tree.insert(point1);
        tree.insert(point2);
        tree.insert(point3);
        tree.insert(point4);
        tree.insert(point4);

        // Test insertion
        assert tree.size() == 5;

        assert tree.root.point == point0;
        assert tree.root.left.point == point1;
        assert tree.root.left.left.point == point2;
        assert tree.root.left.right.point == point3;
        assert tree.root.right.point == point4;

        assert tree.contains(point0);
        assert tree.contains(point1);
        assert tree.contains(point2);
        assert tree.contains(point3);
        assert tree.contains(point4);
        assert tree.contains(point5);

        assert !tree.contains(new Point2D(0.1, 0.1));

        Iterable<Point2D> points = tree.range(new RectHV(0.15, 0.2, 0.25, 0.4));
        Iterator<Point2D> iterator = points.iterator();

        assert iterator.hasNext();
        assert iterator.next().equals(point2);
        assert !iterator.hasNext();

        points = tree.range(new RectHV(0.6, 0.1, 0.9, 0.6));
        iterator = points.iterator();

        assert iterator.hasNext();
        assert iterator.next().equals(point0);
        assert iterator.next().equals(point4);
        assert !iterator.hasNext();

        assert  tree.nearest(new Point2D(0.0, 0.0)).equals(point2);
        assert  tree.nearest(new Point2D(0.7, 0.2)).equals(point0);
        tree.draw();
    }

}
