package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author :
 * @version :
 */
public class ClientMain extends Application {

    static Stage stage;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("ClientLoginFxUI.fxml"));

        this.stage.setTitle("Login");
        this.stage.setScene(new Scene(root,320,580));
        this.stage.initStyle(StageStyle.UNIFIED);
        this.stage.show();
    }
}
