package bisq.app.desktop;

import bisq.api.http.client.HttpApiClient;
import bisq.api.http.client.ObservableHttpApiClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BisqDesktop extends Application {

    @Override
    public void start(Stage stage) {

        var client = new ObservableHttpApiClient(new HttpApiClient());
        var price = client.getPriceObservable();

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