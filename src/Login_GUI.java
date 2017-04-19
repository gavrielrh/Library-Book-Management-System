
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Eric on 4/18/2017.
 */
public class Login_GUI {
    /**
     * Creates Login GUI and authenicate user login.
     * @return Login GUI Scene
     */
    public static Scene login(Stage stage, SystemInvoker sys, String ClientId){
        stage.setTitle("LBMS | Login");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Button back = new Button("Back");
        grid.add(back, 0,0);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(StartMenu_GUI.createGUI(stage, sys, ClientId));
            }
        });

        Text sceneTitle = new Text("Login");
        sceneTitle.setFont(Font.font("Tohama", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 1 , 2 , 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 2);
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        Label password = new Label("Password: ");
        grid.add(password, 0, 3);
        PasswordField passField = new PasswordField();
        grid.add(passField, 1, 3);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Text msg = new Text();
        grid.add(msg, 0, 5, 2, 1);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String errorMsg = "";
                String user = userTextField.getText();
                errorMsg = (user.length() == 0) ? errorMsg + "User Name " : errorMsg;
                String pass = passField.getText();
                errorMsg = (pass.length() == 0) ? errorMsg + "Password " : errorMsg;

                if (errorMsg.length() == 0){
                    String[] param = {user, pass};
                    String commandParam = String.join(",", param);
                    String resp = sys.handleCommand(ClientId + "," + commandParam + ";");
                    msg.setText(resp);

                } else {
                    msg.setFill(Color.RED);
                    msg.setText("Missing:  " + errorMsg);
                }
            }
        });

        Text client = new Text("Client ID: " + ClientId);
        HBox clientBox = new HBox();
        clientBox.setAlignment(Pos.BASELINE_RIGHT);
        clientBox.getChildren().add(client);
        grid.add(clientBox, 1,6);

        Scene scene = new Scene(grid, 400,400);
        return scene;
    }
}
