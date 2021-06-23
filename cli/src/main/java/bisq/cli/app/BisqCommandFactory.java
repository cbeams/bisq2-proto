package bisq.cli.app;

import bisq.api.Bisq;
import bisq.api.OfferBook;
import bisq.api.client.BisqApiClient;
import picocli.CommandLine;

class BisqCommandFactory implements CommandLine.IFactory {

    private final Bisq bisq = new BisqApiClient();

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (cls.equals(BisqCommandLine.BisqCommand.class))
            return CommandLine.defaultFactory().create(cls);
        if (cls.equals(BisqCommandLine.BisqCommand.OfferSubcommand.class))
            return cls.getDeclaredConstructor(OfferBook.class).newInstance(bisq.getOfferBook());
        return cls.getDeclaredConstructor(Bisq.class).newInstance(bisq);
    }
}
