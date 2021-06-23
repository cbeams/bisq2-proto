package bisq.api.rx;

import bisq.api.Bisq;

import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class RxBisq {

    private final Bisq bisq;

    public RxBisq(Bisq bisq) {
        this.bisq = bisq;
    }

    public Observable<String> getPriceObservable() {
        Consumer<Emitter<String>> c = emitter -> {
            emitter.onNext(bisq.getPrice());
            Thread.sleep(1000);
        };
        return Observable.generate(c);
    }
}
