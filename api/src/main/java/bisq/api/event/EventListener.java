package bisq.api.event;

public interface EventListener<E> {
    void onEvent(Event<E> event);
}
