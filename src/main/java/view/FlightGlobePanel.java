package view;

import controller.FlightChangeListener;
import controller.FlightController;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import model.AppConstants;
import model.FlightGlobe;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;

import javax.swing.*;
import java.util.List;

/**
 * FlightGlobePanel.java -  A JPanel that contains a GlobeView.  Contains and handles the updating of the Globe and the
 * Threads associated with the Globe's data.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class FlightGlobePanel extends JPanel {
    /** Is the panel running? */
    private boolean running = true;
    /** Handles the fading of the markers. */
    private Thread fadeThread;
    /** Handles updating the flight information. */
    private Thread flightUpdater;

    private FlightController flights = new FlightController();
    private final List< Marker > flightList = flights.getFlightMarkers();

    /** Should the API be started in a anonymous or logged-in state? */
    private OpenSkyApi api = ( AppConstants.ANONYMOUS ) ? new OpenSkyApi() : new OpenSkyApi( AppConstants.USERNAME, AppConstants.PASSWORD );
    private FlightGlobe globePanel;

    /**
     * Create a new FlightGlobe.
     *
     * @param width  The width in pixels of the globe view.
     * @param height The height in pixels of the globe view.
     */
    public FlightGlobePanel( int width, int height ) {
        this.setSize( width, height );

        globePanel = new FlightGlobe( width, height, flightList );
        add( globePanel );

        launchThreads();
    }

    /** Start all the update Threads. */
    private void launchThreads() {
        fadeThread = new Thread( new FadeMarkers() );
        fadeThread.start();

        flightUpdater = new Thread( new FlightUpdater() );
        flightUpdater.start();
    }

    /**
     * Toggle a specified layer on or off depending on it's current state.
     *
     * @param name The name of the Layer that should be toggled.
     * @return True if the Layer was enabled, otherwise false.
     */
    public boolean toggleLayer( String name ) {
        Layer l = globePanel.getModel().getLayers().getLayerByName( name );

        boolean enabled = l.isEnabled();
        l.setEnabled( !enabled );
        return !enabled;
    }

    /** The Application is attempting to exit, stop all the Threads. */
    public void killThreads() {
        running = false;
        flightUpdater.interrupt();
        fadeThread.interrupt();
    }

    /**
     * Adds a FlightChangeListener to the Flight View.
     *
     * @param flightChangeListener The change listener that should be added to the Flight Display.
     */
    public void addFlightListener( FlightChangeListener flightChangeListener ) {
        globePanel.addListener( flightChangeListener );
    }

    /** Class that handles the Thread responsible for fading the markers. */
    private class FadeMarkers implements Runnable {
        @Override
        public void run() {
            try {
                while ( running ) {
                    fade();
                    globePanel.redraw();
                    Thread.sleep( 17 );
                }
            } catch ( InterruptedException ie ) {
                if ( AppConstants.DEBUGGING )
                    System.out.println( "Interrupted the thread." );
            } catch ( Exception e ) {
                e.printStackTrace();
                System.out.println( "Some other error." );
            }
        }

        /** Fade the markers by 1/2000 per 1/60th of a second. */
        private void fade() {
            for ( int i = 0; i < flightList.size(); i++ ) {
                MarkerAttributes a = flightList.get( i ).getAttributes();
                double opacity = a.getOpacity() - ( 1d / 2000d );
                a.setOpacity( Math.max( opacity, 0 ) );

                if ( AppConstants.DEBUGGING )
                    if ( flightList.get( i ) == globePanel.getSelectedMarker() )
                        System.out.println( opacity );
            }
        }
    }

    /** Thread to continuously update the flight information. */
    private class FlightUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while ( running ) {
                    //Poll for data and update flights
                    getFlightData();

                    globePanel.redraw();

                    //Sleep for 10 seconds
                    Thread.sleep( AppConstants.FLIGHT_REFRESH_INTERVAL * 1000 );
                }
            } catch ( SecurityException se ) {
                if ( AppConstants.DEBUGGING )
                    System.out.println( "Could not interrupt the Thread, waiting to close." );
            } catch ( InterruptedException ie ) {
                if ( AppConstants.DEBUGGING )
                    System.out.println( "Interrupted the thread." );
            } catch ( Exception e ) {
                e.printStackTrace();
                System.out.println( "Some other error." );
            }
        }

        /** Process the states handed down from the API. */
        private void getFlightData() {
            try {
                flights.processStates( pollStates( api ) );
            } catch ( Exception e ) {
                if ( AppConstants.DEBUGGING ) {
                    e.printStackTrace();
                    System.out.println( e.getMessage() );
                    System.out.println( "Polling was unable to finish." );
                }
            }
        }

        /**
         * Obtain the state data from the API.
         *
         * @param api The OpenSky API
         * @return The dataSet returned from the API.
         * @throws Exception If the API is non-responsive.
         */
        private OpenSkyStates pollStates( OpenSkyApi api ) throws Exception {
            return api.getStates( 0, null );
        }
    }
}
