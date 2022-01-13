/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PointSET {
    private SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.size() == 0;
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!pointSet.contains(p)) pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        if (pointSet == null || pointSet.size() < 1) throw new NoSuchElementException();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> pointsIn = new ArrayList<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) pointsIn.add(point);
        }
        return pointsIn;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointSet.size() == 0) return null;
        Point2D neighbor, nearest = null;
        boolean firstRun = true;
        for (Point2D point : pointSet) {
            if (firstRun) {
                nearest = point;
                firstRun = false;
            }
            neighbor = point;
            if (p.distanceSquaredTo(neighbor) < p.distanceSquaredTo(nearest))
                nearest = neighbor;
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET set = new PointSET();
        Point2D point = new Point2D(0.3, 0.7);
        set.insert(point);
        Point2D pointd = new Point2D(0.3, 0.7);
        set.insert(pointd);
        for (int i = 1; i < 6; i++) {
            double num = 1.5 / i;
            Point2D newP = new Point2D(num, num / 2);
            set.insert(newP);
        }
        StdOut.println("What is the size of the tree? Size: " + set.size());
        StdOut.println("Does it contain the point .3, .7? " + set.contains(point));

        StdOut.println("Let's check points in the rectangle 0, 0, .5, .5:");
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
        for (Point2D each : set.range(rect)) {
            StdOut.println(each.toString());
        }

        Point2D pointn = new Point2D(0.7, 0.3);
        set.insert(pointd);
        StdOut.println(
                "What is the nearest to the point .7, .3? Point: " + set.nearest(pointn));

        StdOut.println("Finally, let's draw:");
        set.draw();

    }
}
