package bisq.api.http.client;

import bisq.api.client.ApiClient;
import okhttp3.*;

import java.io.IOException;

public class HttpApiClient implements ApiClient {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 2140;

    private final OkHttpClient client = new OkHttpClient();

    private final String baseUrl;

    public HttpApiClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public HttpApiClient(String host, int port) {
        this.baseUrl = String.format("http://%s:%s", host, port);
    }

    @Override
    public String getPrice() throws IOException {
        return client.newCall(new Request.Builder()
                .url(baseUrl + "/price")
                .build()).execute().body().string();
    }

    @Override
    public String getOffers() throws IOException {
        return client.newCall(new Request.Builder()
                .url(baseUrl + "/offer")
                .build()).execute().body().string();
    }

    @Override
    public String getOffer(int id) throws IOException {
        return client.newCall(new Request.Builder()
                .url(baseUrl + "/offer/" + id)
                .build()).execute().body().string();
    }

    @Override
    public String addOffer(String json) throws IOException {
        return client.newCall(new Request.Builder()
                .url(baseUrl + "/offer")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build()).execute().body().string();
    }

    @Override
    public void deleteOffer(int id) throws IOException {
        client.newCall(new Request.Builder()
                .url(baseUrl + "/offer/" + id)
                .delete()
                .build()).execute();
    }

    @Override
    public void deleteAllOffers() throws IOException {
        client.newCall(new Request.Builder()
                .url(baseUrl + "/offer")
                .delete()
                .build()).execute();
    }
}
