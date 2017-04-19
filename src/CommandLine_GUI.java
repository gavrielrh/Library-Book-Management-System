import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by Eric on 4/19/2017.
 */
public class CommandLine_GUI {
    public static Scene CommandLine(SystemInvoker sys, String ClientId){
        VBox cmd = new VBox();
        cmd.setSpacing(1);
        cmd.setPadding(new Insets(10,10,10,10));
        cmd.setAlignment(Pos.BOTTOM_LEFT);
        TextField request = new TextField();
        cmd.getChildren().addAll(request);

        request.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    String resp = sys.handleCommand(request.getText());
                    Text response = new Text(resp);
                    cmd.getChildren().add(cmd.getChildren().size()-1, response);
                    request.setText("");
                }
            }
        });
        Scene scene = new Scene(cmd, 500, 300);
        return scene;
    }
}
