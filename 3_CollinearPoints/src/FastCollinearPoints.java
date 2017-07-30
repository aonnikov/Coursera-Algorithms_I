import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final List<LineSegment> lineSegments = new ArrayList<>();

    /**
     * Finds all line segments containing 4 points
     */
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);

        Point[] pointsCopy = points.clone();
        Point[] sortedPoints = points.clone();
        Arrays.sort(pointsCopy);
        int pointsCount = pointsCopy.length;

        for (Point point : pointsCopy) {
            // Order the points by their slope to P
            Arrays.sort(sortedPoints, point.slopeOrder());
            // Start from the second point because the first is the point itself
            for (int start = 1, end = 2; start < pointsCount - 2;) {
                double slope = point.slopeTo(sortedPoints[start]);

                // While the next slope is equal to the initial do move right
                while (end < pointsCount && Double.compare(slope, point.slopeTo(sortedPoints[end])) == 0) {
                    end++;
                }

                // If found more than 3 points with the same slope then make a LineSegment
                if ((end - start) >= 3) {
                    Arrays.sort(sortedPoints, start, end);
                    if (sortedPoints[start].compareTo(point) > 0) {
                        this.lineSegments.add(new LineSegment(point, sortedPoints[end - 1]));
                    }
                }

                start = end;
                end++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}