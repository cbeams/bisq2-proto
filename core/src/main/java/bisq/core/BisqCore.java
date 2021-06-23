package bisq.core;

import bisq.api.Bisq;
import bisq.api.OfferBook;

import java.util.Random;

import static java.lang.String.format;

public class BisqCore implements Bisq {

    @Override
    public String getPrice() {
        return format("%d.00", new Random().nextInt(100_000));
    }

    @Override
    public OfferBook getOfferBook() {
        throw new UnsupportedOperationException();
    }
}
