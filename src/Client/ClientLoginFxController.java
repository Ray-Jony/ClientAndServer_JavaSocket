package Client;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author :
 * @version :
 */
public class ClientLoginFxController implements Initializable {

    public static Client client;

    @FXML
    public TextField Username;
    @FXML
    public PasswordField Password;
    @FXML
    public Button login;
    @FXML
    public Button logRegister;

    @FXML
    public void setLogin(ActionEvent e) throws IOException {
        String username = Username.getText();
        String password = Password.getText();
        String loginInfo = username + "@" + password;

        ClientLogin login;
        login = client.getClientLogin();
        login.setLoginInfo(loginInfo);
        login.start();

        if (client.isLogin()) {
            Alert successfulLogin = new Alert(Alert.AlertType.INFORMATION, "you are online", ButtonType.OK);
            successfulLogin.showAndWait();
            Parent chatRoomParent = FXMLLoader.load(getClass().getResource("ClientMainFxUI.fxml"));
            //change Scene
            {
                Scene sceneParentFrame = new Scene(chatRoomParent);

                Stage windowChat = (Stage) ((Node) e.getSource()).getScene().getWindow();
                windowChat.setTitle("Let's Chat! " + username);
                windowChat.setScene(sceneParentFrame);
                windowChat.show();
            }
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
