package controller;

import gov.nasa.worldwind.render.markers.Marker;
import model.Flight;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 12/Apr/2017
 */
public class FlightController {
    private static boolean DEBUGGING = false;

    private HashMap< String, Flight > flightCollection = new HashMap<>();
    private List< Marker > flightMarkers = new ArrayList<>(  );

    public void processStates( OpenSkyStates states ) {
        int added = 0;
        int updated = 0;
        for ( StateVector vector : states.getStates() ) {
            String cs = vector.getCallsign();

            if ( cs != null && !cs.isEmpty() ) {
                if ( flightCollection.containsKey( cs ) ) {
                    updated++;
                    flightCollection.get( cs ).updateFlight( vector );
                } else {
                    added++;
                    Flight f = new Flight( vector );

                    if( f.getMarker() != null){
                        flightMarkers.add( f.getMarker() );
                    }

                    flightCollection.put( cs, f);
                }
            }
        }

        if(DEBUGGING){
            System.out.println( "Added " + added + " flights." );
            System.out.println( "Updated " + updated + " flights." );
        }
    }

    public void printFlights() {
        for ( Flight f : flightCollection.values() ) {
            System.out.println( f + "\n" );
        }
    }

    public List< Marker > getFlightMarkers() {
        return flightMarkers;
    }

    public HashMap< String, Flight > getFlightCollection() {
        return flightCollection;
    }
}
