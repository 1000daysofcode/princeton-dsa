/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Argument to constructor cannot be null");
        if (checkNullPoints(points))
            throw new IllegalArgumentException("There should be no null points");

        Point[] pts = points.clone();
        Arrays.sort(pts);
        if (checkDuplicates(pts))
            throw new IllegalArgumentException("There should be no duplicate points");

        int n = pts.length;

        Point[] sortedPoints = new Point[pts.length - 1];
        for (int i = 0; i < n; i++) {
            if (pts[i] == null) throw new IllegalArgumentException("Points cannot be null");
            remSort(pts, sortedPoints, i);
            getLines(sortedPoints, pts[i]);
        }
    }

    private void getLines(Point[] pointsToCompare, Point refPt) {
        int numPoints = 2, end = -1;
        ArrayList<Point> tempLine = new ArrayList<>();
        for (int i = 0; i < pointsToCompare.length - 1; i++) {
            if (!tempLine.contains(refPt)) tempLine.add(refPt);
            if (!tempLine.contains(pointsToCompare[i])) tempLine.add(pointsToCompare[i]);
            if (refPt.slopeTo(pointsToCompare[i]) == pointsToCompare[i]
                    .slopeTo(pointsToCompare[i + 1])) {
                numPoints++;
                if (!tempLine.contains(pointsToCompare[i + 1]))
                    tempLine.add(pointsToCompare[i + 1]);
            }
            else if (numPoints > 3) {
                Point[] temp = tempLine.toArray(new Point[tempLine.size()]);
                Arrays.sort(temp);
                if (refPt.compareTo(temp[0]) == 0) {
                    LineSegment line = new LineSegment(temp[0], temp[temp.length - 1]);
                    if (!lines.contains(line)) lines.add(line);
                }
                numPoints = 2;
                tempLine.clear();
            }
            else {
                numPoints = 2;
                tempLine.clear();
            }
            if (i == pointsToCompare.length - 2 && numPoints > 3) {
                Point[] temp = tempLine.toArray(new Point[tempLine.size()]);
                Arrays.sort(temp);
                if (refPt.compareTo(temp[0]) == 0) {
                    LineSegment line = new LineSegment(temp[0], temp[temp.length - 1]);
                    if (!lines.contains(line)) lines.add(line);
                }
                numPoints = 2;
                tempLine.clear();
            }
        }
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

    private void remSort(Point[] points, Point[] sortedPoints, int p) {
        boolean reduce = false;
        for (int i = 0; i < points.length; i++) {
            if (i == p) {
                reduce = true;
                continue;
            }
            if (reduce) sortedPoints[i - 1] = points[i];
            else sortedPoints[i] = points[i];
        }
        Arrays.sort(sortedPoints, points[p].slopeOrder());
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineArray = lines.toArray(new LineSegment[lines.size()]);
        return lineArray;
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
