package bisq.api.client;

import bisq.api.OfferBook;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class OfferBookApiClient implements OfferBook {

    private final OkHttpClient httpClient;
    private final String offerApiUrl;

    public OfferBookApiClient(OkHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.offerApiUrl = baseUrl + "/offer";
    }

    @Override
    public String list() throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(offerApiUrl)
                .build()).execute().body().string();
    }

    @Override
    public String view(int id) throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(offerApiUrl + "/" + id)
                .build()).execute().body().string();
    }

    @Override
    public String create(String json) throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(offerApiUrl)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build()).execute().body().string();
    }

    @Override
    public void delete(int id) throws IOException {
        httpClient.newCall(new Request.Builder()
                .url(offerApiUrl + "/" + id)
                .delete()
                .build()).execute();
    }

    @Override
    public void delete() throws IOException {
        httpClient.newCall(new Request.Builder()
                .url(offerApiUrl)
                .delete()
                .build()).execute();
    }
}
