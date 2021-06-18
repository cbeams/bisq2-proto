package bisq.app.desktop;

import bisq.api.http.client.HttpApiClient;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BisqDesktop extends Application {

    @Override
    public void start(Stage stage) {

        class BisqClient {

            private final HttpApiClient httpApiClient;

            public BisqClient() {
                httpApiClient = new HttpApiClient("localhost", 9999);
            }

            public Observable<String> getObservablePrice() {
                Consumer<Emitter<String>> c = emitter -> {
                    // this is where we would make the REST or WebSocket or gRPC API call.
                    emitter.onNext(httpApiClient.getPrice());
                    //
                    Thread.sleep(1000);
                };
                return Observable.generate(c);
            }
        }

        BisqClient bisqClient = new BisqClient();
        Observable<String> observablePrice = bisqClient.getObservablePrice();

        StringProperty number = new SimpleStringProperty("0.00");
        Label label = new Label();
        label.textProperty().bind(number);
        VBox box = new javafx.scene.layout.VBox(label);
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box, 640, 480);
        stage.setScene(scene);
        stage.show();

        Thread t = new Thread(() -> observablePrice.subscribe(i -> Platform.runLater(() -> number.set(i))));
        t.setDaemon(true);
        t.start();
    }

    public static void main(String[] args) {
        launch();
    }
}