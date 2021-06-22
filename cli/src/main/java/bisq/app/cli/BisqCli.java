package bisq.app.cli;

import bisq.api.http.client.HttpApiClient;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.*;

public class BisqCli {

    // exit statuses
    static final int EXIT_OK = CommandLine.ExitCode.OK;
    static final int EXIT_APP_ERROR = CommandLine.ExitCode.SOFTWARE;
    static final int EXIT_USER_ERROR = CommandLine.ExitCode.USAGE;

    // command and subcommand names
    static final String bisq = "bisq";
    static final String price = "price";
    static final String offer = "offer";
    static final String create = "create";
    static final String list = "list";
    static final String view = "view";
    static final String delete = "delete";

    // non-final for testing console output
    static PrintStream out = System.out;
    static PrintStream err = System.err;

    public static void main(String[] args) {
        System.exit(bisq(args));
    }

    public static int bisq(String... args) {
        return new CommandLine(Bisq.class)
                .setOut(new PrintWriter(out))
                .setErr(new PrintWriter(err))
                .execute(args);
    }


    @Command(name = bisq,
            subcommands = {
                    Bisq.Price.class,
                    Bisq.Offer.class
            })
    @SuppressWarnings("unused") // to avoid warnings on @Command methods below
    static class Bisq {

        private static final HttpApiClient api = new HttpApiClient();

        @Command(name = price)
        static class Price implements Runnable {
            @Override
            public void run() {
                out.println(api.getPrice());
            }
        }

        @Command(name = offer)
        static class Offer {
            @Command(name = create)
            public void create(
                    @Parameters(
                            paramLabel = "<json>",
                            description = "Offer json or '-' to read from stdin") String json) throws IOException {

                if ("-".equals(json))
                    json = new BufferedReader(new InputStreamReader(System.in)).readLine();
                out.println(api.addOffer(json));
            }

            @Command(name = list)
            public void list() {
                out.println(api.getOffers());
            }

            @Command(name = view)
            public void view(@Parameters(paramLabel = "<id>") int id) {
                out.println(api.getOffer(id));
            }

            @Command(name = delete)
            public void delete(
                    @Parameters(
                            paramLabel = "<id>",
                            description = "Offer id to delete or 'all' to delete all offers") String id) {
                if ("all".equals(id)) {
                    api.deleteAllOffers();
                    out.println("deleted all offers");
                    return;
                }

                api.deleteOffer(Integer.parseInt(id));
                out.println("deleted offer " + id);
            }
        }
    }
}