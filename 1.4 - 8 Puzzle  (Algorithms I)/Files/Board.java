/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final short[][] inputBoard;
    private final int k, n;

    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        n = tiles.length;
        k = n * n;

        inputBoard = new short[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inputBoard[i][j] = (short) tiles[i][j];
            }
        }
        makeGoal();
        // board = new MinPQ<Board>(k);
    }

    private int[][] makeGoal() {
        short[] temp = flattenGrid(inputBoard);
        Arrays.sort(temp);
        temp[temp.length - 1] = 0;
        int nk = 0;
        int[][] goal = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                goal[i][j] = temp[nk++];
            }
        }
        return goal;
    }

    private short[] flattenGrid(short[][] grid) {
        short[] flatArray = new short[grid.length * grid.length];
        int m = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (inputBoard[i][j] == 0) {
                    flatArray[m++] = (short) (k + 1);
                    continue;
                }
                flatArray[m++] = inputBoard[i][j];
            }
        }
        return flatArray;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", inputBoard[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int[][] goal = makeGoal();
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (inputBoard[i][j] == 0) continue;
                if (inputBoard[i][j] != goal[i][j]) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distances = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances += calcMoves(i, j);
            }
        }
        if (distances < 0) distances = 0;
        return distances;
    }

    private int calcMoves(int i, int j) {
        int correctPos = (inputBoard[i][j]);
        if (correctPos == 0) return 0; // ignore 0

        int currentPos = (n * i) + j + 1;
        if (correctPos == currentPos) return 0; // if correct pos

        int corJ = (correctPos % n) - 1, corI = (correctPos / n);

        // if last index
        if (correctPos % n == 0) {
            corI--;
            corJ = n - 1;
        }

        int iDif, jDif;

        if (corI > i) iDif = corI - i;
        else if (corI < i) iDif = i - corI;
        else iDif = 0;

        if (corJ > j) jDif = corJ - j;
        else if (corJ < j) jDif = j - corJ;
        else jDif = 0;

        return iDif + jDif;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int[][] goal = makeGoal();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (inputBoard[i][j] != goal[i][j]) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.k != that.k) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.inputBoard[i][j] != that.inputBoard[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zI = 0, zJ = 0;
        // find 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (inputBoard[i][j] != 0) continue;
                zI = i;
                zJ = j;
            }
        }
        int tI = zI - 1, bI = zI + 1, lJ = zJ - 1, rJ = zJ + 1;

        Stack<Board> nb = new Stack<Board>();
        if (tI >= 0) {
            int[][] nb1 = makeNeighbor(tI, zJ, zI, zJ);
            nb.push(new Board(nb1));
        }
        if (bI < n) {
            int[][] nb2 = makeNeighbor(bI, zJ, zI, zJ);
            nb.push(new Board(nb2));
        }
        if (lJ >= 0) {
            int[][] nb3 = makeNeighbor(zI, lJ, zI, zJ);
            nb.push(new Board(nb3));
        }
        if (rJ < n) {
            int[][] nb4 = makeNeighbor(zI, rJ, zI, zJ);
            nb.push(new Board(nb4));
        }
        return nb;
    }

    private int[][] makeNeighbor(int nI, int nJ, int oI, int oJ) {
        int[][] neighborArray = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neighborArray[i][j] = inputBoard[i][j];
            }
        }
        int temp = neighborArray[nI][nJ];
        neighborArray[nI][nJ] = 0;
        neighborArray[oI][oJ] = temp;
        return neighborArray;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinArray = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinArray[i][j] = inputBoard[i][j];
            }
        }
        int i1 = 0, j1 = 0, i2 = 0, j2 = 1;

        if (isNull(i1, j1)) {
            j1 = 1;
            i2 = 1;
        }
        if (isNull(i2, j2)) {
            i2 = 1;
            j2 = 0;
        }

        twinArray[i1][j1] = inputBoard[i2][j2];
        twinArray[i2][j2] = inputBoard[i1][j1];

        Board twin = new Board(twinArray);
        return twin;
    }

    private boolean isNull(int i, int j) {
        return inputBoard[i][j] == 0;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] test = { { 1, 0 }, { 2, 3 } };
        // Board testb = new Board(test);
        // StdOut.println(testb.toString());
        // StdOut.println("Manhattan: " + testb.manhattan());
        //
        // int[][] testA1 = { { 1, 2, 4, 3 }, { 5, 6, 7, 8 }, { 9, 10, 12, 11 }, { 13, 14, 0, 15 } };

        int[][] testA1 = { { 1, 2, 3 }, { 7, 4, 0 }, { 5, 8, 6 } };
        int[][] testA2 = { { 1, 2, 3 }, { 7, 4, 6 }, { 0, 5, 8 } };
        Board testB1 = new Board(testA1);
        Board testB2 = new Board(testA2);

        StdOut.println("How wide is board 1? " + testB1.dimension());
        StdOut.println("Let's look at board 1:\n" + testB1.toString());
        StdOut.println("Hamming: " + testB1.hamming() + " | Manhattan: " + testB1.manhattan());
        StdOut.println("Let's look at a twin:\n" + testB1.twin().toString());
        StdOut.println("Is this the goal board? " + testB1.isGoal() + "\n");

        StdOut.println("Let's print some neighbors:");
        for (Board each : testB1.neighbors()) {
            StdOut.println(each.toString());
        }

        StdOut.println("How wide is board 2? " + testB2.dimension());
        StdOut.println("Let's look at board 2:\n" + testB2.toString());
        StdOut.println("Hamming: " + testB2.hamming() + " | Manhattan: " + testB2.manhattan());
        StdOut.println("Let's look at a twin:\n" + testB2.twin().toString());
        StdOut.println("Is this the goal board? " + testB2.isGoal() + "\n");

        StdOut.println("Are the boards equal? " + testB1.equals(testB2));

        StdOut.println("Let's print some neighbors:");
        for (Board each : testB2.neighbors()) {
            StdOut.println(each.toString());
        }
    }
}
