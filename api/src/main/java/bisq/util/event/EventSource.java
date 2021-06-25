package bisq.util.event;

public interface EventSource<E> {

    void addEventListener(EventListener<E> eventListener);
}
