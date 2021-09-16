import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    //store every vertex.
    private Map<Long, Node> vertex = new HashMap<>();

    static class Node {
        ArrayList<Long> adjV;
        ArrayList<Edge> adjE;
        long id;
        double lat;
        double lon;
        String name;

        Node(long i, double lo, double la) {
            adjV = new ArrayList<Long>();
            adjE = new ArrayList<Edge>();
            id = i;
            lat = la;
            lon = lo;
            name = null;
        }
    }

    static class Edge {
        long from;
        long to;
        double distance;
        String name;

        Edge(long f, long t, double d) {
            from = f;
            to = t;
            distance = d;
            name = null;
        }
    }

    /** Add single node into Graph. */
    public void addNode(long id, double lon, double lat) {
        Node node = new Node(id, lon, lat);
        vertex.put(id, node);
    }

    /** Add name for location. */
    public void addName(String name, long id) {
        vertex.get(id).name = cleanString(name);
    }

    /** Add edges for nodes. */
    public void addEdge(List<Edge> connection, String name) {
        for (Edge e : connection) {
            e.name = name;
            //connect them
            Node from = vertex.get(e.from);
            Node to = vertex.get(e.to);
            from.adjE.add(e);
            to.adjE.add(e);

            from.adjV.add(to.id);
            to.adjV.add(from.id);
        }
    }

    /** Find the name of way if two nodes are connected. */
    public String findWayName(Long n1, Long n2) {
        List<Edge> allEdge = vertex.get(n1).adjE;
        for (Edge e : allEdge) {
            if (e.from == n2 || e.to == n2) {
                return e.name;
            }
        }
        return null;
    }

    /** Return weight of the edge which connect n1 and n2. */
    public double findEdgeWeight(Long n1, Long n2) {
        if (!vertex.containsKey(n1)) {
            throw new IllegalArgumentException(n1 + "is not in the graph!");
        }
        if (!vertex.containsKey(n2)) {
            throw new IllegalArgumentException(n1 + " is not in the graph!");
        }

        List<Edge> allEdge = vertex.get(n1).adjE;
        for (Edge e : allEdge) {
            if (e.to == n2 || e.from == n2) {
                return e.distance;
            }
        }
        return 0.0;
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() { //fail
        Iterator<Map.Entry<Long, Node>> it = vertex.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, Node> entry = it.next();
            long k = entry.getKey();
            if (vertex.get(k).adjV.isEmpty()) {
                it.remove();
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
       return vertex.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return vertex.get(v).adjV;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double closestDis = Double.MAX_VALUE;
        long closestID = -1;

        for (long id : vertices()) {
            double distance = distance(lon(id), lat(id), lon, lat);
            if (distance < closestDis) {
                closestID = id;
                closestDis = distance;
            }
        }
        return closestID;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertex.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertex.get(v).lat;
    }

    //For test
    public void printV() {
        for (Long k : vertices()) {
            System.out.println(k);
        }
    }
}
