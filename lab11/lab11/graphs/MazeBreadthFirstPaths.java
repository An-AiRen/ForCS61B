package lab11.graphs;
import edu.princeton.cs.algs4.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        int c;

        marked[s] = true;
        announce();

        while (!targetFound) {
            c = q.dequeue();
            if (c == t) {
                targetFound = true;
                continue;
            }

            for (int n : maze.adj(c)) {
                if (!marked[n]) {
                    edgeTo[n] = c;
                    distTo[n] = distTo[c] + 1;
                    marked[n] = true;
                    announce();
                    q.enqueue(n);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

