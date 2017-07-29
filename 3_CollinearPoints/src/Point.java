/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        int dx = that.x - this.x;
        int dy = that.y - this.y;

        if (dx == 0 && dy == 0) {
            // Points are equal
            return Double.NEGATIVE_INFINITY;
        }
        else if (dx == 0) {
            // Line segment is vertical
            return Double.POSITIVE_INFINITY;
        }
        else if (dy == 0) {
            // Line segment is horizontal
            return +0.0;
        }
        else {
            return dx / dy;
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return this.x - that.x;
        }
        else {
            return this.y - that.y;
        }
    }

    /**
     * Comparator which compares two {@link Point} by the slope they make with this point.
     */
    private class SlopeOrder implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            return Double.compare(slopeTo(p1), slopeTo(p2));
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {

        Point p = new Point(5, 5);

        // Slope tests
        double slope = p.slopeTo(p);
        System.out.println("Slope to itself: " + slope);
        assert slope == Double.NEGATIVE_INFINITY;

        slope = p.slopeTo(new Point(5, 10));
        System.out.println("Slope for vertical line: " + slope);
        assert slope == Double.POSITIVE_INFINITY;

        slope = p.slopeTo(new Point(10, 5));
        System.out.println("Slope for horizontal line: " + slope);
        assert slope == +0.0;

        slope = p.slopeTo(new Point(10, 10));
        System.out.println("Slope: " + slope);
        assert slope == 1.0;

        // Compare tests
        assert p.compareTo(p) == 0;
        assert p.compareTo(new Point(5, 10)) < 0;
        assert p.compareTo(new Point(5, 0)) > 0;
        assert p.compareTo(new Point(10, 5)) < 0;
        assert p.compareTo(new Point(0, 5)) > 0;
    }
}