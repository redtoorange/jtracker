import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainDriver extends Application{
    @Override
    public void start( Stage primaryStage ) throws Exception {
        FXMLLoader loader = new FXMLLoader( getClass().getClassLoader().getResource( "View.fxml" ) );
        Parent root = loader.load();
        Scene mainScene = new Scene( root );

        primaryStage.setTitle( "Swing JavaFX Test" );
        primaryStage.setScene( mainScene );
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch( args );

    }
}
