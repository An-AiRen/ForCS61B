import javax.swing.text.GapContent;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /** Query request from user. */
    private double dst_ullat;
    private double dst_ullon;
    private double dst_lrlat;
    private double dst_lrlon;
    private double w;
    private double h;
    private boolean query_success;
    private double LonDPP;
    private double gap; // longitudinal distance per part.
    private double gap_h; // latitudinal distance per part.
    private int startX, startY, endX, endY; //For position dimension of raster.

    public Rasterer() {
        /** Initialize all of the paras. */
        dst_ullat = MapServer.ROOT_ULLAT;
        dst_ullon = MapServer.ROOT_ULLON;
        dst_lrlat = MapServer.ROOT_LRLAT;
        dst_lrlon = MapServer.ROOT_LRLON;
        w = MapServer.TILE_SIZE;
        h = MapServer.TILE_SIZE;
        query_success = false;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();

        getUserPara(params);

        int depth = confirmDepth();
        String[][] raster = findQueryResult(depth);
        double raster_ul_lon = MapServer.ROOT_ULLON + gap * startX;
        double raster_ul_lat = MapServer.ROOT_ULLAT - gap_h * startY;
        double raster_lr_lon = MapServer.ROOT_ULLON + gap * (endX + 1);
        double raster_lr_lat = MapServer.ROOT_ULLAT - gap_h * (endY + 1); //Forget to add 1!

        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("render_grid", raster);
        results.put("depth", depth);
        results.put("query_success", query_success);
        return results;
    }

    /** Deal with the input from user. */
    private void getUserPara(Map<String, Double> params) {
        String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
                "lrlon", "w", "h"};
        for (String attr : REQUIRED_RASTER_REQUEST_PARAMS) {
            /** if the input stream does not contain any one of the required params, query
             *  request should fail.*/
            if (!params.containsKey(attr)) {
                return;
            }
            switch (attr) {
                case "ullat":dst_ullat = params.get("ullat");break;
                case "ullon":dst_ullon = params.get("ullon");break;
                case "lrlat":dst_lrlat = params.get("lrlat");break;
                case "lrlon":dst_lrlon = params.get("lrlon");break;
                case "w":w = params.get("w");break;
                case "h":h = params.get("h");break;
            }
        }

        /** To judge if UL and LR make sense. */
        if (dst_ullat < dst_lrlat || dst_ullon > dst_lrlon
        || dst_ullat < MapServer.ROOT_LRLAT || dst_lrlat > MapServer.ROOT_ULLAT
        || dst_ullon > MapServer.ROOT_LRLON || dst_lrlon < MapServer.ROOT_ULLON) {
            return;
        }

        LonDPP = (dst_lrlon - dst_ullon) / w;
        query_success = true;
    }

    /** Confirm the depth of img to be used.
     * @return depth if depth is smaller than 7, otherwise 7.
     */
    private int confirmDepth() {
        int depth = -1;
        double cur_LonDDP = LonDPP + 1;
        gap = 2 * (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON);

        while (cur_LonDDP > LonDPP && depth < 7) {
            depth += 1;
            // Calculate the longitudinal gap for each d.
            gap = gap / 2;
            cur_LonDDP = gap / MapServer.TILE_SIZE;
        }

        return depth;
    }

    private String[][] findQueryResult(int depth) {
        if (!query_success) {
            return null;
        }

        // Deal with the situation where some parts of raster is outside of the region.
        if (dst_ullon < MapServer.ROOT_ULLON) {
            startX = 0;
        }
        if (dst_ullat > MapServer.ROOT_ULLAT) {
            startY = 0;
        }
        if (dst_lrlon > MapServer.ROOT_LRLON) {
            endX = (int)Math.pow(2, depth) - 1;
        }
        if (dst_lrlat < MapServer.ROOT_LRLAT) {
            endY = (int)Math.pow(2, depth) - 1;
        }

        // Calculate the start position of searching.
        gap_h = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth); //Forget at the first time!
        startX = (int)Math.floor((dst_ullon - MapServer.ROOT_ULLON) / gap);
        startY = (int)Math.floor((MapServer.ROOT_ULLAT - dst_ullat) / gap_h);
        endX = (int)Math.floor((dst_lrlon - MapServer.ROOT_ULLON) / gap);
        endY = (int)Math.floor((MapServer.ROOT_ULLAT - dst_lrlat) / gap_h);

        //Searching.....
        String[][] query = new String[endY - startY + 1][endX - startX + 1];
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                query[j - startY][i - startX] = "d" + depth + "_x" + i + "_y" + j + ".png";
            }
        }
        return query;
    }

//    public static void main(String[] args) {
//        Map<String, Double> input = new HashMap<>();
//        Rasterer r = new Rasterer();
//        input.put("lrlon", -122.24053369025242);
//        input.put("ullon", -122.24163047377972);
//        input.put("ullat", 37.87655856892288);
//        input.put("lrlat", 37.87548268822065);
//        input.put("w", 892.0);
//        input.put("h", 875.0);
//        Map<String, Object> results = r.getMapRaster(input);
//        System.out.println(results);
//    }
}
