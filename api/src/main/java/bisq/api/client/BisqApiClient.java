package bisq.api.client;

import bisq.api.Bisq;
import bisq.api.OfferBook;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public class BisqApiClient implements Bisq {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 2140;

    private final OkHttpClient httpClient = new OkHttpClient();

    private final String restApiBaseUrl;
    private final OfferBook offerBook;

    public BisqApiClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public BisqApiClient(String host, int port) {
        this.restApiBaseUrl = String.format("http://%s:%s", host, port);
        this.offerBook = new OfferBookApiClient(httpClient, restApiBaseUrl);
    }

    @Override
    public String getPrice() throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(restApiBaseUrl + "/price")
                .build()).execute().body().string();
    }

    @Override
    public OfferBook getOfferBook() {
        return offerBook;
    }
}
