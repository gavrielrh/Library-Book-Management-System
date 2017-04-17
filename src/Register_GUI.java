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
public class Register_GUI extends Application {

    /**
     * Calls the stage and scene with text boxes and buttons for a user
     * to register or go to the login GUI
     * @param args user input
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LBMS");

        primaryStage.setScene(register());
        primaryStage.show();
    }

    /**
     * creates the scene for registering a user
     * @return
     */
    public static Scene register(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Text sceneTitle = new Text("LBMS");
        sceneTitle.setFont(Font.font("Tohama", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0 , 2 , 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label password = new Label("Password: ");
        grid.add(password, 0, 2);
        PasswordField passField = new PasswordField();
        grid.add(passField, 1, 2);

        //Button that allows the user to register after running the register command and all
        //information is acceptable. Creates an account.
        Button btn = new Button("Register");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 4);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //TODO register visitor
            }
        });


        // opens the login GUI
        Button bt = new Button("Go To Login");
        HBox hbBt = new HBox(10);
        hbBt.setAlignment(Pos.BOTTOM_RIGHT);
        hbBt.getChildren().add(bt);
        grid.add(hbBt, 1, 7);


        final Text actiontarget2 = new Text();
        grid.add(actiontarget2, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //TODO opens the login GUI
            }
        });

        Scene scene = new Scene(grid, 300,275, Color.DARKCYAN);
        return scene;
    }

    public static Scene request(){
        BorderPane pane = new BorderPane();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10,10,10,10));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: red");

        Text command = new Text("Commands");
        TextField commandText = new TextField();
        commandText.setPrefWidth(500 - command.getWrappingWidth());

        hbox.getChildren().addAll(command, commandText);

        pane.setBottom(hbox);
        Scene scene = new Scene(pane, 500,500);
        return scene;
    }

}


