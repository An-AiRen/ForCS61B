//import jdk.javadoc.internal.doclets.formats.html.Navigation;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    static Map<Long, Double> disToStart = new HashMap<>(); //store the distance to start from each.
    static Map<Long, Long> edgeTo = new HashMap<>(); //store the previous node.
    static GraphDB graph;

    static long start;
    static long end;

    /** Comparator for MinPQ. */
    private static class astar<Long> implements Comparator<Long> {
        @Override
        public int compare(Long o1, Long o2) {
            double disTo1 = disToStart.get(o1);
            double disTo2 = disToStart.get(o2);

            double c = (disTo1 + graph.distance((long)o1, end)) -
                    (disTo2 + graph.distance((long)o2, end));
            if (c < 0) {
                return -1;
            } else if (c > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        //Get ID of start and end position.
        start = g.closest(stlon, stlat);
        end = g.closest(destlon, destlat);
        graph = g;
        disToStart.put(start, 0.0);

        astar<Long> comparator = new astar<>();
        PriorityQueue<Long> pq = new PriorityQueue<Long>(comparator);
        Stack<Long> shortestPath = new Stack<>();
        List<Long> ret = new ArrayList<>();
        Set<Long> hasReached = new HashSet<>();

        long current = start; //store which node we are at.
        pq.add(current);

        while (!pq.isEmpty() && current != end) {
            current = pq.remove();
            hasReached.add(current); //mark it.

            for (long neighbor : g.adjacent(current)) {
                //Avoid tracing back.
                if (edgeTo.containsKey(current) && edgeTo.get(current) == neighbor) {
                    continue;
                }
                //Avoid repeating
                if (hasReached.contains(neighbor)) {
                    continue;
                }

                //Calculate the distance from certain neighbor to start.
                double disTo = disToStart.get(current) + g.distance(neighbor, current);
                if (disToStart.containsKey(neighbor)) { //neighbor has been memorized.
                    double origin = disToStart.get(neighbor);
                    if (origin < disTo) { //no change will happen.
                        continue;
                    }
                }
                edgeTo.put(neighbor, current);
                disToStart.put(neighbor, disTo);
                pq.add(neighbor);
            }
        }

        // build path in stack
        while (current != start) {
            shortestPath.push(current);
            current = edgeTo.get(current);
        }
        shortestPath.push(start);
        // reverse it to the normal state.
        while (!shortestPath.isEmpty()) {
            ret.add(shortestPath.pop());
        }

        return ret;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> guideLines = new ArrayList<>();
        long start = route.get(0);
        long prevNode = start;
        NavigationDirection step = new NavigationDirection();
        String wayName = "";
        double prevBearing = 0.0;
        double bearing;

        for (long node : route) {
            //it's just the beginning
            if (node == start) {
                continue;
            }

            double currentDis = g.findEdgeWeight(prevNode, node);
            String currentWay = g.findWayName(prevNode, node);
            double currentBearing = g.bearing(prevNode, node);

            if (currentWay == "" || currentWay == null) { //deal with the unknown road.
                currentWay = NavigationDirection.UNKNOWN_ROAD;
            }

            //determine the path to begin with. nice english!
            if (prevNode == start) {
                wayName = currentWay;
                step.way = wayName;
                step.direction = NavigationDirection.START;
            } else { //deal with normal situation where prevNode is not start.
                if (!currentWay.equals(wayName)) { //represent a change in the way.
                    guideLines.add(step);
                    step = new NavigationDirection();

                    step.direction = getDirection(prevBearing, currentBearing); //prevBearing should be initialized?
                    step.way = currentWay;
                    wayName = currentWay; //almost forget it!
                }
            }
            step.distance += currentDis;
            prevNode = node;
            prevBearing = currentBearing;
        }
        guideLines.add(step); //almost forget to add the last Navigation!

        return guideLines;
    }

    /** Get direction from bearing angle. */
    private static int getDirection(double prev, double current) { //weird that every method should be static?
        int dir;
        double bearing = current - prev;

        //adjust bearing when its abs is larger than 180
        //it's quite important!
        //and to be honest I haven't understand it fully.
        if (bearing >= 180 || bearing <= -180) {
            bearing = bearing > 0 ? Math.abs(bearing) - 360 : 360 - Math.abs(bearing);
        }
        if (bearing <= 15 && bearing >= -15) {
            dir = NavigationDirection.STRAIGHT;
        } else if (bearing <= 30 && bearing >= -30) {
            dir = bearing < 0 ? NavigationDirection.SLIGHT_LEFT : NavigationDirection.SLIGHT_RIGHT;
        } else if (bearing <= 100 && bearing >= -100) {
            dir = bearing < 0 ? NavigationDirection.LEFT : NavigationDirection.RIGHT;
        } else {
            dir = bearing < 0 ? NavigationDirection.SHARP_LEFT : NavigationDirection.SHARP_RIGHT;
        }
        return dir;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
