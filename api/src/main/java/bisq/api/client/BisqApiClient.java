package bisq.api.client;

import bisq.api.Bisq;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class BisqApiClient implements Bisq {

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS) // to keep any websocket connections alive
            .build();

    private final OfferBookApiClient offerBook;

    public BisqApiClient(String host, int port) {
        this.offerBook = new OfferBookApiClient(httpClient, host, port);
    }

    @Override
    public OfferBookApiClient getOfferBook() {
        return offerBook;
    }
}