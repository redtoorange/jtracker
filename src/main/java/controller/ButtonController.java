package controller;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import view.NasaGlobeView;


public class ButtonController {
    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 50;

    private final String[] layers = { "OSM", "Flights" };
    private NasaGlobeView sng;
    private AnchorPane outputPanel;

    public ButtonController( NasaGlobeView sng, AnchorPane outputPanel ) {
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

            b.setPrefWidth( BUTTON_WIDTH );
            b.setPrefHeight( BUTTON_HEIGHT );

            b.setLayoutX( ( outputPanel.getPrefWidth() - BUTTON_WIDTH ) / 2f );
            b.setLayoutY( BUTTON_HEIGHT * i );

            b.addEventHandler( MouseEvent.MOUSE_CLICKED, event -> {
                setButtonColor( sng.toggleLayer( b.getText() ), b );
            } );
            outputPanel.getChildren().add( b );
        }
    }

    private void setButtonColor( boolean enabled, Button button ) {
        if ( enabled )
            button.setStyle( "-fx-base: #b6e7c9;" );
        else
            button.setStyle( "" );
    }
}
