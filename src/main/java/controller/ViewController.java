package controller;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import model.AppConstants;
import view.FlightGlobePanel;

import javax.swing.*;

/**
 * ViewController.java - The primary view controller the JavaFX interacts with.  All JavaFX nodes are handed in here to
 * be allocated to the specific sub controllers.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class ViewController {
    @FXML
    private SwingNode swingNode;
    @FXML
    private VBox outputPanel;
    @FXML
    private Label callSignLabel, altitudeLabel, headingLabel, originLabel, velocityLabel;
    @FXML
    private WebView webViewer;

    private FlightGlobePanel sng;
    private ButtonController buttonController;
    private FlightInformationController flightInformationControllerController;

    /**
     * Create the sub controllers and the globe.
     */
    @FXML
    public void initialize() {
        flightInformationControllerController = new FlightInformationController( callSignLabel, altitudeLabel, headingLabel, originLabel, velocityLabel, webViewer );

        sng = new FlightGlobePanel( AppConstants.GLOBE_WIDTH, AppConstants.GLOBE_HEIGHT );
        sng.addFlightListener( flightInformationControllerController );

        createAndSetSwingContent();
        buttonController = new ButtonController( sng, outputPanel );
    }

    /**
     * Add content to the Swing node.  This needs to be done in a non-JavaFX Thread.
     */
    private void createAndSetSwingContent() {
        SwingUtilities.invokeLater( () -> swingNode.setContent( sng ) );
    }

    /**
     * Kill command sent in to stop the async update Threads.
     */
    public void killThreads() {
        if ( AppConstants.DEBUGGING )
            System.out.println( "Killing Globe Threads." );
        if ( sng != null )
            sng.killThreads();
    }
}
