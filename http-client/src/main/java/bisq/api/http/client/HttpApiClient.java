package bisq.api.http.client;

import bisq.api.client.ApiClient;
import okhttp3.*;

import java.io.IOException;
import java.io.UncheckedIOException;

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
    public String getPrice() {
        Request request = new Request.Builder()
                .url(baseUrl + "/price")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getOffers() {
        Request request = new Request.Builder()
                .url(baseUrl + "/offer")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getOffer(int id) {
        Request request = new Request.Builder()
                .url(baseUrl + "/offer/" + id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String addOffer(String json) {

        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(baseUrl + "/offer")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
