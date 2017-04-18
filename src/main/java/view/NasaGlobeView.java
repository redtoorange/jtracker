package view;

import controller.CustomGlobePanel;
import controller.FlightController;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.layers.Earth.OSMMapnikLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.MarkerLayer;
import model.AppConstants;
import model.FlightMarker;
import model.PollDataException;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;

import javax.swing.*;


public class NasaGlobeView extends JPanel {
    private boolean running = true;
    //private RenderableLayer layer;
    private Thread fadeThread;
    private Thread flightUpdater;

    private static String USERNAME = "redtoorange";
    private static String PASSWORD = "orange3161";

    private FlightController flights = new FlightController();
    private OpenSkyApi api = new OpenSkyApi( USERNAME, PASSWORD );

    private Layer osm = new OSMMapnikLayer();
    private MarkerLayer markerLayer = new MarkerLayer( flights.getFlightMarkers() );
    private CustomGlobePanel wwd;


    public NasaGlobeView( int width, int height) {
        this.setSize( width, height );
        wwd = new CustomGlobePanel();
        wwd.setPreferredSize( new java.awt.Dimension( width, height ) );

//        wwd.setSize( 1050, 700 );
        this.add( wwd );
        wwd.setModel( new BasicModel() );

        osm.setEnabled( false );
        osm.setName( "OSM" );

        markerLayer.setEnabled( false );
        markerLayer.setName( "Flights" );


        wwd.getModel().getLayers().add( markerLayer );
        wwd.getModel().getLayers().add( osm );

//        fadeThread = new Thread( new FadeMarkers() );
//        fadeThread.start();

        flightUpdater = new Thread( new FlightUpdater() );
        flightUpdater.start();

        wwd.addSelectListener( new SelectListener() {
            @Override
            public void selected( SelectEvent selectEvent ) {
                if ( selectEvent.isLeftClick() && selectEvent.getTopObject() instanceof FlightMarker ) {
                    FlightMarker m = ( FlightMarker ) selectEvent.getTopObject();
                    System.out.println( m.getFlight() );
                    selectEvent.consume();
                }
            }
        } );
    }

    public void autoSize(){
        this.setSize( this.getParent().getWidth(), getParent().getHeight() );
        wwd.setPreferredSize( new java.awt.Dimension( getWidth(), getHeight() ) );
    }


//    class FadeMarkers implements Runnable{
//        int i = 0;
//        @Override
//        public void run() {
//            while(i < 1000){
//                System.out.println( "Fading" );
//                fade();
//                i++;
//                try{
//                    Thread.sleep( 17 );
//                }
//                catch(InterruptedException ie){
//                    ie.printStackTrace();
//                }
//            }
//        }
//
//        private void fade(){
//            for(int i = 0; i < markerList.size(); i++){
//                MarkerAttributes a = markerList.get( i ).getAttributes();
//
//                double opacity = a.getOpacity();
//                if(opacity > 0){
//                    opacity -= 1/1000f;
//                    if( opacity < 0 )
//                        opacity = 0;
//
//                    a.setOpacity( opacity );
//                }
//
//                markerList.get( i ).setAttributes( a );
//            }
//            wwd.redraw();
//        }
//    }


    public boolean toggleLayer( String name ) {
        Layer l = wwd.getModel().getLayers().getLayerByName( name );

        boolean enabled = l.isEnabled();
        l.setEnabled( !enabled );
        return !enabled;
    }

    private void getFlightData() throws PollDataException {
        try {
            flights.processStates( pollStates( api ) );
        } catch ( Exception e ) {
            throw new PollDataException();
        }
    }

    private OpenSkyStates pollStates( OpenSkyApi api ) throws Exception {
        return api.getStates( 0, null );
    }

    class FlightUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while ( running ) {
                    if ( running )
                        getFlightData();

                    wwd.redraw();

                    if ( running )
                        Thread.sleep( 10000 );
                }
            } catch ( PollDataException pde ) {
                if ( AppConstants.DEBUGGING )
                    System.out.println( "Polling was unable to finish." );
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
    }

    public void killThreads() {
        running = false;
        flightUpdater.interrupt();
    }
}
