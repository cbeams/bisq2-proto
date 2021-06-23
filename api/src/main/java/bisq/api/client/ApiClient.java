package bisq.api.client;

import java.io.IOException;

public interface ApiClient {

    String getPrice() throws IOException;

    String getOffers() throws IOException;

    String getOffer(int id) throws IOException;

    String addOffer(String json) throws IOException;

    void deleteOffer(int id) throws IOException;

    void deleteAllOffers() throws IOException;
}
