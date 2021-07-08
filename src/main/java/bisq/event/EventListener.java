package bisq.event;

public interface EventListener<E> {
    void onEvent(Event<E> event);
}
