package controller;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import view.NasaGlobeView;

import javax.swing.*;


public class ViewController {
    @FXML
    private SwingNode swingNode;
    @FXML
    private AnchorPane outputPanel;

    private NasaGlobeView sng = new NasaGlobeView();
    private ButtonController buttonController;

    @FXML
    public void initialize() {
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
}
