package bisq.event;

public interface EventSource<E> {

    void addEventListener(EventListener<E> eventListener);
}
