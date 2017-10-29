import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A mutable data type that represents a set of points in the unit square.
 * Created by aonnikov on 22/10/2017.
 */
public class PointSET {

    private final Set<Point2D> pointSet;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        this.pointSet = new TreeSet<>();
    }

    /**
     * Is the set empty?
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    /**
     * Number of points in the set
      * @return number of points in the set
     */
    public int size() {
        return this.pointSet.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     * @param p the point
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point must be not null!");
        }
        this.pointSet.add(p);
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
        return this.pointSet.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        // Draw border;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();

        for (Point2D point: this.pointSet) {
            drawPoint(point);
        }
    }

    private void drawPoint(Point2D point) {
        StdDraw.setPenColor(StdDraw.BLACK);
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
        List<Point2D> pointsInRange = new ArrayList<>();
        for (Point2D point : this.pointSet) {
            if (rect.contains(point)) pointsInRange.add(point);
        }
        return pointsInRange;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * @param p the point
     * @return a nearest neighbor to the point
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point must be not null!");
        }
        if (this.isEmpty()) {
            return null;
        }
        Point2D nearest = null;
        double distance = Double.MAX_VALUE;
        for (Point2D candidate : this.pointSet) {
            double candidateDistance = p.distanceSquaredTo(candidate);
            if (candidateDistance < distance) {
                distance = candidateDistance;
                nearest = candidate;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        Point2D point0 = new Point2D(0.7, 0.2);
        Point2D point1 = new Point2D(0.5, 0.4);
        Point2D point2 = new Point2D(0.2, 0.3);
        Point2D point3 = new Point2D(0.4, 0.7);
        Point2D point4 = new Point2D(0.9, 0.6);
        Point2D point5 = new Point2D(0.2, 0.3); // Duplicated

        PointSET set = new PointSET();
        set.insert(point0);
        set.insert(point1);
        set.insert(point2);
        set.insert(point3);
        set.insert(point4);
        set.insert(point4);

        // Test insertion
        assert set.size() == 5;

        assert set.contains(point0);
        assert set.contains(point1);
        assert set.contains(point2);
        assert set.contains(point3);
        assert set.contains(point4);
        assert set.contains(point5);

        assert !set.contains(new Point2D(0.1, 0.1));

        Iterable<Point2D> points = set.range(new RectHV(0.15, 0.2, 0.25, 0.4));
        Iterator<Point2D> iterator = points.iterator();

        assert iterator.hasNext();
        assert iterator.next().equals(point2);
        assert !iterator.hasNext();

        points = set.range(new RectHV(0.6, 0.1, 0.9, 0.6));
        iterator = points.iterator();

        assert iterator.hasNext();
        assert iterator.next().equals(point0);
        assert iterator.next().equals(point4);
        assert !iterator.hasNext();

        assert  set.nearest(new Point2D(0.0, 0.0)).equals(point2);
        assert  set.nearest(new Point2D(0.7, 0.2)).equals(point0);

        set = new PointSET();
        set.insert(new Point2D(0.7, 0.2));
        set.insert(new Point2D(0.5, 0.4));
        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.4, 0.7));
        set.insert(new Point2D(0.9, 0.6));

        Point2D nearest = set.nearest(new Point2D(0.016, 0.605));
        System.out.println(nearest);
        assert nearest.equals(new Point2D(0.2, 3));
    }

}
