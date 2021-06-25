package bisq.fx.app;

import bisq.api.client.BisqApiClient;
import bisq.core.BisqCore;
import bisq.core.app.BisqDaemon;
import bisq.core.service.api.rest.RestApiService;
import bisq.fx.offer.ObservableOfferBook;

import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BisqFX extends Application {

    @Override
    public void start(Stage stage) {

        String host = RestApiService.DEFAULT_HOST;
        int port = RestApiService.DEFAULT_PORT;
        if (!RestApiService.isRunningLocally(port)) {
            System.out.printf("No api service detected on port %d. Starting own.\n", port);
            new BisqDaemon(new RestApiService(new BisqCore(), port)).run();
        }

        var bisq = new BisqApiClient(host, port);
        var offerBook = bisq.getOfferBook();
        var observableOfferBook = new ObservableOfferBook(offerBook);
        var offerList = observableOfferBook.list();

        var offerListView = new ListView<String>();
        offerListView.itemsProperty().bind(new SimpleListProperty<>(offerList));
        offerListView.setPrefWidth(100);
        offerListView.setPrefHeight(170);

        var box = new javafx.scene.layout.VBox(offerListView);
        box.setAlignment(Pos.CENTER);
        var scene = new Scene(box, 640, 480);

        stage.setScene(scene);
        stage.show();

        // do a full REST API refresh of the offer book every so often in case a WebSocket API event was missed
        Executors.newSingleThreadScheduledExecutor().schedule(observableOfferBook::load, 15, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        launch();
    }
}
