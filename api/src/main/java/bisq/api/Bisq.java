package bisq.api;

import java.io.IOException;

public interface Bisq {

    String getPrice() throws IOException;

    OfferBook getOfferBook();
}
