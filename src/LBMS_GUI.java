/**
 * Created by Eric on 4/2/2017.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class LBMS_GUI extends Application {
    private Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("LBMS");

        primaryStage.setScene(login());
        primaryStage.show();
    }

    public Scene login(){
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
                stage.setScene(VisitorAccount());
            }
        });



        Scene scene = new Scene(grid, 300,275);
        return scene;
    }



    public Scene VisitorAccount(){
        BorderPane pane = new BorderPane();

        HBox borrowBooks = new HBox();
        borrowBooks.setPadding(new Insets(20, 20, 0, 20));
        borrowBooks.setAlignment(Pos.BASELINE_RIGHT);
        Button borrowBook = new Button("Borrow Books");
        borrowBooks.getChildren().add(borrowBook);

        borrowBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setScene(VisitorBorrow());
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
        fines.getChildren().addAll(fine, pay);

        pane.setTop(borrowBooks);
        pane.setCenter(borrow);
        pane.setBottom(fines);
        Scene scene = new Scene(pane, 500,500);
        return scene;
    }

    public Scene VisitorBorrow(){
        BorderPane pane = new BorderPane();
        //search
        HBox search = new HBox();
        search.setSpacing(10);
        search.setPadding(new Insets(20,20,20,20));
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Type 1",
                        "Type 2",
                        "Type 3"
                );
        ComboBox<String> searchType = new ComboBox<>(options);
        TextField searchInput = new TextField("search");
        Button searchBtn = new Button("Search");
        search.getChildren().addAll(searchType, searchInput, searchBtn);

        //results
        VBox results = new VBox();
        results.setPadding(new Insets(20,20,20,20));
        results.setPrefHeight(200);
        Text resultText = new Text("Results");
        ListView<String> result = new ListView<>();
        ObservableList<String> r = FXCollections.observableArrayList();
        r.add("result 1");
        r.add("result 2");
        r.add("result 3");
        result.setItems(r);
        results.getChildren().addAll(resultText, result);

        //checkout
        VBox checkout = new VBox();
        checkout.setPadding(new Insets(20,20,20,20));
        checkout.setPrefHeight(200);
        Text booklist = new Text("Books to checkout");
        ListView<String> books = new ListView<>();
        ObservableList<String> b = FXCollections.observableArrayList();
        b.add("book 1");
        b.add("book 2");
        b.add("book 3");
        books.setItems(b);
        Button checkoutBtn = new Button("Checkout");

        checkout.getChildren().addAll(booklist, books, checkoutBtn);
        pane.setTop(search);
        pane.setCenter(results);
        pane.setBottom(checkout);
        Scene scene = new Scene(pane, 500, 500);
        return scene;
    }
}
