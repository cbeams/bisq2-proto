package bisq.app.cli;

import bisq.api.client.ApiClient;
import bisq.api.client.OfferApi;
import bisq.api.http.client.HttpApiClient;
import picocli.CommandLine;

class BisqCommandFactory implements CommandLine.IFactory {

    private final ApiClient api = new HttpApiClient();

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (cls.equals(BisqCli.Bisq.class))
            return CommandLine.defaultFactory().create(cls);
        if (cls.equals(BisqCli.Bisq.Offer.class))
            return cls.getDeclaredConstructor(OfferApi.class).newInstance(api.getOfferApi());
        return cls.getDeclaredConstructor(ApiClient.class).newInstance(api);
    }
}
