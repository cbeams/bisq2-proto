package bisq.client.http;

import bisq.api.BisqClient;
import bisq.api.OfferBook;
import okhttp3.*;

import java.io.IOException;

public class HttpBisqClient implements BisqClient {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 2140;

    private final OkHttpClient httpClient = new OkHttpClient();

    private final String baseUrl;
    private final OfferBook offerBook;

    public HttpBisqClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public HttpBisqClient(String host, int port) {
        this.baseUrl = String.format("http://%s:%s", host, port);
        this.offerBook = new HttpOfferBook(httpClient, baseUrl);
    }

    @Override
    public String getPrice() throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(baseUrl + "/price")
                .build()).execute().body().string();
    }

    @Override
    public OfferBook getOfferBook() {
        return offerBook;
    }
}
