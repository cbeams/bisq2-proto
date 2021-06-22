package bisq.app.cli;

import bisq.api.http.client.HttpApiClient;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.*;

public class BisqCli {

    public static final int EXIT_OK = CommandLine.ExitCode.OK;
    public static final int EXIT_APP_ERROR = CommandLine.ExitCode.SOFTWARE;
    public static final int EXIT_USER_ERROR = CommandLine.ExitCode.USAGE;

    static Console console = new SystemConsole();

    public static void main(String[] args) {
        System.exit(new BisqCli().run(args));
    }

    public int run(String... args) {
        return new CommandLine(Bisq.class)
                .setOut(new PrintWriter(console.getOut()))
                .setErr(new PrintWriter(console.getErr()))
                .execute(args);
    }


    @Command(name = "bisq",
            subcommands = {
                    Bisq.Price.class,
                    Bisq.Offer.class
            })
    @SuppressWarnings("unused") // to avoid warnings on @Command methods below
    static class Bisq {

        private static final HttpApiClient api = new HttpApiClient();

        @Command(name = "price")
        static class Price implements Runnable {
            @Override
            public void run() {
                console.outln(api.getPrice());
            }
        }

        @Command(name = "offer")
        static class Offer {
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
}