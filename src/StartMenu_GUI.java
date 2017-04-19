import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by Eric on 4/18/2017.
 */
public class StartMenu_GUI {

    public static Scene createGUI(Stage stage, SystemInvoker sys, String ClientId){
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20,80,20,80));

        //Title
        HBox top = new HBox();
        top.setPadding(new Insets(20,20,20,20));
        top.setSpacing(50);
        Text welcome = new Text("Welcome to the LBMS");
        welcome.setFont(Font.font("Bold",20));
        top.getChildren().addAll(welcome);

        //Register Visitor Button
        Button RegisterVisitor = new Button("Register New Visitor");
        RegisterVisitor.setPrefWidth(200);
        RegisterVisitor.setAlignment(Pos.CENTER);
        RegisterVisitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setScene(RegisterNewVisitor_GUI.registerNewVisitor(stage, sys, ClientId));
            }

        });

        //Login Button
        Button Login = new Button("Login");
        Login.setPrefWidth(200);
        Login.setAlignment(Pos.CENTER);
        Login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(Login_GUI.login(stage, sys, ClientId));
            }
        });

        //Create Account Button
        Button CreateAccount = new Button("Create Account");
        CreateAccount.setPrefWidth(200);
        CreateAccount.setAlignment(Pos.CENTER);
        CreateAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(Register_GUI.register(stage, sys, ClientId));
            }
        });

        //New Client Button
        Button StartNewClient = new Button("Start New Client");
        StartNewClient.setPrefWidth(200);
        StartNewClient.setAlignment(Pos.CENTER);
        StartNewClient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String id = sys.handleCommand("connect;");
                Stage newClient = new Stage();
                newClient.setScene(StartMenu_GUI.createGUI(newClient, sys, id));
                newClient.show();
                newClient.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        we.consume();
                        sys.handleCommand(id+",disconnect;");
                        newClient.close();
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
        });

        //add buttons to Vbox
        VBox options = new VBox();
        options.setPadding(new Insets(20,20,20,20));
        options.setSpacing(10);
        options.getChildren().addAll(RegisterVisitor,Login,CreateAccount, StartNewClient);

        //clientID display
        HBox client = new HBox();
        client.setPadding(new Insets(20,20,20,20));
        client.setAlignment(Pos.BASELINE_RIGHT);
        Text clientId = new Text("ClientID: " + ClientId);
        client.getChildren().addAll(clientId);

        //set positions for Nodes
        pane.setTop(top);
        pane.setCenter(options);
        pane.setBottom(client);
        return new Scene(pane, 400,400);
    }
}
