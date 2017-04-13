package model;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.*;
import org.opensky.model.StateVector;

public class Flight {
    private FlightMarker marker;
    private MarkerAttributes attributes;
    private Position position;

    private String callSign;
    private Double altitude;
    private Double heading = 0d;
    private Double longitude;
    private Double latitude;

    public Flight( StateVector vector ) {
        this.callSign = vector.getCallsign();
        this.altitude = vector.getAltitude();
        this.heading = vector.getHeading();
        this.longitude = vector.getLongitude();
        this.latitude = vector.getLatitude();

        buildMarker();
    }

    public void updateFlight( StateVector vector ) {
        this.altitude = vector.getAltitude();
        this.heading = vector.getHeading();
        this.longitude = vector.getLongitude();
        this.latitude = vector.getLatitude();

        updateMarker();
    }

    private void buildMarker() {
        if ( longitude != null && latitude != null ) {
            if ( altitude != null )
                position = Position.fromDegrees( latitude, longitude, altitude );
            else
                position = Position.fromDegrees( latitude, longitude );
        }


        if ( position != null ) {
            attributes = new BasicMarkerAttributes( Material.RED, BasicMarkerShape.HEADING_ARROW, 1, 10, 5 );
            marker = new FlightMarker( position, attributes, Angle.fromDegrees( heading ), this );
        }
    }

    private void updateMarker() {
        if ( longitude != null && latitude != null ) {
            if ( altitude != null )
                position = Position.fromDegrees( latitude, longitude, altitude );
            else
                position = Position.fromDegrees( latitude, longitude );
        }

        if ( position != null && marker != null )
            marker.setPosition( position );
        else if ( position != null && marker == null ) {
            attributes = new BasicMarkerAttributes( Material.RED, BasicMarkerShape.HEADING_ARROW, 1 );
            marker = new FlightMarker( position, attributes, Angle.fromDegrees( heading ), this );
        }
    }

    public FlightMarker getMarker() {
        return marker;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( "Call Sign: " + callSign + "\n" );

        sb.append( "\tAlt: " + altitude + "\n" );
        sb.append( "\tHeading: " + heading + "\n" );
        sb.append( "\tLon: " + longitude + "\n" );
        sb.append( "\tLat: " + latitude + "\n" );

        return sb.toString();
    }


}
