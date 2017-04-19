/**
 * Created by Eric on 4/2/2017.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class LBMS_GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("LBMS");

        SystemInvoker sys = new SystemInvoker(SystemInvoker.startUp());

        String ClientId = sys.handleCommand("connect;");
        primaryStage.setScene(StartMenu_GUI.createGUI(primaryStage, sys, ClientId));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                we.consume();
                sys.handleCommand(ClientId+",disconnect;");
                primaryStage.close();
                int numClients = Integer.parseInt(sys.handleCommand("currentclient;"));
                System.out.println(numClients);
                if (numClients == 0) {
                    sys.handleCommand("quit;");
                    System.out.println("Closed");
                    Platform.exit();
                }
            }
        });

    }

}
