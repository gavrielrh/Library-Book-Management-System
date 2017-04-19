import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Eric on 4/18/2017.
 */
public class VisitorAccount_GUI {
    public static Scene visitorAccount(Stage stage, SystemInvoker sys, String ClientId){
        BorderPane pane = new BorderPane();

        HBox borrowBooks = new HBox();
        borrowBooks.setPadding(new Insets(20, 20, 0, 20));
        borrowBooks.setAlignment(Pos.BASELINE_RIGHT);
        Button borrowBook = new Button("Borrow Books");
        borrowBooks.getChildren().add(borrowBook);

        borrowBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setScene(BorrowBooks_GUI.VisitorBorrow(stage));
            }
        });

        //Borrowed Books List
        BorderPane borrow = new BorderPane();
        borrow.setPadding(new Insets(20,20,20,20));
        Text booksBorrowed = new Text("Books Borrowed");
        ListView<String> books = new ListView<>();
        ObservableList<String> b = FXCollections.observableArrayList();
        b.add("book 1");
        b.add("book 2");
        b.add("book 3");
        books.setItems(b);
        books.setPrefWidth(450);
        books.setPrefHeight(350);
        Button bookReturn = new Button("Return Book");
        borrow.setTop(booksBorrowed);
        borrow.setCenter(books);
        borrow.setBottom(bookReturn);

        //

        VBox fines = new VBox();
        fines.setPadding(new Insets(0,20,20,20));
        Text fine = new Text("Fines: S" + 5.00);
        Button pay = new Button("Pay Fine");
        HBox client = new HBox();
        client.setPadding(new Insets(20,20,20,20));
        client.setAlignment(Pos.BASELINE_RIGHT);
        Text clientId = new Text("ClientID: " + ClientId);
        client.getChildren().addAll(clientId);

        fines.getChildren().addAll(fine, pay, client);

        pane.setTop(borrowBooks);
        pane.setCenter(borrow);
        pane.setBottom(fines);
        Scene scene = new Scene(pane, 400,400);
        return scene;
    }
}
