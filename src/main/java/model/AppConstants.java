package model;

import gov.nasa.worldwind.render.Material;

/**
 * AppConstants.java - Store general settings for development and debugging.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class AppConstants {
    /** Default Color for flights. */
    public static final Material DEFAULT_FLIGHT_MAT = Material.LIGHT_GRAY;
    /** Default Color for the Flight's heading marker. */
    public static final Material DEFAULT_HEADING_MAT = Material.LIGHT_GRAY;
    /** Selected Color for flights. */
    public static final Material SELECTED_FLIGHT_MAT = Material.GREEN;
    /** Selected Color for the Flight's heading marker. */
    public static final Material SELECTED_HEADING_MAT = Material.GREEN;
    /** Hovering Color for flights. */
    public static final Material HOVER_FLIGHT_MAT = Material.YELLOW;
    /** Hovering Color for the Flight's heading marker. */
    public static final Material HOVER_HEADING_MAT = Material.YELLOW;
    /** How much to scale the marker by when hovering. */
    public static final double HOVER_ZOOM = 1.4d;
    /** Print debugging data? */
    public static boolean DEBUGGING = true;
    /** Should to */
    public static boolean ANONYMOUS = true;
    public static String USERNAME = "redtoorange";
    public static String PASSWORD = "orange3161";
    /** How often to poll the API in seconds.   */
    public static int FLIGHT_REFRESH_INTERVAL = 10;
    public static int GLOBE_WIDTH= 1050;
    public static int GLOBE_HEIGHT = 675;
}
