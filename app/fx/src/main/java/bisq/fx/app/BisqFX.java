package bisq.fx.app;

import bisq.client.BisqApiClient;
import bisq.app.BisqApp;
import bisq.core.BisqCore;
import bisq.core.node.BisqNode;
import bisq.core.service.api.rest.RestApiService;
import bisq.fx.offer.ObservableOfferBook;

import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class BisqFX extends Application implements BisqApp {

    public static final Logger log = LoggerFactory.getLogger(BisqFX.class);

    static BisqFXCommand bisqfx;

    @Override
    public void start(Stage stage) {

        stage.setTitle(format("Bisq (%s)", bisqfx.node));

        final BisqNode bisqNode;
        if (bisqfx.host.equals("localhost") && !RestApiService.isRunningLocally(bisqfx.port)) {
            log.info("No api service detected on port {}. Starting own.", bisqfx.port);
            bisqNode = new BisqNode(new RestApiService(new BisqCore(), bisqfx.port));
            bisqNode.run();
        } else {
            bisqNode = null;
        }

        var bisq = new BisqApiClient(bisqfx.host, bisqfx.port);
        var offerBook = bisq.getOfferBook();
        var observableOfferBook = new ObservableOfferBook(offerBook);
        var offerList = observableOfferBook.list();

        var offerListView = new ListView<String>();
        offerListView.itemsProperty().bind(new SimpleListProperty<>(offerList));
        offerListView.setPrefWidth(100);
        offerListView.setPrefHeight(170);

        var box = new javafx.scene.layout.VBox(offerListView);
        box.setAlignment(Pos.CENTER);
        var scene = new Scene(box, 320, 240);

        stage.setScene(scene);
        stage.show();

        // do a full REST API refresh of the offer book every so often in case a WebSocket API event was missed
        var executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(observableOfferBook::load, 15, 15, TimeUnit.SECONDS);

        stage.setOnCloseRequest(windowEvent -> {
            bisq.close();
            if (bisqNode != null)
                bisqNode.stop();
            executor.shutdownNow();
        });
    }

    public static void launchAppplication(BisqFXCommand command) {
        bisqfx = command;
        launch();
    }

    public static void main(String[] args) {
        throw new UnsupportedOperationException(format("Run %s instead", BisqFXMain.class.getName()));
    }
}
