package bisq.client.http;

import bisq.api.OfferBook;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class HttpOfferBook implements OfferBook {

    private final OkHttpClient httpClient;
    private final String offerUrl;

    public HttpOfferBook(OkHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.offerUrl = baseUrl + "/offer";
    }

    @Override
    public String list() throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(offerUrl)
                .build()).execute().body().string();
    }

    @Override
    public String view(int id) throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(offerUrl + "/" + id)
                .build()).execute().body().string();
    }

    @Override
    public String create(String json) throws IOException {
        return httpClient.newCall(new Request.Builder()
                .url(offerUrl)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build()).execute().body().string();
    }

    @Override
    public void delete(int id) throws IOException {
        httpClient.newCall(new Request.Builder()
                .url(offerUrl + "/" + id)
                .delete()
                .build()).execute();
    }

    @Override
    public void delete() throws IOException {
        httpClient.newCall(new Request.Builder()
                .url(offerUrl)
                .delete()
                .build()).execute();
    }
}
