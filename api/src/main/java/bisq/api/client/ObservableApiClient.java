package bisq.api.client;

import io.reactivex.rxjava3.core.Observable;

public interface ObservableApiClient {

    Observable<String> getPriceObservable();
}
