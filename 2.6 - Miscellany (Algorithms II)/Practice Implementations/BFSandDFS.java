/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class BFSandDFS {
    private Graph g;
    private boolean[] marked;

    public BFSandDFS(int v) {
        g = new Graph(v);
    }

    public void BFS(int s) {
        boolean[] marked = new boolean[g.V()];
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        marked[s] = true;

        while (!queue.isEmpty()) {
            int p = queue.dequeue();

            System.out.print(p + " -> ");

            if (!g.adj[p].isEmpty()) {
                for (int each : g.adj[p].toArray()) {
                    if (!marked[each]) {
                        queue.enqueue(each);
                        marked[each] = true;
                    }
                }
            }
        }
        System.out.print("End\n");
    }

    public void DFS(int s) {
        marked = new boolean[g.V()];
        marked[s] = true;
        System.out.print(s + " -> ");
        for (int each : g.adj[s].toArray()) {
            if (!marked[each]) dfs(each);
        }
        System.out.print("End\n");
    }

    private void dfs(int s) {
        marked[s] = true;
        System.out.print(s + " -> ");
        for (int each : g.adj[s].toArray()) if (!marked[each]) dfs(each);
    }

    public void topSort() {
        marked = new boolean[g.V()];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < g.V(); i++) topsort(i, stack);
        while (!stack.isEmpty()) System.out.print(stack.pop() + " -> ");
        System.out.print("End\n");
    }

    private void topsort(int v, Stack<Integer> stack) {
        if (marked[v]) return;
        marked[v] = true;
        for (int each : g.adj[v].toArray()) topsort(each, stack);
        stack.push(v);
    }

    private class Graph {
        private int vert;
        private DLL[] adj;

        public Graph(int v) {
            vert = v;
            adj = new DLL[v];
            for (int i = 0; i < v; i++) adj[i] = new DLL();
        }

        public void addE(int v, int w) {
            adj[v].putFirst(w);
        }

        public int V() {
            return vert;
        }
    }

    public static void main(String[] args) {
        BFSandDFS g = new BFSandDFS(6);

        // Test BFS and DFS
        // g.g.addE(0, 1);
        // g.g.addE(0, 2);
        // g.g.addE(1, 2);
        // g.g.addE(2, 0);
        // g.g.addE(2, 3);
        // g.g.addE(3, 3);

        // g.BFS(2);
        // g.DFS(2);

        // Test topological sort
        g.g.addE(5, 2);
        g.g.addE(5, 0);
        g.g.addE(4, 0);
        g.g.addE(4, 1);
        g.g.addE(2, 3);
        g.g.addE(3, 1);

        g.topSort();
    }
}
