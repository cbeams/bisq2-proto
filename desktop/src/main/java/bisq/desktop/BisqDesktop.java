package bisq.desktop;

import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class BisqDesktop extends Application {

    @Override
    public void start(Stage stage) {

        class BisqClient {

            public int getPrice() {
                return 0;
            }

            public Observable<Integer> getObservablePrice() {
                Consumer<Emitter<Integer>> c = emitter -> {
                    // this is where we would make the REST or WebSocket or gRPC API call.
                    emitter.onNext(new Random().nextInt());
                    //
                    Thread.sleep(1000);
                };
                return Observable.generate(c);
            }
        }

        BisqClient bisqClient = new BisqClient();
        int price = bisqClient.getPrice();
        Observable<Integer> observablePrice = bisqClient.getObservablePrice();

        IntegerProperty number = new SimpleIntegerProperty(39233);
        Label label = new Label();
        label.textProperty().bind(number.asString());
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