/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.IndexMinPQ;

public class Dijkstra {
    private int v;
    private double[] distTo;
    private IndexMinPQ<Double> pq;
    private EdgeWeightedDigraph g;
    private DirectedEdge[] pathTo;

    public Dijkstra(int vert, EdgeWeightedDigraph graph) {
        v = vert;
        g = graph;

        distTo = new double[v];

        for (int i = 0; i < v; i++) distTo[i] = Double.POSITIVE_INFINITY;

        pathTo = new DirectedEdge[v];
    }

    public void findSP(int source) {
        distTo[source] = 0;
        pathTo[source] = new DirectedEdge(0, 0, 0.0);

        pq = new IndexMinPQ<>(v);
        pq.insert(source, 0.0);

        while (!pq.isEmpty()) {
            int vI = pq.delMin();
            for (DirectedEdge each : g.adj(vI)) {
                checkAndRelax(each);
            }
        }

        System.out.println("Vertex \t\t Shortest Distance");
        for (int i = 0; i < v; i++) System.out.println(i + "\t\t\t" + distTo[i]);

        System.out.println("Vertex \t\t Path to vertex from");
        for (int i = 0; i < v; i++) System.out.println(i + "\t\t\t" + pathTo[i]);
    }

    private void checkAndRelax(DirectedEdge edge) {
        int vI = edge.from(), wI = edge.to();
        if (distTo[wI] > distTo[vI] + edge.weight()) {
            distTo[wI] = distTo[vI] + edge.weight();
            pathTo[wI] = edge;
            if (pq.contains(wI)) pq.decreaseKey(wI, distTo[wI]);
            else pq.insert(wI, distTo[wI]);
        }
    }

    public static void main(String[] args) {
        int v = 5;

        EdgeWeightedDigraph wDAG;
        wDAG = new EdgeWeightedDigraph(v);

        wDAG.addEdge(new DirectedEdge(0, 1, 9));
        wDAG.addEdge(new DirectedEdge(0, 2, 6));
        wDAG.addEdge(new DirectedEdge(0, 3, 5));
        wDAG.addEdge(new DirectedEdge(0, 4, 3));
        wDAG.addEdge(new DirectedEdge(2, 1, 2));
        wDAG.addEdge(new DirectedEdge(2, 3, 4));

        Dijkstra d = new Dijkstra(v, wDAG);

        d.findSP(0);
    }
}
