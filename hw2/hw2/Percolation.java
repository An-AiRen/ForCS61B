package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gLen;
    private int topS;
    private int bottomS;
    private int[][] sites;
    private int openSites;
    private WeightedQuickUnionUF possiblePath;

    /** create N-by-N grid, with all sites initial */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        gLen = N;
        topS = N * N;
        bottomS = N * N + 1;
        openSites = 0;

        /** Using WQUUF to store the paths, the last two items serve as sentinel */
        possiblePath = new WeightedQuickUnionUF(N * N + 2);

        /** Performance requirements:N^2 */
        sites = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                sites[i][j] = 0; //0 represent blocked, 1 open
            }
        }
    }

    /** Switch 2D to 1D in sequence*/
    private int xyTo1D(int x, int y) {
        return x * gLen + y;
    }

    /** Open the site(row, col) if it is not open */
    public void open(int row, int col) {
        /** Already opened, no need to reopen! */
        if (sites[row][col] != 0) {
            return;
        }
        sites[row][col] = 1;
        merge(row, col);
        openSites += 1;
    }

    /** is the site(row, col) open? */
    public boolean isOpen(int row, int col) {
        return sites[row][col] == 1;
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        return possiblePath.connected(xyTo1D(row, col), topS);
    }

    /** number of open sites */
    public int numberOfOpenSites() {
        return openSites;
    }

    /** does the system percolate? */
    public boolean percolates() {
        return possiblePath.connected(topS, bottomS);
    }

    /** Connect two sites according to rules. UGLY!*/
    private void merge(int row, int col) {
        int posIn1D = xyTo1D(row, col);
        /** The first line */
        if (row == 0) {
            possiblePath.union(topS ,posIn1D);
            /** Connect to the neighborhood */
            if (isOpen(row + 1, col))
                possiblePath.union(xyTo1D(row + 1, col), posIn1D);
            if (col != 0 && isOpen(row, col - 1))
                possiblePath.union(xyTo1D(row, col - 1), posIn1D);
            if (col != gLen - 1 && isOpen(row, col + 1))
                possiblePath.union(xyTo1D(row, col + 1), posIn1D);
        } else if (row == gLen - 1) {
            possiblePath.union(bottomS, posIn1D);

            if (isOpen(row - 1, col))
                possiblePath.union(xyTo1D(row - 1, col), posIn1D);
            if (col != 0 && isOpen(row, col - 1))
                possiblePath.union(xyTo1D(row, col - 1), posIn1D);
            if (col != gLen - 1 && isOpen(row, col + 1))
                possiblePath.union(xyTo1D(row, col + 1), posIn1D);
        } else if (col == 0 || col == gLen - 1) {
            if (isOpen(row - 1, col))
                possiblePath.union(xyTo1D(row - 1, col), posIn1D);
            if (isOpen(row + 1, col))
                possiblePath.union(xyTo1D(row + 1, col), posIn1D);
            if (col == 0 && isOpen(row, col + 1))
                possiblePath.union(xyTo1D(row, col + 1), posIn1D);
            if (col == gLen - 1 && isOpen(row, col - 1))
                possiblePath.union(xyTo1D(row, col - 1), posIn1D);
        } else {
            if (isOpen(row - 1, col))
                possiblePath.union(xyTo1D(row - 1, col), posIn1D);
            if (isOpen(row + 1, col))
                possiblePath.union(xyTo1D(row + 1, col), posIn1D);
            if (isOpen(row, col - 1))
                possiblePath.union(xyTo1D(row, col - 1), posIn1D);
            if (isOpen(row, col + 1))
                possiblePath.union(xyTo1D(row, col + 1), posIn1D);
        }
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(4);

        p.open(2, 3);
        System.out.println(p.isOpen(2, 3));
        p.open(1, 3);
        p.open(1, 2);
        p.open(0, 2);
        p.open(3, 3);
        System.out.println(p.percolates());
    }
}
