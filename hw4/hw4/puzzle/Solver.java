package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.HashSet;

public class Solver {
    private MinPQ<SearchNode> pq;
    private SearchNode goal;

    private class SearchNode {
        private WorldState item;
        private SearchNode prev;
        private int movefstart;

        private SearchNode(WorldState i, SearchNode p, int m) {
            item = i;
            prev = p;
            movefstart = m;
        }
    }

    public class comparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            //no duplication!
            if (o1.item.equals(o2.item)) {
                return 0;
            }

            int po1 = o1.movefstart + o1.item.estimatedDistanceToGoal();
            int po2 = o2.movefstart + o2.item.estimatedDistanceToGoal();
            return po1 - po2;
        }
    }
    comparator cp = new comparator();

    public Solver(WorldState initial) {
        pq = new MinPQ<SearchNode>(cp);
        findSolution(initial);
    }

    public int moves() {
        int moves = 0;
        SearchNode s = goal;
        while (s.prev != null) {
            moves += 1;
            s = s.prev;
        }
        return moves;
    }

    public Iterable<WorldState> solution() {
        Stack<WorldState> path = new Stack<>();
        SearchNode s = goal;
        while (s != null) {
            path.push(s.item);
            s = s.prev;
        }
        return path;
    }

    private void findSolution(WorldState initial) {
        SearchNode current = new SearchNode(initial, null, 0);
        while (!current.item.isGoal()) {
            for (WorldState neighbor : current.item.neighbors()) {
                if (current.prev != null && neighbor.equals(current.prev.item)) {
                    continue;
                }
                SearchNode n = new SearchNode(neighbor, current, current.movefstart + 1);
                pq.insert(n);
            }
            current = pq.delMin();
        }
        goal = current;
    }
}
