package bisq.api.http.client;

import bisq.api.client.ObservableApiClient;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class ObservableHttpApiClient implements ObservableApiClient {

    private final HttpApiClient httpApiClient;

    public ObservableHttpApiClient(HttpApiClient httpApiClient) {
        this.httpApiClient = httpApiClient;
    }

    @Override
    public Observable<String> getPriceObservable() {
        Consumer<Emitter<String>> c = emitter -> {
            emitter.onNext(httpApiClient.getPrice());
            Thread.sleep(1000);
        };
        return Observable.generate(c);
    }
}
