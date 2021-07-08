package bisq.app.fx;

import bisq.app.BisqApp;
import bisq.app.picocli.CommonOptions;
import bisq.app.picocli.InitializableCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

@Command(name = "bisqfx",
        mixinStandardHelpOptions = true,
        versionProvider = BisqApp.VersionProvider.class)
@SuppressWarnings("unused")
class BisqFXCommand implements Runnable, InitializableCommand {

    @Option(names = {"-v", CommonOptions.verboseOpt},
            description = "Enable verbose mode to diagnose problems")
    boolean verbose = false;

    @Option(names = {"-n", "--node"},
            showDefaultValue = ALWAYS)
    String node = "localhost:2140";

    String host;
    int port;

    @Override
    public void init() {
        // quick and dirty for a demo
        this.host = node.split(":")[0];
        this.port = Integer.parseInt(node.split(":")[1]);
    }

    public void run() {
        BisqFXApp.launchAppplication(this);
    }
}
