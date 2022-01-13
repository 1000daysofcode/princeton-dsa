/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid, perx;
    private int n, openSites = 0, qfli;
    private WeightedQuickUnionUF qf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        grid = new int[n][n];
        perx = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            perx[0][i] = 1;
        }
        this.n = n;
        this.qf = new WeightedQuickUnionUF((n * n) + 2);
        this.qfli = n * n + 1;
    }

    // converts 2D grid to 1d union-find object
    private int xyTo1D(int x, int y) {
        int xa = x - 1, ya = y - 1;
        int flattened = (n * xa) - 1, remaining = ya + 1;
        if (xa == 0 && ya == 0) return 1;
        else return flattened + remaining + 1;
    }

    // validate inputs and throw exception if not
    private boolean valid(int row, int col) {
        if (row < 1 || row > n) throw new IllegalArgumentException("Row out of bounds");
        if (col < 1 || col > n) throw new IllegalArgumentException("Col out of bounds");
        return true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        boolean full;
        if (valid(row, col)) {
            int current = xyTo1D(row, col), top = xyTo1D(row - 1, col), left = xyTo1D(row, col - 1),
                    right = xyTo1D(row, col + 1), bottom = xyTo1D(row + 1, col);
            if (!isOpen(row, col)) {
                grid[row - 1][col - 1] = 1;
                openSites++;
            }

            full = isFull(row, col);
            if (row - 1 == 0) qf.union(0, current);
            if (full) perx[row][col] = 1;
            if (row > 1 && top > 0 && grid[row - 2][col - 1] == 1) {
                qf.union(current, top);
                if (perx[row][col] == 1) perx[row - 1][col] = 1;
            }
            if (col > 1 && left > 0 && grid[row - 1][col - 2] == 1) {
                qf.union(current, left);
                if (perx[row][col] == 1) perx[row][col - 1] = 1;
            }
            if (col < n && right < qfli && grid[row - 1][col] == 1) {
                qf.union(current, right);
                if (perx[row][col] == 1) perx[row][col + 1] = 1;
            }
            if (row == n) qf.union(qfli, current);
            if (row < n && bottom < qfli && grid[row][col - 1] == 1) {
                qf.union(current, bottom);
                if (perx[row][col] == 1) perx[row + 1][col] = 1;
            }

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int r = row - 1, c = col - 1;
        return grid[r][c] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (valid(row, col)) {
            if (qf.find(xyTo1D(row, col)) == qf.find(0)) return true;
            return false;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (qf.find(0) == qf.find(qfli)) return true;
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        Percolation test = new Percolation(n);
        test.n = n;

        int j = 1;
        boolean jr = false;
        for (int i = 1; i <= n; i++) {
            test.open(i, j);
            if (j > 3) jr = true;
            if (!jr) j++;
            else j--;
            test.open(i, j);
        }

        int num = test.numberOfOpenSites();
        boolean perc = test.percolates();

        System.out.println("How many sites are open? Num: " + num);
        System.out.println("Does the object percolate? A: " + perc);
    }
}
