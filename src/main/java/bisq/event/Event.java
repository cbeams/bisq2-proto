package bisq.event;

public class Event<E> {

    public enum Type { CREATION, DELETION }

    private final E entity;
    private final Type type;

    public Event(E entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public E getEntity() {
        return entity;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Event{" +
                "entity=" + entity +
                ", type=" + type +
                '}';
    }
}
