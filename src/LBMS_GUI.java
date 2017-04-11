/**
 * Created by Eric on 4/2/2017.
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


public class LBMS_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LBMS");

        primaryStage.setScene(login());
        primaryStage.show();
    }

    public static Scene login(){
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

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //handle login
            }
        });



        Scene scene = new Scene(grid, 300,275);
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
