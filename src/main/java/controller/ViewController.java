package controller;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import model.AppConstants;
import view.NasaGlobeView;

import javax.swing.*;


public class ViewController {
    @FXML
    private SwingNode swingNode;
    @FXML
    private AnchorPane outputPanel;
    @FXML
    private Pane globePane;

    private NasaGlobeView sng;
    private ButtonController buttonController;

    @FXML
    public void initialize() {
        Rectangle2D rect= Screen.getPrimary().getBounds();

        double dpi = Screen.getPrimary().getDpi();
        sng = new NasaGlobeView( (int)(1050 * (dpi/72)), (int)(700 * (dpi/72)) );

        createAndSetSwingContent();
        buttonController = new ButtonController( sng, outputPanel );

    }

    private void createAndSetSwingContent() {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                swingNode.setContent( sng );
            }
        } );
    }

    public void killThreads() {
        if ( AppConstants.DEBUGGING )
            System.out.println( "Killing Globe Threads." );
        if ( sng != null )
            sng.killThreads();
    }
}
