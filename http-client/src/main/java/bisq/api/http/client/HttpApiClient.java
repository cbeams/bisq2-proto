package bisq.api.http.client;

import bisq.api.client.ApiClient;
import bisq.api.client.OfferApi;
import okhttp3.*;

import java.io.IOException;

public class HttpApiClient implements ApiClient {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 2140;

    private final OkHttpClient client = new OkHttpClient();

    private final String baseUrl;
    private final OfferApi offerApi;

    public HttpApiClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public HttpApiClient(String host, int port) {
        this.baseUrl = String.format("http://%s:%s", host, port);
        this.offerApi = new HttpOfferApi(client, baseUrl);
    }

    @Override
    public String getPrice() throws IOException {
        return client.newCall(new Request.Builder()
                .url(baseUrl + "/price")
                .build()).execute().body().string();
    }

    @Override
    public OfferApi getOfferApi() {
        return offerApi;
    }
}
