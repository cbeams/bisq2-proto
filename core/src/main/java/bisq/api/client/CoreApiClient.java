package bisq.api.client;

public class CoreApiClient implements ApiClient {

    @Override
    public String getVersion() {
        return "43";
    }

    @Override
    public String getPrice() {
        return "$42.00";
    }
}
