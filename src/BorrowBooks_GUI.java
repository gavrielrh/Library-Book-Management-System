import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Eric on 4/18/2017.
 */
public class BorrowBooks_GUI {

    public static Scene VisitorBorrow(Stage stage){
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
