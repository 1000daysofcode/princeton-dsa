/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] means;
    private final int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Arguments must be positive integers above 0");
        means = new double[trials];
        t = trials;
        for (int i = 0; i < trials; i++) {
            Percolation grid = new Percolation(n);
            while (!grid.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                if (!grid.isOpen(x, y)) grid.open(x, y);
            }
            int openSites = grid.numberOfOpenSites();
            boolean percs = grid.percolates();
            double totalSites = Math.pow(n, 2) * 1.0;
            if (percs) means[i] = openSites / totalSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(means);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (t == 1) return Double.NaN;
        return StdStats.stddev(means);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]), t = Integer.parseInt(args[1]);

        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException("Arguments must be positive integers above 0");

        PercolationStats trial = new PercolationStats(n, t);
        StdOut.println("mean                    = " + trial.mean());
        StdOut.println("stddev                  = " + trial.stddev());
        StdOut.println(
                "95% confidence interval = [" + trial.confidenceLo() + ", "
                        + trial.confidenceHi() + "]");
    }
}
