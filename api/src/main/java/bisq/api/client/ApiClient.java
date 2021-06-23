package bisq.api.client;

import java.io.IOException;

public interface ApiClient {

    String getPrice() throws IOException;

    OfferApi getOfferApi();
}
