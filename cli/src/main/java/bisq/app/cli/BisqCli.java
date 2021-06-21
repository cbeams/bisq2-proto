package bisq.app.cli;

import bisq.api.http.client.HttpApiClient;
import picocli.AutoComplete;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

public class BisqCli {

    public static final int EXIT_OK = CommandLine.ExitCode.OK;
    public static final int EXIT_APP_ERROR = CommandLine.ExitCode.SOFTWARE;
    public static final int EXIT_USER_ERROR = CommandLine.ExitCode.USAGE;

    private Console console = new SystemConsole();

    private final HttpApiClient api = new HttpApiClient();
    private final Bisq bisq = new Bisq();
    private final CommandLine commandLine =
            new CommandLine(bisq)
                .addSubcommand(bisq.getprice)
                .addSubcommand(new CommandLine(bisq.offer)
                        .addSubcommand(bisq.offer.list));

    public int run(String... args) {
        return commandLine
                .setOut(new PrintWriter(console.getOut()))
                .setErr(new PrintWriter(console.getErr()))
                .execute(args);
    }

    void setConsole(Console console) {
        this.console = console;
    }

    public static void main(String[] args) {
        System.exit(new BisqCli().run(args));
    }


    // see :cli:generateBashCompletion in build.gradle
    public static class BashCompletionGenerator {
        public static void main(String[] args) throws IOException {
            AutoComplete.bash(Bisq.CMD_NAME, new File(args[0]), null, new BisqCli().commandLine);
        }
    }


    @Command(name = Bisq.CMD_NAME)
    class Bisq {
        static final String CMD_NAME = "bisq";

        final Offer offer = new Offer();
        final GetPrice getprice = new GetPrice();

        @Command(name = "getprice")
        class GetPrice implements Callable<Integer> {
            @Override
            public Integer call() {
                console.outln(api.getPrice());
                return EXIT_OK;
            }
        }

        @Command(name = "offer")
        class Offer {
            final List list = new List();

            @Command(name = "list")
            class List implements Callable<Integer> {
                @Override
                public Integer call() {
                    console.outln(api.getOffers());
                    return EXIT_OK;
                }
            }
        }
    }
}