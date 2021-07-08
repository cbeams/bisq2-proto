package bisq.api.offer;

public class Offer {
    private final String value;

    public Offer(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Offer { value: " + value + " }";
    }
}
