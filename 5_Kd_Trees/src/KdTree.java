import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        private KdNode left;  // The left/bottom subtree
        private KdNode right; // The right/top subtree

        public KdNode(Point2D point) {
            this.point = point;
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

        this.root = this.insert(this.root, p, true);
        this.size++;
    }

    /**
     * Insert new point
     * @param node the {@link KdNode}
     * @param point the {@link Point2D}
     * @param horizontal true if the orientation is horizontal
     */
    private KdNode insert(KdNode node, Point2D point, boolean horizontal) {
        if (node == null) return new KdNode(point);

        boolean left = isLeft(node, point, horizontal);

        if (left) node.left  = insert(node.left,  point, !horizontal);
        else      node.right = insert(node.right, point, !horizontal);

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

        if (this.root != null) drawNode(this.root, new RectHV(0.0, 0.0, 1.0, 1.0), true);
    }

    private void drawNode(KdNode node, RectHV rect, boolean horizontal) {
        // Draw line
        StdDraw.setPenRadius();
        if (horizontal) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
        }

        // Draw point
        drawPoint(node.point);

        if (node.left != null)  this.drawNode(node.left,  leftRect(node.point, rect,   horizontal), !horizontal);
        if (node.right != null) this.drawNode(node.right, rightRect(node.point, rect, horizontal), !horizontal);
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

        return range(this.root, rect, new RectHV(0.0, 0.0, 1.0, 1.0), true, new ArrayList<>());
    }

    private Iterable<Point2D> range(KdNode node, RectHV rect, RectHV nodeRect, boolean horizontal, List<Point2D> points) {
        if (node == null) return points;

        if (rect.contains(node.point)) {
            points.add(node.point);
        }

        if (horizontal) {
            if (rect.xmin() < node.point.x()) {
                nodeRect = leftRect(node.point, nodeRect, horizontal);
                range(node.left,  rect, nodeRect, !horizontal, points);
            }
            if (rect.xmax() >= node.point.x()) {
                nodeRect = rightRect(node.point, nodeRect, horizontal);
                range(node.right, rect, nodeRect, !horizontal, points);
            }
        }
        else {
            if (rect.ymin() < node.point.y()) {
                nodeRect = leftRect(node.point, nodeRect, horizontal);
                range(node.left,  rect, nodeRect, !horizontal, points);
            }
            if (rect.ymax() >= node.point.y()) {
                nodeRect = rightRect(node.point, nodeRect, horizontal);
                range(node.right, rect, nodeRect, !horizontal, points);
            }
        }

        return points;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * @param point the point
     * @return a nearest neighbor to the point
     */
    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Point must be not null!");
        if (this.isEmpty()) return null;

        return nearest(this.root, point, new RectHV(0.0, 0.0, 1.0, 1.0), true, Double.MAX_VALUE);
    }

    private Point2D nearest(KdNode node, Point2D point, RectHV nodeRect, boolean horizontal, double nearestDistance) {
        if (node == null) return null;

        double rectDistance = nodeRect.distanceSquaredTo(point);
        if (rectDistance >= nearestDistance) return null;

        Point2D found = null;
        double distance = node.point.distanceSquaredTo(point);
        if (distance < nearestDistance) {
            found = node.point;
            nearestDistance = distance;
        }

        KdNode subtree1;
        KdNode subtree2;
        RectHV rect1;
        RectHV rect2;
        boolean left = isLeft(node, point, horizontal);
        if (left) {
            subtree1 = node.left;
            subtree2 = node.right;
            rect1 = leftRect(node.point, nodeRect, horizontal);
            rect2 = rightRect(node.point, nodeRect, horizontal);
        }
        else {
            subtree1 = node.right;
            subtree2 = node.left;
            rect1 = rightRect(node.point, nodeRect, horizontal);
            rect2 = leftRect(node.point, nodeRect, horizontal);
        }

        // Search in left subtree
        Point2D first = nearest(subtree1, point, rect1, !horizontal, nearestDistance);
        if (first != null) {
            distance = first.distanceSquaredTo(point);
            if (distance < nearestDistance) {
                found = first;
                nearestDistance = distance;
            }
        }

        // Search in right subtree
        Point2D second = nearest(subtree2, point, rect2, !horizontal, nearestDistance);
        if (second != null) {
            distance = second.distanceSquaredTo(point);
            if (distance < nearestDistance) {
                found = second;
            }
        }

        return found;
    }

    /**
     * Returns true if the node is left
     * @param node the {@link KdNode}
     * @param point the {@link Point2D}
     * @param horizontal if the node is oriented horizontally
     * @return true if the node is left
     */
    private boolean isLeft(KdNode node, Point2D point, boolean horizontal) {
        if (horizontal) return point.x() < node.point.x();
        else            return point.y() < node.point.y();
    }

    /**
     * Returns the left {@link RectHV} object for the node
     * @param point the {@link Point2D} to build the rect
     * @param parent parent rect
     * @param horizontal if the node is oriented horizontally
     * @return the {@link RectHV}
     */
    private RectHV leftRect(Point2D point, RectHV parent, boolean horizontal) {
        if (horizontal) return new RectHV(parent.xmin(), parent.ymin(), point.x(), parent.ymax());
        else            return new RectHV(parent.xmin(), parent.ymin(), parent.xmax(), point.y());
    }

    /**
     * Returns the right {@link RectHV} object for the node
     * @param point the {@link Point2D} to build the rect
     * @param parent parent rect
     * @param horizontal if the node is oriented horizontally
     * @return the {@link RectHV}
     */
    private RectHV rightRect(Point2D point, RectHV parent, boolean horizontal) {
        if (horizontal) return new RectHV(point.x(), parent.ymin(), parent.xmax(), parent.ymax());
        else            return new RectHV(parent.xmin(), point.y(), parent.xmax(), parent.ymax());
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

        tree = new KdTree();
        tree.insert(new Point2D(0.25, 0.75));
        tree.insert(new Point2D(0.75, 0.00));
        tree.insert(new Point2D(0.75, 1.00));
        tree.insert(new Point2D(0.75, 0.25));
        tree.insert(new Point2D(1.00, 0.00));
        tree.insert(new Point2D(0.50, 0.25));
        tree.insert(new Point2D(0.50, 0.75));
        tree.insert(new Point2D(1.00, 0.50));
        tree.insert(new Point2D(0.25, 0.50));
        tree.insert(new Point2D(0.50, 0.50));

        Point2D nearest = tree.nearest(new Point2D(0.00, 0.50));
        System.out.println(nearest);
        assert nearest.equals(new Point2D(0.25, 0.50));
    }

}
