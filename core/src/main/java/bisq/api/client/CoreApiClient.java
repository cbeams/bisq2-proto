package bisq.api.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import static java.lang.String.format;

public class CoreApiClient implements ApiClient {

    @Override
    public String getPrice() {
        return format("%d.00", new Random().nextInt(100_000));
    }

    @Override
    public String getOffers() throws IOException {
        throw new UnsupportedEncodingException();
    }

    @Override
    public String getOffer(int id) throws IOException {
        throw new UnsupportedEncodingException();
    }

    @Override
    public String addOffer(String json) throws IOException {
        throw new UnsupportedEncodingException();
    }

    @Override
    public void deleteOffer(int id) throws IOException {
        throw new UnsupportedEncodingException();
    }

    @Override
    public void deleteAllOffers() throws IOException {
        throw new UnsupportedEncodingException();
    }
}
