package bisq.api.client;

import bisq.api.BisqClient;
import bisq.api.OfferBook;

import java.util.Random;

import static java.lang.String.format;

public class CoreBisqClient implements BisqClient {

    @Override
    public String getPrice() {
        return format("%d.00", new Random().nextInt(100_000));
    }

    @Override
    public OfferBook getOfferBook() {
        throw new UnsupportedOperationException();
    }
}
