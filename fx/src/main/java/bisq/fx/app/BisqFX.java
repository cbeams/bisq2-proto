package bisq.fx.app;

import bisq.api.client.BisqApiClient;
import bisq.api.rx.RxBisq;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BisqFX extends Application {

    @Override
    public void start(Stage stage) {

        var bisqClient = new RxBisq(new BisqApiClient());
        var price = bisqClient.getPriceObservable();

        var priceLabel = new Label();
        var priceProperty = new SimpleStringProperty();
        priceLabel.textProperty().bind(priceProperty);

        var priceBox = new javafx.scene.layout.VBox(priceLabel);
        priceBox.setAlignment(Pos.CENTER);
        var scene = new Scene(priceBox, 640, 480);

        stage.setScene(scene);
        stage.show();

        var priceThread = new Thread(() -> price.subscribe(i -> Platform.runLater(() -> priceProperty.set(i))));
        priceThread.setDaemon(true);
        priceThread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}