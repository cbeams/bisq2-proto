package bisq.api.rx;

import bisq.api.BisqClient;

import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class RxBisqClient {

    private final BisqClient bisqClient;

    public RxBisqClient(BisqClient bisqClient) {
        this.bisqClient = bisqClient;
    }

    public Observable<String> getPriceObservable() {
        Consumer<Emitter<String>> c = emitter -> {
            emitter.onNext(bisqClient.getPrice());
            Thread.sleep(1000);
        };
        return Observable.generate(c);
    }
}
