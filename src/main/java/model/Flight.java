package model;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import org.opensky.model.StateVector;

/**
 * Flight.java - Model that represents a Vector from the API's states data.  This model stores all the key data values
 * from a polling of the data source.  It also stores a reference to the marker that is being used to represent this
 * flight on the globe view.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class Flight {
    private FlightMarker marker;
    private BasicMarkerAttributes attributes;
    private Position position;

    private String callSign;
    private Double altitude;
    private Double heading = 0d;
    private Double longitude;
    private Double latitude;
    private Double velocity;

    private String origin;

    /**
     * Create a new flight based on a flight vector.
     *
     * @param vector The flight's vector from a states object.
     */
    public Flight( StateVector vector ) {
        this.callSign = vector.getCallsign();
        this.altitude = vector.getAltitude();
        this.heading = vector.getHeading();
        this.longitude = vector.getLongitude();
        this.latitude = vector.getLatitude();

        this.origin = vector.getOriginCountry();
        this.velocity = vector.getVelocity();

        buildMarker();
    }

    /**
     * Update a Flight with a new vector object.
     *
     * @param vector The flight's vector from a states object.
     */
    public void updateFlight( StateVector vector ) {
        this.altitude = vector.getAltitude();
        this.heading = vector.getHeading();
        this.longitude = vector.getLongitude();
        this.latitude = vector.getLatitude();

        this.origin = vector.getOriginCountry();
        this.velocity = vector.getVelocity();

        updateMarker();
    }

    /** Build a new marker based on this class' information. */
    private void buildMarker() {
        //If it has a lat and long, it can have a position.
        if ( longitude != null && latitude != null ) {
            if ( altitude != null ) //if it had an altitude, we can use that to.
                position = Position.fromDegrees( latitude, longitude, altitude );
            else
                position = Position.fromDegrees( latitude, longitude );
        }

        //Set the starting attributes
        attributes = new BasicMarkerAttributes( AppConstants.DEFAULT_FLIGHT_MAT, BasicMarkerShape.ORIENTED_SPHERE, 1d, 10, 5 );
        attributes.setHeadingMaterial( AppConstants.DEFAULT_HEADING_MAT );

        //If there was a position, then build a marker.
        if ( position != null )
            marker = new FlightMarker( position, attributes, Angle.fromDegrees( heading ), this );
    }

    /** Update the marker's information. */
    private void updateMarker() {
        //Refresh the marker's opacity, it's been updated.
        attributes.setOpacity( 1 );

        //If it has a lat and long, it can have a position.
        if ( longitude != null && latitude != null ) {
            if ( altitude != null ) //if it had an altitude, we can use that to.
                position = Position.fromDegrees( latitude, longitude, altitude );
            else
                position = Position.fromDegrees( latitude, longitude );
        }

        //Set the marker's position or create a marker if it doesn't have one.
        if ( position != null && marker != null ) {
            marker.setPosition( position );
        } else if ( position != null && marker == null ) {
            marker = new FlightMarker( position, attributes, Angle.fromDegrees( heading ), this );
        }
    }

    /** @return The FlightMarker that represents this Flight. */
    public FlightMarker getMarker() {
        return marker;
    }

    /** @return A String representation of the flight data. */
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

    /** @return The Flight's Callsign. */
    public String getCallSign() {
        return callSign;
    }

    /** @return The Flight's current altitude. */
    public Double getAltitude() {
        return altitude;
    }

    /** @return The Flight's current heading. */
    public Double getHeading() {
        return heading;
    }

    /** @return The Flight's current velocity. */
    public Double getVelocity() {
        return velocity;
    }

    /** @return The Flight's Origin Country. */
    public String getOrigin() {
        return origin;
    }
}
