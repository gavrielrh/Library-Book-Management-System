
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class RegisterNewVisitor_GUI {
    public static Scene registerNewVisitor(Stage stage, SystemInvoker sys, String ClientId){
        stage.setTitle("LBMS | Register New Visitor");
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
        Text sceneTitle = new Text("Register New Visitor");
        sceneTitle.setFont(Font.font("Tohama", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 1 , 2 , 1);

        Label FirstName = new Label("First Name:");
        grid.add(FirstName, 0, 2);
        TextField FirstNameField = new TextField();
        grid.add(FirstNameField, 1, 2);

        Label LastName = new Label("Last Name: ");
        grid.add(LastName, 0, 3);
        TextField LastNameField = new TextField();
        grid.add(LastNameField, 1, 3);

        Label Address = new Label("Address: ");
        grid.add(Address, 0, 4);
        TextField AddressField = new TextField();
        grid.add(AddressField, 1, 4);

        Label PhoneNum = new Label("Phone Number: ");
        grid.add(PhoneNum, 0, 5);
        TextField PhoneNumField = new TextField();
        grid.add(PhoneNumField, 1, 5);

        //Button that allows the user to register after running the register command and all
        //information is acceptable. Creates an account.
        Button btn = new Button("Register");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        Text msg = new Text("");
        grid.add(msg, 0, 7, 3,1);

        Text client = new Text("Client ID: " + ClientId);
        HBox clientBox = new HBox();
        clientBox.setAlignment(Pos.BASELINE_RIGHT);
        clientBox.getChildren().add(client);
        grid.add(clientBox, 1,8);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String errorMsg = "";
                String fname = FirstNameField.getText();
                errorMsg = (fname.length() == 0) ? errorMsg + "First Name " : errorMsg;
                String lname = LastNameField.getText();
                errorMsg = (lname.length() == 0) ? errorMsg + "Last Name " : errorMsg;
                String address = AddressField.getText();
                errorMsg = (address.length() == 0) ? errorMsg + "Address " : errorMsg;
                String phonenum = PhoneNumField.getText();
                errorMsg = (phonenum.length() == 0) ? errorMsg + "Phone Number " : errorMsg;

                if (errorMsg.length() == 0){
                    String[] param = {fname, lname, address, phonenum};
                    String commandParam = String.join(",", param);
                    String resp = sys.handleCommand("register,"+commandParam+";");
                    msg.setText("Register Success. VisitorID: "+ resp);

                } else {
                    msg.setFill(Color.RED);
                    msg.setText("Missing:  " + errorMsg);
                }
            }
        });


        Scene scene = new Scene(grid, 400,400, Color.DARKCYAN);
        return scene;
    }


}
