import controller.FlightController;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 12/Apr/2017
 */
public class OpenSkyTestDriver {
    private static String USERNAME = "redtoorange";
    private static String PASSWORD = "orange3161";



    public static void main( String[] args ) throws Exception {
        FlightController flights = new FlightController();
        OpenSkyApi api = new OpenSkyApi( USERNAME, PASSWORD );


        flights.processStates( pollStates( api ));
        flights.printFlights();

        Thread.sleep( 60000 );

        flights.processStates( pollStates( api ));
        flights.printFlights();

        Thread.sleep( 60000 );

        flights.processStates( pollStates( api ));
        flights.printFlights();
    }

    private static OpenSkyStates pollStates( OpenSkyApi api ) throws Exception{
        return api.getStates( 0, null );
    }
}
