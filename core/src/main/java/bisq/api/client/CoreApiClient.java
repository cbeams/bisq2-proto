package bisq.api.client;

public class CoreApiClient implements ApiClient {

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String getPrice() {
        return "$42.00";
    }
}
