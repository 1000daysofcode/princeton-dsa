/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    // Dictionary & words
    private final BoggleDictTST<Integer> d;
    private HashSet<String> w;
    // DS for DFS (including board, adj matrix & marked matrix)
    private BoggleBoard b;
    private AdjNode[][] adj;
    private boolean[][] marked;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        d = new BoggleDictTST<>();
        for (int i = 0; i < dictionary.length; i++) {
            d.put(dictionary[i], scoreOf(dictionary[i]));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rowEnd = board.rows(), colEnd = board.cols();
        marked = new boolean[rowEnd][colEnd];
        w = new HashSet<>();
        b = board;

        // Create array of adjacency nodes
        adj = new AdjNode[rowEnd][colEnd];
        for (int i = 0; i < rowEnd; i++) {
            for (int j = 0; j < colEnd; j++) {
                // Iterate around selected index
                adj[i][j] = new AdjNode(i, j, rowEnd, colEnd);
            }
        }

        String word = "";

        // Run depth-first search for all indices in board
        for (int i = 0; i < rowEnd; i++) {
            for (int j = 0; j < colEnd; j++) {
                runDFS(i, j, word);
            }
        }
        return w;
    }

    // run recursive dfs for a letter
    private void runDFS(int i, int j, String word) {
        // run dfs for a given index in a graph

        // add current letter to word
        word += b.getLetter(i, j);

        // deal with 'QU' case
        if (word.charAt(word.length() - 1) == ('Q')) word += 'U';

        // only add words 3+ characters long
        if (word.length() >= 3) if (d.contains(word)) w.add(word);

        // mark index to avoid returning to this neighbor whilst recurring
        marked[i][j] = true;
        for (int[] nb : adj[i][j].nb()) {
            // check for prefix before recurring to prevent unnecessary recursion
            if (!marked[nb[0]][nb[1]] && d.hasPrefix(word)) {
                runDFS(nb[0], nb[1], word);
            }
        }

        // unmark index so BFS can back up and find more words
        marked[i][j] = false;
    }

    // adjacency node data type; stores list of adjacent i,j index pairs
    private class AdjNode {
        private final int nodeI, nodeJ;
        private final ArrayList<int[]> nb;

        // Node constructor
        public AdjNode(int i, int j, int r, int c) {
            nodeI = i;
            nodeJ = j;

            nb = new ArrayList<>();

            // loop through surrounding indices and add
            for (int inI = i - 1; inI <= i + 1; inI++) {
                if (inI >= 0 && inI < r) {
                    for (int inJ = j - 1; inJ <= j + 1; inJ++) {
                        if (inJ >= 0 && inJ < c) {
                            int[] temp = { inI, inJ };
                            if (inI == i && inJ == j) continue;
                            nb.add(temp);
                        }
                    }
                }
            }
        }

        // call node's i index
        public int i() {
            return nodeI;
        }

        // call node's j index
        public int j() {
            return nodeJ;
        }

        // iterable of neighboring indices
        public Iterable<int[]> nb() {
            return nb;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        int len = word.length();
        // return corresponding scores
        if (len >= 8) return 11;
        if (len == 7) return 5;
        if (len == 6) return 3;
        if (len == 5) return 2;
        if (len == 3 || len == 4) return 1;
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
