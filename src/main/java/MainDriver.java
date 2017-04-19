import controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.AppConstants;

/**
 * MainDriver.java - The primary Driver for the Application.  Setup in the way that JavaFX requires.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public class MainDriver extends Application {
    /** Kept so that the kill command can be sent. */
    private ViewController viewController;

    public static void main( String[] args ) {
        launch( args );
    }

    /**
     * Load up the View and launch the application.
     *
     * @param primaryStage Passed in by the System.
     * @throws Exception Throws default JavaFX Exceptions.
     */
    @Override
    public void start( Stage primaryStage ) throws Exception {
        FXMLLoader loader = new FXMLLoader( getClass().getClassLoader().getResource( "View.fxml" ) );
        Parent root = loader.load();
        viewController = loader.getController();

        Scene mainScene = new Scene( root );

        primaryStage.setTitle( "Swing JavaFX Test" );
        primaryStage.setScene( mainScene );

        primaryStage.show();
    }

    /**
     * Custom implementation to kill off all the non-JavaFX Threads.
     *
     * @throws Exception Throws default JavaFX Exceptions.
     */
    @Override
    public void stop() throws Exception {
        // Send Kill Command
        if ( AppConstants.DEBUGGING )
            System.out.println( "Killing App Threads." );

        viewController.killThreads();

        super.stop();
    }
}
