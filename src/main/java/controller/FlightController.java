package controller;

import gov.nasa.worldwind.render.markers.Marker;
import model.Flight;
import model.PollDataException;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FlightController.java - A controller that acts as a wrapper for a collection of Flights and their Corresponding
 * Markers.  Abstracts away the updating and the creation of flights.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class FlightController {
    private HashMap< String, Flight > flightCollection;
    private List< Marker > flightMarkers;

    /** Construct the FlightController */
    public FlightController() {
        flightCollection = new HashMap<>();
        flightMarkers = new ArrayList<>();
    }

    /**
     * Parse a OpenSkyStates data structure out into the individual flights.  Each flight is matched with is corresponding
     * "vector".  This vector is handed to the flight to allow it to update it's own information.
     *
     * @param states A collection of "vectors" passed in from the OpenSky API
     * @throws PollDataException If the polling times out, this is thrown.
     */
    public void processStates( OpenSkyStates states ) throws PollDataException {
        try {
            //Each Vector is a separate flight.
            for ( StateVector vector : states.getStates() ) {
                String cs = vector.getCallsign();

                //if there is no callsign, there is nothing to index the flight by, toss it.
                if ( cs != null && !cs.isEmpty() ) {
                    if ( flightCollection.containsKey( cs ) ) {
                        //Old callsign?  Update the flight information.
                        Flight f = flightCollection.get( cs );
                        f.updateFlight( vector );
                    } else {
                        //New callsign?  Create a new flight for it.
                        Flight f = new Flight( vector );

                        if ( f.getMarker() != null )
                            flightMarkers.add( f.getMarker() );

                        flightCollection.put( cs, f );
                    }
                }
            }
        } catch ( Exception e ) {
            //The connection timed out.
            throw new PollDataException();
        }
    }

    /**
     * @return The collection of all the Markers that represent the Flights controlled here.
     */
    public List< Marker > getFlightMarkers() {
        return flightMarkers;
    }
}
