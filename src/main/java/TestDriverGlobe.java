/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

import controller.FlightController;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.layers.MarkerLayer;
import model.FlightMarker;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;

import javax.swing.*;

/**
 * This example demonstrates the simplest possible way to create a WorldWind application.
 *
 * @version $Id: TestDriverGlobe.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class TestDriverGlobe extends JFrame {
    private Thread flightUpdater;
    private static String USERNAME = "redtoorange";
    private static String PASSWORD = "orange3161";

    private FlightController flights = new FlightController();
    private OpenSkyApi api = new OpenSkyApi( USERNAME, PASSWORD );
    private MarkerLayer markerLayer = new MarkerLayer( flights.getFlightMarkers() );
    private WorldWindowGLCanvas wwd;

    public TestDriverGlobe() {
        flightUpdater = new Thread( new updateFlights() );

        wwd = new WorldWindowGLCanvas();
        wwd.setPreferredSize( new java.awt.Dimension( 1000, 800 ) );
        this.getContentPane().add( wwd, java.awt.BorderLayout.CENTER );
        wwd.setModel( new BasicModel() );

        markerLayer.setName( "Flights" );
        markerLayer.setEnabled( true );
        wwd.getModel().getLayers().add( markerLayer );

        flightUpdater.start();
        wwd.addSelectListener( new SelectListener() {
            @Override
            public void selected( SelectEvent selectEvent ) {
                if( selectEvent.isLeftClick() && selectEvent.getTopObject() instanceof FlightMarker ){
                    FlightMarker m = (FlightMarker)selectEvent.getTopObject();
                    System.out.println( m.getFlight() );
                    selectEvent.consume();
                }
            }
        } );
    }

    public static void main( String[] args ) {
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                JFrame frame = new TestDriverGlobe();
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.pack();
                frame.setVisible( true );
            }
        } );
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

    class updateFlights implements Runnable{
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