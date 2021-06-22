package bisq.app.cli;

import bisq.api.http.client.HttpApiClient;
import picocli.AutoComplete;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.*;

public class BisqCli {

    public static final int EXIT_OK = CommandLine.ExitCode.OK;
    public static final int EXIT_APP_ERROR = CommandLine.ExitCode.SOFTWARE;
    public static final int EXIT_USER_ERROR = CommandLine.ExitCode.USAGE;

    Console console = new SystemConsole();

    private final Bisq bisq = new Bisq();

    private final CommandLine commandLine =
            new CommandLine(bisq)
                    .addSubcommand(bisq.price)
                    .addSubcommand(bisq.offer);

    public static void main(String[] args) {
        System.exit(new BisqCli().run(args));
    }

    public int run(String... args) {
        return commandLine
                .setOut(new PrintWriter(console.getOut()))
                .setErr(new PrintWriter(console.getErr()))
                .execute(args);
    }


    @Command(name = Bisq.CMD_NAME)
    @SuppressWarnings("unused") // to avoid warnings on @Command methods below
    class Bisq {
        static final String CMD_NAME = "bisq";

        final HttpApiClient api = new HttpApiClient();

        final Price price = new Price();
        final Offer offer = new Offer();

        @Command(name = "price")
        class Price implements Runnable {
            @Override
            public void run() {
                console.outln(api.getPrice());
            }
        }

        @Command(name = "offer")
        class Offer {
            @Command(name = "create")
            public void create(
                    @Parameters(
                            paramLabel = "<json>",
                            description = "Offer json or '-' to read from stdin") String json) throws IOException {

                if ("-".equals(json))
                    json = new BufferedReader(new InputStreamReader(System.in)).readLine();
                console.outln(api.addOffer(json));
            }

            @Command(name = "list")
            public void list() {
                console.outln(api.getOffers());
            }

            @Command(name = "view")
            public void view(@Parameters(paramLabel = "<id>") int id) {
                console.outln(api.getOffer(id));
            }

            @Command(name = "delete")
            public void delete(
                    @Parameters(
                            paramLabel = "<id>",
                            description = "Offer id to delete or 'all' to delete all offers") String id) {
                if ("all".equals(id)) {
                    api.deleteAllOffers();
                    console.outln("deleted all offers");
                    return;
                }

                api.deleteOffer(Integer.parseInt(id));
                console.outln("deleted offer " + id);
            }
        }
    }


    // see :cli:generateBashCompletion in build.gradle
    public static class BashCompletionGenerator {
        public static void main(String[] args) throws IOException {
            AutoComplete.bash(Bisq.CMD_NAME, new File(args[0]), null, new BisqCli().commandLine);
        }
    }
}