package controller;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import view.FlightGlobePanel;

/**
 * ButtonController.java - A controller that will dynamically create new buttons for layers.  These buttons will allow
 * the user to enable/disable different layers.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class ButtonController {
    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 50;

    /**
     * Each layer must be added to this Array with the string matching the layer name.
     */
    private final String[] layers = { "Flights", "OSM", };

    private FlightGlobePanel sng;
    private VBox outputPanel;

    public ButtonController( FlightGlobePanel sng, VBox outputPanel ) {
        this.sng = sng;
        this.outputPanel = outputPanel;

        initLayerButtons();
    }

    /**
     * Create the buttons the control the layers on the map.  Dynamically
     * build the buttons based on the Strings inside of the Layer Array.
     */
    private void initLayerButtons() {
        for ( int i = 0; i < layers.length; i++ ) {
            Button b = new Button( layers[i] );

            // The Flights later starts enabled.
            if ( b.getText().equals( "Flights" ) )
                b.setStyle( "-fx-base: #b6e7c9;" );

            b.setPrefWidth( BUTTON_WIDTH );
            b.setPrefHeight( BUTTON_HEIGHT );

            b.addEventHandler( MouseEvent.MOUSE_CLICKED, event -> {
                setButtonColor( sng.toggleLayer( b.getText() ), b );
            } );
            outputPanel.getChildren().add( b );
        }
    }

    /**
     * Dynamically change the style of the button.  Make it green if the layer is enabled.
     *
     * @param enabled Enable or disable the button?
     * @param button  The button that was clicked?
     */
    private void setButtonColor( boolean enabled, Button button ) {
        if ( enabled )
            button.setStyle( "-fx-base: #b6e7c9;" );
        else
            button.setStyle( "" );
    }
}
