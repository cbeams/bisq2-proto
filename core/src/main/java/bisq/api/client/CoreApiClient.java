package bisq.api.client;

import java.util.Random;

import static java.lang.String.format;

public class CoreApiClient implements ApiClient {

    @Override
    public String getPrice() {
        return format("%d.00", new Random().nextInt(100_000));
    }
}
