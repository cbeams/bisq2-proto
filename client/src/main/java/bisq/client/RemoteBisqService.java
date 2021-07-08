package bisq.client;

import bisq.api.BisqService;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class RemoteBisqService implements AutoCloseable, BisqService {

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS) // to keep any websocket connections alive
            .build();

    private final RemoteOfferBook offerBook;

    public RemoteBisqService(String host, int port) {
        this.offerBook = new RemoteOfferBook(httpClient, host, port);
    }

    @Override
    public RemoteOfferBook getOfferBook() {
        return offerBook;
    }

    @Override
    public void close() {
        offerBook.close();
        httpClient.dispatcher().executorService().shutdown();
    }
}
