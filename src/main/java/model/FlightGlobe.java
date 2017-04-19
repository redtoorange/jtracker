package model;

import controller.FlightChangeController;
import controller.FlightChangeListener;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.layers.Earth.OSMMapnikLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * FlightGlobe.java - Custom implementation of the Nasa Globe Panel.  Contains custom layers to display Flight
 * information.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class FlightGlobe extends WorldWindowGLJPanel implements FlightChangeController {
    private List< FlightChangeListener > listeners;
    private Marker lastFlightHovered;
    private FlightMarker selectedMarker;
    private BasicMarkerAttributes oldMarkerAttributes;
    private Flight currentFlight;
    private MarkerLayer markerLayer;
    private Layer osm;

    /**
     * Create the panel.  This sets up the click listeners to handle clicking flights and hovering.
     *
     * @param width      The Width of the panel.
     * @param height     The Height of the panel.
     * @param flightList The collection containing all the Flight Markers.
     */
    public FlightGlobe( int width, int height, List< Marker > flightList ) {
        listeners = new ArrayList<>();

        setPreferredSize( new java.awt.Dimension( width, height ) );
        setModel( new BasicModel() );

        buildLayers( flightList );

        addSelectListener( this::handleClickEvent );
        addSelectListener( this::handleRollOver );
    }

    /**
     * Setup the OpenStreetMap and Flights Layer.
     *
     * @param flightList The collection of markers that represent the flights.
     */
    private void buildLayers( List< Marker > flightList ) {
        markerLayer = new MarkerLayer( flightList );
        osm = new OSMMapnikLayer();

        osm.setEnabled( false );
        osm.setName( "OSM" );

        markerLayer.setEnabled( true );
        markerLayer.setName( "Flights" );

        getModel().getLayers().add( markerLayer );
        getModel().getLayers().add( osm );
    }

    /**
     * <p>
     * <b>
     *     <font color="red">
     *         Based on code from <a href="https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/Markers.java">
     * Nasa Tutorial Series</a>.
     *         </font>
     *
     * </b>
     * </p>
     * <p>
     * When the mouse rolls over a marker, it will zoom in by the HOVER_ZOOM_FACTOR and change color to the HOVER_FLIGHT_MAT.
     *
     * @param event The event that triggered this event handler, stores which marker was hovered.
     */
    private void handleRollOver( SelectEvent event ) {
        if ( lastFlightHovered != null
                && ( event.getTopObject() == null || !event.getTopObject().equals( lastFlightHovered ) ) ) {
            lastFlightHovered.setAttributes( oldMarkerAttributes );
            lastFlightHovered = null;
        }

        if ( !event.getEventAction().equals( SelectEvent.ROLLOVER ) )
            return;
        if ( event.getTopObject() == null || event.getTopPickedObject().getParentLayer() == null )
            return;
        if ( event.getTopPickedObject().getParentLayer() != markerLayer )
            return;

        if ( lastFlightHovered == null && event.getTopObject() instanceof Marker ) {
            lastFlightHovered = ( Marker ) event.getTopObject();
            oldMarkerAttributes = ( BasicMarkerAttributes ) lastFlightHovered.getAttributes();

            MarkerAttributes hoverAttributes = new BasicMarkerAttributes( oldMarkerAttributes );

            hoverAttributes.setMaterial( AppConstants.HOVER_FLIGHT_MAT );
            hoverAttributes.setHeadingMaterial( AppConstants.HOVER_HEADING_MAT );
            hoverAttributes.setOpacity( 1d );
            hoverAttributes.setMarkerPixels( oldMarkerAttributes.getMarkerPixels() * AppConstants.HOVER_ZOOM );
            hoverAttributes.setMinMarkerSize( oldMarkerAttributes.getMinMarkerSize() * AppConstants.HOVER_ZOOM );

            lastFlightHovered.setAttributes( hoverAttributes );
        }
    }

    /**
     * Update the color of the clicked marker and changed the currently selected marker.  This notifies all listeners.
     *
     * @param selectEvent The event that triggered this event handler, stores which marker was clicked.
     */
    private void handleClickEvent( SelectEvent selectEvent ) {
        if ( selectEvent.isLeftClick() && selectEvent.getTopObject() instanceof FlightMarker ) {
            if ( selectedMarker != null ) {
                selectedMarker.getAttributes().setMaterial( AppConstants.DEFAULT_FLIGHT_MAT );
                selectedMarker.getAttributes().setHeadingMaterial( AppConstants.DEFAULT_HEADING_MAT );
            }

            selectedMarker = ( FlightMarker ) selectEvent.getTopObject();
            currentFlight = selectedMarker.getFlight();
            notifyListeners();

            selectedMarker.getAttributes().setMaterial( AppConstants.SELECTED_FLIGHT_MAT );
            selectedMarker.getAttributes().setHeadingMaterial( AppConstants.SELECTED_HEADING_MAT );
            oldMarkerAttributes.setMaterial( AppConstants.SELECTED_FLIGHT_MAT );
            oldMarkerAttributes.setHeadingMaterial( AppConstants.SELECTED_HEADING_MAT );
            selectEvent.consume();
        }
    }

    /**
     * Add a FlightChangeListener to this controller.  Will allow the controller to notify the listeners when there is
     * a change to the current flight.
     *
     * @param flightChangeListener The FlightChangeListener to add to this controller.
     */
    @Override
    public void addListener( FlightChangeListener flightChangeListener ) {
        listeners.add( flightChangeListener );
    }

    /** Tell all the listeners that the current flight has changed. */
    @Override
    public void notifyListeners() {
        for ( FlightChangeListener fcl : listeners ) {
            fcl.flightChanged( currentFlight );
        }
    }

    public FlightMarker getSelectedMarker() {
        return selectedMarker;
    }
}
