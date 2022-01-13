/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int moves = -1;
    private Node currentOG, currentT;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Order order = new Order();
        MinPQ<Node> pq1 = new MinPQ<Node>(order), pq2 = new MinPQ<>(order);

        Node ogNode = new Node(initial);
        pq1.insert(ogNode);

        Node tNode = new Node(initial.twin());
        pq2.insert(tNode);

        currentOG = ogNode;
        currentT = tNode;

        Node newOG, newT;

        // while is not goal
        while (!ogNode.board.isGoal() && !tNode.board.isGoal()) {

            // delete min priority node
            newOG = pq1.delMin();
            newT = pq2.delMin();
            moves++;

            ogNode = newOG;
            currentOG = ogNode;

            tNode = newT;
            currentT = tNode;

            boolean co = false;

            // insert neighbors
            for (Board each : ogNode.board.neighbors()) {
                if (moves > 0) if (!co) if (each.equals(ogNode.last.board)) {
                    co = true;
                    continue;
                }

                Node newNode;
                if (moves >= 0) newNode = new Node(each, ogNode);
                else newNode = new Node(each);
                pq1.insert(newNode);
            }

            co = false;

            for (Board each : tNode.board.neighbors()) {
                if (moves > 0) if (!co) if (each.equals(tNode.last.board)) {
                    co = true;
                    continue;
                }

                Node newNode;
                if (moves >= 0) newNode = new Node(each, tNode);
                else newNode = new Node(each);
                pq2.insert(newNode);
            }
        }
    }

    // private node class
    private static class Node {
        private Board board;
        private int mv;
        private int moves;
        private Node last = null;

        private Node(Board input, Node lastBoard) {
            board = input;
            last = lastBoard;
            moves = lastBoard.moves + 1;
            mv = board.manhattan() + moves;
            // hv = board.hamming() + moves;
        }

        private Node(Board input) {
            board = input;
            moves = 0;
            mv = board.manhattan() + moves;
            // hv = board.hamming() + moves;
        }
    }

    private class Order implements Comparator<Node> {

        // public int compare(Node n1, Node n2) {
        //     if (n1.mv < n2.mv) return -1;
        //     if (n1.mv > n2.mv) return 1;
        //     return Integer.compare(n1.hv, n2.hv);
        // }

        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.mv, n2.mv);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (currentOG.board.isGoal()) return true;
        if (currentT.board.isGoal()) return false;
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return currentOG.moves;
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        // from isgoal node
        Stack<Board> solList = new Stack<>();

        // add nodes to stack
        solList.push(currentOG.board);

        if (currentOG.last == null) return solList;

        Node step = currentOG.last;
        while (step.last != null) {
            solList.push(step.board);
            step = step.last;
        }
        solList.push(step.board);
        return solList;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
