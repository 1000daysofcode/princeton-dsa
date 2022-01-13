/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {
    private final LineSegment[] lines;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Argument to constructor cannot be null");
        if (checkNullPoints(points))
            throw new IllegalArgumentException("There should be no null points");

        Point[] pts = Arrays.copyOf(points, points.length);
        Arrays.sort(pts);

        if (checkDuplicates(pts))
            throw new IllegalArgumentException("There should be no duplicate points");

        for (int i = 1; i < pts.length; i++)
            if (pts[i] == pts[i - 1])
                throw new IllegalArgumentException("There cannot be duplicate points");

        ArrayList<LineSegment> tempSegs = new ArrayList<>();

        for (int i = 0; i < pts.length - 3; i++) {
            if (pts[i] == null) throw new IllegalArgumentException("Point cannot be null");
            for (int j = i + 1; j < pts.length - 2; j++) {
                if (pts[j] == pts[j - 1])
                    throw new IllegalArgumentException("There cannot be duplicate points");
                for (int k = j + 1; k < pts.length - 1; k++) {
                    double slopeIJ = pts[i].slopeTo(pts[j]),
                            slopeJK = pts[j].slopeTo(pts[k]);

                    if (slopeIJ == slopeJK) {
                        for (int ll = k + 1; ll < pts.length; ll++) {
                            if (pts[ll] == pts[ll - 1])
                                throw new IllegalArgumentException(
                                        "There cannot be duplicate points");
                            double slopeLL = pts[k].slopeTo(pts[ll]);

                            if (slopeJK == slopeLL) {
                                LineSegment newLine = new LineSegment(pts[i], pts[ll]);
                                if (!tempSegs.contains(newLine)) tempSegs.add(newLine);
                            }
                        }
                    }
                }
            }
        }
        lines = tempSegs.toArray(new LineSegment[tempSegs.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineCopy = lines.clone();
        return lineCopy;
    }

    private boolean checkNullPoints(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) return true;
        }
        return false;
    }

    private boolean checkDuplicates(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i] == points[i - 1]) return true;
        }
        return false;
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

        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        StdOut.println("Number of segments: " + bcp.numberOfSegments());
        StdOut.println("Collinear Points:");
        for (LineSegment line : bcp.segments()) {
            StdOut.println(line);
        }
        StdOut.println();
    }
}
