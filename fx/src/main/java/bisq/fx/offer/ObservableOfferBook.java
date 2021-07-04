package bisq.fx.offer;

import bisq.api.offer.OfferBook;
import bisq.util.event.Event;
import bisq.util.event.EventListener;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ObservableOfferBook implements EventListener<String> {

    private static final Logger log = LoggerFactory.getLogger(ObservableOfferBook.class);

    private final OfferBook offerBook;
    private final ObservableList<String> observableOffers = FXCollections.observableArrayList();

    public ObservableOfferBook(OfferBook offerBook) {
        this.offerBook = offerBook;
        this.offerBook.addEventListener(this);
    }

    public ObservableList<String> list() {
        load();
        return observableOffers;
    }

    @Override
    public void onEvent(Event<String> offerEvent) {
        switch (offerEvent.getType()) {
            case CREATION -> Platform.runLater(() -> observableOffers.add(offerEvent.getEntity()));
            case DELETION -> Platform.runLater(() -> observableOffers.remove(offerEvent.getEntity()));
            default -> throw new IllegalArgumentException("unsupported event type: " + offerEvent.getType());
        }
    }

    public void load() {
        try {
            List<String> offers = offerBook.findAll();
            Platform.runLater(() -> this.observableOffers.setAll(offers));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }
}
