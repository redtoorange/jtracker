package view;

import controller.CustomGlobePanel;
import controller.FlightController;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.layers.Earth.OSMMapnikLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.MarkerLayer;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;

import javax.swing.*;


public class NasaGlobeView extends JPanel {
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


    public NasaGlobeView() {
        wwd = new CustomGlobePanel();
        wwd.setPreferredSize( new java.awt.Dimension( 1050, 700 ) );
        wwd.setSize( 1050, 700 );
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

    private void getFlightData() {
        try {
            flights.processStates( pollStates( api ) );
            //flights.printFlights();
        } catch ( Exception e ) {
            System.out.println( "An error occurred during the poll" );
            e.printStackTrace();
        }

    }

    private OpenSkyStates pollStates( OpenSkyApi api ) throws Exception {
        return api.getStates( 0, null );
    }

    class FlightUpdater implements Runnable{
        @Override
        public void run() {
            try{
                while( true ){
                    getFlightData();
                    wwd.redraw();
                    Thread.sleep( 10000 );
                }
            }
            catch(Exception e){
                System.out.println( "model.Flight update thread lost something." );
            }
        }
    }

}
