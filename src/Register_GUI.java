/**
 * Created by Lucas on 4/17/2017.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.net.www.ApplicationLaunchException;

/**
 * Graphical use interface for registering a user, also can bring the user to the login GUI
 */
public class Register_GUI{

    /**
     * creates the scene for registering a user
     * @return
     */
    public static Scene register(Stage stage, SystemInvoker sys, String ClientId){
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

        Text sceneTitle = new Text("LBMS");
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

        //Button that allows the user to register after running the register command and all
        //information is acceptable. Creates an account.
        Button btn = new Button("Register");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 4,2,1);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String visitorID = userTextField.getText();
                String resp = sys.handleCommand(ClientId + ",create,user,password,visitor,"+visitorID+";");
                actiontarget.setText(resp);
            }
        });

        //Client Id
        Text client = new Text("Client ID: " + ClientId);
        HBox clientBox = new HBox();
        clientBox.setAlignment(Pos.BASELINE_RIGHT);
        clientBox.getChildren().add(client);
        grid.add(clientBox, 1,6);

        Scene scene = new Scene(grid, 400,400, Color.DARKCYAN);
        return scene;
    }

}


