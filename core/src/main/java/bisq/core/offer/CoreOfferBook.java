package bisq.core.offer;

import bisq.api.offer.OfferBook;
import bisq.util.event.Event;
import bisq.util.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static bisq.util.event.Event.Type.CREATION;
import static bisq.util.event.Event.Type.DELETION;

public class CoreOfferBook implements OfferBook {

    private static final Logger log = LoggerFactory.getLogger(CoreOfferBook.class);

    private final List<String> offers = new ArrayList<>();
    private final List<EventListener<String>> eventListeners = new ArrayList<>();

    @Override
    public void addEventListener(EventListener<String> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public List<String> findAll() {
        return offers;
    }

    @Override
    public String findById(int id) {
        return offers.get(id);
    }

    @Override
    public String save(String offer) {
        log.debug("saving offer {}", offer);
        offers.add(offer);
        notifyListeners(new Event<>(offer, CREATION));
        return offer;
    }

    @Override
    public void delete(int id) {
        var offer = offers.remove(id);
        notifyListeners(new Event<>(offer, DELETION));
    }

    @Override
    public void deleteAll() {
        offers.forEach(offer -> notifyListeners(new Event<>(offer, DELETION)));
        offers.clear();
    }

    private void notifyListeners(Event<String> event) {
        eventListeners.forEach(eventListener -> eventListener.onEvent(event));
    }
}
