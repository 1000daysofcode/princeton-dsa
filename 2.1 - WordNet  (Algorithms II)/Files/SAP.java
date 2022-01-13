/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;
    private int ancestor = -1;
    private int shortestPath = -1;
    private BreadthFirstDirectedPaths bfsV, bfsW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        makeBFS(v, w);
        return shortestPath;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        makeBFS(v, w);
        if (shortestPath == -1) return -1;
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        checkNull(v);
        checkNull(w);

        makeBFS(v, w);
        return shortestPath;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        checkNull(v);
        checkNull(w);

        makeBFS(v, w);
        return ancestor;
    }

    private void checkNull(Iterable<Integer> vORw) {
        for (Integer item : vORw) {
            if (item == null) throw new IllegalArgumentException();
        }
    }

    private void makeBFS(Iterable<Integer> v, Iterable<Integer> w) {
        bfsV = new BreadthFirstDirectedPaths(G, v);
        bfsW = new BreadthFirstDirectedPaths(G, w);

        int currentShortest = Integer.MAX_VALUE;

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int distV = bfsV.distTo(i), distW = bfsW.distTo(i),
                        sumDist = distV + distW;
                if (sumDist < currentShortest) {
                    currentShortest = sumDist;
                    ancestor = i;
                }
            }
        }
        if (currentShortest == Integer.MAX_VALUE) currentShortest = -1;
        shortestPath = currentShortest;
    }

    private void makeBFS(int v, int w) {
        bfsV = new BreadthFirstDirectedPaths(G, v);
        bfsW = new BreadthFirstDirectedPaths(G, w);

        int currentShortest = Integer.MAX_VALUE;

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int distV = bfsV.distTo(i), distW = bfsW.distTo(i),
                        sumDist = distV + distW;
                if (sumDist < currentShortest) {
                    currentShortest = sumDist;
                    ancestor = i;
                }
            }
        }
        if (currentShortest == Integer.MAX_VALUE) currentShortest = -1;

        shortestPath = currentShortest;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
