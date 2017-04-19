package model;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

/**
 * FlightMarker.java -  Custom Marker that contains information about the underlying flight it represents.  This
 * is designed to simplify looking up the flight data.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class FlightMarker extends BasicMarker {
    private Flight flight;

    public FlightMarker( Position position, MarkerAttributes attrs, Flight flight ) {
        super( position, attrs );
        this.flight = flight;
    }

    public FlightMarker( Position position, MarkerAttributes attrs, Angle heading, Flight flight ) {
        super( position, attrs, heading );
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }
}
