package bisq.cli.app;

import bisq.api.BisqClient;
import bisq.api.OfferBook;
import bisq.client.http.HttpBisqClient;
import picocli.CommandLine;

class BisqCommandFactory implements CommandLine.IFactory {

    private final BisqClient bisqClient = new HttpBisqClient();

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (cls.equals(BisqCommandLine.BisqCommand.class))
            return CommandLine.defaultFactory().create(cls);
        if (cls.equals(BisqCommandLine.BisqCommand.OfferSubcommand.class))
            return cls.getDeclaredConstructor(OfferBook.class).newInstance(bisqClient.getOfferBook());
        return cls.getDeclaredConstructor(BisqClient.class).newInstance(bisqClient);
    }
}
