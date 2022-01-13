/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int treeSize = 0;

    // construct an empty set of points
    public KdTree() {
        root = new Node();
    }

    private static class Node {
        // the point
        private Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        // the left/bottom subtree
        private Node lb = null;
        // the right/top subtree
        private Node rt = null;

        private Node(Point2D point, RectHV rectangle) {
            p = point;
            rect = rectangle;
        }

        private Node() {
            p = null;
            rect = null;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSize == 0;
    }

    // number of points in the set
    public int size() {
        return treeSize;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        // horizontal == false (first split is vertical)
        root = insertNode(root, root, p, false);
    }

    private Node insertNode(Node prev, Node cursor, Point2D p, boolean horizontal) {
        if (treeSize < 1) {
            RectHV r = new RectHV(0, 0, 1, 1);
            treeSize++;
            return new Node(p, r);
        }

        if (cursor == null) {
            RectHV r;
            int cmp = cmp(p, prev.p, horizontal);
            if (horizontal) {
                if (cmp < 0) {
                    r = new RectHV(prev.rect.xmin(), prev.rect.ymin(),
                                   prev.p.x(), prev.rect.ymax());
                }
                else {
                    r = new RectHV(prev.p.x(), prev.rect.ymin(),
                                   prev.rect.xmax(), prev.rect.ymax());
                }
            }
            // if vertical
            else {
                if (cmp < 0) {
                    r = new RectHV(prev.rect.xmin(), prev.rect.ymin(),
                                   prev.rect.xmax(), prev.p.y());
                }
                else {
                    r = new RectHV(prev.rect.xmin(), prev.p.y(),
                                   prev.rect.xmax(), prev.rect.ymax());
                }
            }

            treeSize++;
            return new Node(p, r);
        }

        // Switch horizontal -> vertical (or vis versa) for next branch
        if (horizontal) horizontal = false;
        else horizontal = true;

        int cmp = cmp(p, cursor.p, horizontal);

        // p.x() < cursor.p.x()
        if (cmp == 0) treeSize += 0;
        else if (cmp < 0) cursor.lb = insertNode(cursor, cursor.lb, p, horizontal);
            // p.x() > cursor.p.x()
        else cursor.rt = insertNode(cursor, cursor.rt, p, horizontal);
        // if (cmp == 0) treeSize++;

        return cursor;
    }

    private int cmp(Point2D argP, Point2D curP, boolean horizontal) {
        if (horizontal) {
            if (argP.x() < curP.x()) return -1;
            if (argP.x() > curP.x()) return 1;
            return Double.compare(argP.y(), curP.y());
        }
        else {
            if (argP.y() < curP.y()) return -1;
            if (argP.y() > curP.y()) return 1;
            return Double.compare(argP.x(), curP.x());
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node cursor = root;
        boolean horizontal = false;
        return contains(cursor, p, horizontal);
    }

    private boolean contains(Node cursor, Point2D p, boolean horizontal) {
        if (cursor == null || cursor.p == null) return false;
        if (horizontal) horizontal = false;
        else horizontal = true;

        int cmp = cmp(p, cursor.p, horizontal);
        if (cmp < 0) return contains(cursor.lb, p, horizontal);
        else if (cmp > 0) return contains(cursor.rt, p, horizontal);
        else return true;
    }

    // draw all points to standard draw
    public void draw() {
        boolean horizontal = false;
        drawAll(root, horizontal);
    }

    private void drawAll(Node curNode, boolean horizontal) {
        if (curNode == null) return;
        // First draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        curNode.p.draw();

        // Use RectHV dimensions to draw line
        StdDraw.setPenRadius();
        RectHV r = curNode.rect;

        // red
        if (horizontal) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(r.xmin(), curNode.p.y(), r.xmax(), curNode.p.y());
        }
        // blue
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(curNode.p.x(), r.ymin(), curNode.p.x(), r.ymax());
        }

        // Recur on each side
        if (horizontal) horizontal = false;
        else horizontal = true;
        drawAll(curNode.lb, horizontal);
        drawAll(curNode.rt, horizontal);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Node cursor = root;
        ArrayList<Point2D> points = new ArrayList<>();
        if (cursor == null || cursor.p == null) return points;
        if (rect.contains(cursor.p)) points.add(cursor.p);
        findRange(rect, cursor, points, false);
        return points;
    }

    private void findRange(RectHV rect, Node cursor, ArrayList<Point2D> points, boolean recurring) {
        // iterate/recur through
        Point2D point = cursor.p;
        if (recurring && rect.contains(point)) points.add(point);
        recurring = true;
        if (cursor.lb != null && rect.intersects(cursor.lb.rect))
            findRange(rect, cursor.lb, points, recurring);
        if (cursor.rt != null && rect.intersects(cursor.rt.rect))
            findRange(rect, cursor.rt, points, recurring);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null || root.p == null) return null;
        Point2D rootPoint = root.p;
        Node cursor = root;
        double distance = dToP(p, rootPoint);
        boolean recurring = false, horizontal = false;
        Point2D nearest = null;
        findNearest(recurring, horizontal, cursor, p, rootPoint, distance);
        return nearest;
    }

    private void findNearest(boolean recurring, boolean horizontal, Node cursor, Point2D p,
                             Point2D nearest, double oldDist) {
        // if (cursor.lb == null && cursor.rt == null) return nearest;
        double newDist = dToP(p, cursor.p);
        if (recurring && newDist < oldDist) {
            oldDist = newDist;
            nearest = cursor.p;
        }
        if (!recurring) recurring = true;

        if (horizontal) horizontal = false;
        else horizontal = true;

        double cmp = cmp(p, cursor.p, horizontal);

        if (cmp < 0) {
            if (cursor.lb != null && dToP(p, cursor.lb.rect) < oldDist)
                findNearest(recurring, horizontal, cursor.lb, p, nearest, oldDist);
            if (cursor.rt != null && dToP(p, cursor.rt.rect) < oldDist)
                findNearest(recurring, horizontal, cursor.rt, p, nearest, oldDist);
        }
        else {
            if (cursor.rt != null && dToP(p, cursor.rt.rect) < oldDist)
                findNearest(recurring, horizontal, cursor.rt, p, nearest, oldDist);
            if (cursor.lb != null && dToP(p, cursor.lb.rect) < oldDist)
                findNearest(recurring, horizontal, cursor.lb, p, nearest, oldDist);
        }
    }

    private double dToP(Point2D origP, Point2D thisP) {
        return origP.distanceSquaredTo(thisP);
    }

    private double dToP(Point2D origP, RectHV thisRect) {
        return thisRect.distanceSquaredTo(origP);
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        Point2D point = new Point2D(0.3, 0.7);
        tree.insert(point);
        Point2D pointd = new Point2D(0.3, 0.7);
        tree.insert(pointd);

        for (int i = 2; i < 7; i++) {
            double num = 1.5 / i;
            Point2D newP = new Point2D(num, num / 2);
            tree.insert(newP);
        }
        StdOut.println("What is the size of the tree? Size: " + tree.size());

        StdOut.println("Let's check points in the rectangle 0, 0, .5, .5:");
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
        for (Point2D each : tree.range(rect)) {
            StdOut.println(each.toString());
        }

        Point2D pointn = new Point2D(0.8, 0.8);
        StdOut.println("Does it contain the point 0.8, 0.8? " + tree.contains(pointn));
        tree.insert(pointn);
        StdOut.println("Does it contain the point 0.8, 0.8? " + tree.contains(pointn));

        Point2D pointnn = new Point2D(0.7, 0.3);
        StdOut.println(
                "What is the nearest to the point .7, .3? Point: " + tree.nearest(pointnn));

        StdOut.println("Finally, let's draw:");
        tree.draw();
    }
}
