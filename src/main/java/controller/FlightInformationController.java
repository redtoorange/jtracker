package controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Flight;

import java.text.NumberFormat;

/**
 * FlightInformationController.java - Pulls information from the flight a user clicks and places it into the View.  It
 * then loads the FlightAware page into the detailed view tab.
 *
 * @author Andrew McGuiness
 * @version 4/18/2017
 */
public class FlightInformationController implements FlightChangeListener {
    private final String FLIGHT_DATA = "http://flightaware.com/live/flight/";
    private NumberFormat formatter = NumberFormat.getIntegerInstance();
    private Label callsignLabel, altituteLabel, headingLabel, originLabel, velocityLabel;
    private WebEngine engine;

    /**
     * Create the Flight Information Controller.
     *
     * @param callsignLabel The Label to display the Call Sign in.
     * @param altituteLabel The Label to display the Altitude in.
     * @param headingLabel  The Label to display the Heading in.
     * @param originLabel   The Label to display the Origin Country in.
     * @param velocityLabel The Label to display the Velocity in.
     * @param webView       The WebView to load the FlightAware page in.
     */
    public FlightInformationController( Label callsignLabel, Label altituteLabel, Label headingLabel, Label originLabel,
                                        Label velocityLabel, WebView webView ) {
        this.callsignLabel = callsignLabel;
        this.altituteLabel = altituteLabel;
        this.headingLabel = headingLabel;
        this.originLabel = originLabel;
        this.velocityLabel = velocityLabel;

        engine = webView.getEngine();
    }

    /**
     * Update the information in the display based on the flight.
     *
     * @param flight The Flight to parse the data from.
     */
    public void updateInformation( Flight flight ) {
        engine.load( FLIGHT_DATA + flight.getCallSign() );

        callsignLabel.setText( flight.getCallSign() );
        altituteLabel.setText( formatter.format( flight.getAltitude() * 3.28084 ) + "ft" );
        headingLabel.setText( formatter.format( flight.getHeading() ) + "º" );
        originLabel.setText( flight.getOrigin() );
        velocityLabel.setText( formatter.format( flight.getVelocity() * 2.23694 ) + "mph" );
    }

    /**
     * The call back to use to update the flight information.  Must be called in a separate thread to prevent a collision,
     * with JavaFX.
     *
     * @param flight The flight to update the information with.
     */
    @Override
    public void flightChanged( Flight flight ) {
        Platform.runLater( () -> updateInformation( flight ) );
    }
}
