package bisq.core;

import bisq.Bisq;
import bisq.offer.OfferBook;
import bisq.core.offer.CoreOfferBook;

public class BisqCore implements Bisq {

    private final OfferBook offerBook = new CoreOfferBook();

    @Override
    public OfferBook getOfferBook() {
        return offerBook;
    }
}
