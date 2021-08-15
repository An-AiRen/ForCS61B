package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int N;
    private int[][] tile;

    public Board(int[][] tiles) {
        N = tiles.length;
        tile = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tile[i][j] = tiles[i][j];
            }
        }
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new IndexOutOfBoundsException("0 <= i, j < N !");
        }
        return tile[i][j];
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int size = size();

        int zeroX = -1;
        int zeroY = -1;

        // Find the pos of blank.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) == 0) {
                    zeroX = i;
                    zeroY = j;
                }
            }
        }

        // Initialize the neighbor.
        int[][] n = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                n[i][j] = tileAt(i, j);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.abs(i - zeroX) + Math.abs(j - zeroY) == 1) {
                    //Move
                    n[zeroX][zeroY] = tileAt(i, j);
                    n[i][j] = 0;

                    Board newNeighbor = new Board(n);
                    neighbors.enqueue(newNeighbor);

                    //Reset
                    n[i][j] = n[zeroX][zeroY];
                    n[zeroX][zeroY] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        //the last brick is sure to wrong.
        int hamming = -1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != N * i + j + 1) {
                    hamming += 1;
                }
            }
        }
        return hamming;
    }

    public int manhattan() {
        int man = 0;
        int goalX; //the position it should be at
        int goalY;
        int t;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                t = tileAt(i, j);
                if (t == 0) {
                    continue;
                }
                goalX = (t - 1) / N;
                goalY = (t - 1) % N;
                man += Math.abs(i - goalX) + Math.abs(j - goalY);
            }
        }
        return man;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board e = (Board) y;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N;j++) {
                if (tileAt(i, j) != e.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
