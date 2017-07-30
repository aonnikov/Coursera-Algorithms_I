import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A stupid brute force implementation to find collinear points
 */
public class BruteCollinearPoints {

    private final List<LineSegment> lineSegments = new ArrayList<>();

    /**
     * Finds all line segments containing 4 points
     */
    public BruteCollinearPoints(Point[] points) {
        validatePoints(points);

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        for (int p = 0; p < sortedPoints.length - 3; p++) {
            Point point1 = sortedPoints[p];
            for (int q = p + 1; q < sortedPoints.length - 2; q++) {
                Point point2 = sortedPoints[q];
                double slope2 = point1.slopeTo(point2);
                for (int r = q + 1; r < sortedPoints.length - 1; r++) {
                    Point point3 = sortedPoints[r];
                    double slope3 = point1.slopeTo(point3);
                    if (Double.compare(slope2, slope3) == 0) {
                        for (int s = r + 1; s < sortedPoints.length; s++) {
                            Point point4 = sortedPoints[s];
                            double slope4 = point1.slopeTo(point4);

                            if (Double.compare(slope3, slope4) == 0) {
                                LineSegment lineSegment = new LineSegment(point1, point4);
                                lineSegments.add(lineSegment);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Validate the points.
     * Throw a {@link IllegalAccessException} if the points array is null,
     * if any point in the array is null, or if the argument to the constructor
     * contains a repeated point.
     * @param points the points to validate
     */
    private void validatePoints(Point[] points) {
        // Check the points are not null
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // Check that there are no nulls in the array
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        // Check duplicates
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    /**
     * The number of line segments containing 4 points
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return this.lineSegments.size();
    }

    /**
     * Returns all found line segments containing 4 points
     * @return line segments
     */
    public LineSegment[] segments() {
        return this.lineSegments.toArray(new LineSegment[this.lineSegments.size()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}