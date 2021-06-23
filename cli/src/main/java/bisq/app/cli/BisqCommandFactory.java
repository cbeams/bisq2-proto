package bisq.app.cli;

import bisq.api.http.client.HttpApiClient;
import picocli.CommandLine;

class BisqCommandFactory implements CommandLine.IFactory {

    private final HttpApiClient api = new HttpApiClient();

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (cls.equals(BisqCli.Bisq.class))
            return CommandLine.defaultFactory().create(cls);
        return cls.getDeclaredConstructor(HttpApiClient.class).newInstance(api);
    }
}
