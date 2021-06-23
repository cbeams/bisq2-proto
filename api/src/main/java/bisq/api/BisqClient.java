package bisq.api;

import java.io.IOException;

public interface BisqClient {

    String getPrice() throws IOException;

    OfferBook getOfferBook();
}
