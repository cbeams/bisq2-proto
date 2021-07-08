package bisq.core;

import bisq.api.BisqService;
import bisq.api.offer.OfferBook;

import bisq.core.offer.CoreOfferBook;

public class CoreBisqService implements BisqService {

    private final OfferBook offerBook = new CoreOfferBook();

    @Override
    public OfferBook getOfferBook() {
        return offerBook;
    }
}
