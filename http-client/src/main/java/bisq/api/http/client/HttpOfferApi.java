package bisq.api.http.client;

import bisq.api.client.OfferApi;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class HttpOfferApi implements OfferApi {

    private OkHttpClient client;
    private String offerUrl;

    public HttpOfferApi(OkHttpClient client, String baseUrl) {
        this.client = client;
        this.offerUrl = baseUrl + "/offer";
    }

    @Override
    public String getOffers() throws IOException {
        return client.newCall(new Request.Builder()
                .url(offerUrl)
                .build()).execute().body().string();
    }

    @Override
    public String getOffer(int id) throws IOException {
        return client.newCall(new Request.Builder()
                .url(offerUrl + "/" + id)
                .build()).execute().body().string();
    }

    @Override
    public String addOffer(String json) throws IOException {
        return client.newCall(new Request.Builder()
                .url(offerUrl)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build()).execute().body().string();
    }

    @Override
    public void deleteOffer(int id) throws IOException {
        client.newCall(new Request.Builder()
                .url(offerUrl + "/" + id)
                .delete()
                .build()).execute();
    }

    @Override
    public void deleteAllOffers() throws IOException {
        client.newCall(new Request.Builder()
                .url(offerUrl)
                .delete()
                .build()).execute();
    }
}
