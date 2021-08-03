package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

import java.util.Random;

public class PercolationStats {
    private Percolation grid;
    private double[] pThreshold;
    private int pindex;

    /** perform T independently */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) {throw new IllegalArgumentException("N must greater than 0!");}
        if (T <= 0) {throw new IllegalArgumentException("the experiment times must greater than 0!");}

        pThreshold = new double[T];
        pindex = 0;

        for (int i = 0; i < T; i += 1) {
            grid = pf.make(N);
            StdRandom.setSeed(i);

            while (!grid.percolates()) {
                int x = StdRandom.uniform(0, N);
                int y = StdRandom.uniform(0, N);
                if (!grid.isOpen(x, y))
                    grid.open(x, y);
            }

            pThreshold[pindex] = (double)grid.numberOfOpenSites() / (N * N);
            pindex += 1;
        }
    }

    /** sample mean of percolation threshold */
    public double mean() {
        double sum = 0;
        for (double sample : pThreshold) {
            sum += sample;
        }
        return sum /(double)pindex;
    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        double sumSquare = 0;
        double m = mean();
        for (double sample : pThreshold) {
            sumSquare += (sample - m) * (sample - m);
        }
        return sumSquare / (pindex - 1);
    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(pindex);
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(pindex);
    }

}
