package model;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 12/Apr/2017
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
