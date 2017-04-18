import controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.AppConstants;


public class MainDriver extends Application {
    private ViewController viewController;

    @Override
    public void start( Stage primaryStage ) throws Exception {
        FXMLLoader loader = new FXMLLoader( getClass().getClassLoader().getResource( "View.fxml" ) );
        Parent root = loader.load();
        viewController = loader.getController();

        Scene mainScene = new Scene( root );

        primaryStage.setTitle( "Swing JavaFX Test" );
        primaryStage.setScene( mainScene );


        System.out.println( Screen.getPrimary().getBounds().getWidth() );
        System.out.println( Screen.getPrimary().getBounds().getHeight() );
        System.out.println( Screen.getPrimary().getDpi() );

        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch( args );

    }

    @Override
    public void stop() throws Exception {
        // Send Kill Command
        if ( AppConstants.DEBUGGING )
            System.out.println( "Killing App Threads." );
        viewController.killThreads();

        super.stop();
    }
}
