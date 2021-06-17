import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        IntegerProperty number = new SimpleIntegerProperty(39233);
        Label label = new Label();
        label.textProperty().bind(number.asString());
        Button button = new Button("get price");
        button.setOnMouseClicked(mouseEvent -> number.set(number.get() + 1));
        VBox box = new javafx.scene.layout.VBox(label, button);
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}