package bisq.app.cli;

import bisq.api.client.ApiClient;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.util.concurrent.Callable;

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

    // option names
    static final String debugOpt = "--debug";

    // mutable for testing
    static PrintStream out = System.out;
    static PrintStream err = System.err;

    public static void main(String[] args) {
        System.exit(bisq(args));
    }

    public static int bisq(String... args) {
        return new CommandLine(Bisq.class, new BisqCommandFactory())
                .setOut(new PrintWriter(out))
                .setErr(new PrintWriter(err))
                .setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
                    err.println("error: " + ex.getMessage());
                    if (parseResult.hasMatchedOption(debugOpt)) {
                        throw ex;
                    }
                    return EXIT_APP_ERROR;
                })
                .execute(args);
    }


    @Command(name = bisq,
            subcommands = {
                    Bisq.Price.class,
                    Bisq.Offer.class
            })
    @SuppressWarnings("unused") // to avoid warnings on @Command methods below
    static class Bisq {

        @Option(names = debugOpt, description = "Print stack trace when execution errors occur")
        boolean debug = false;

        @Command(name = price)
        static class Price implements Callable<Integer> {

            private final ApiClient api;

            public Price(ApiClient api) {
                this.api = api;
            }

            @Override
            public Integer call() throws IOException {
                out.println(api.getPrice());
                return EXIT_OK;
            }
        }

        @Command(name = offer)
        static class Offer {

            private final ApiClient api;

            public Offer(ApiClient api) {
                this.api = api;
            }

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
            public void list() throws IOException {
                out.println(api.getOffers());
            }

            @Command(name = view)
            public void view(@Parameters(paramLabel = "<id>") int id) throws IOException {
                out.println(api.getOffer(id));
            }

            @Command(name = delete)
            public void delete(
                    @Parameters(
                            paramLabel = "<id>",
                            description = "Offer id to delete or 'all' to delete all offers") String id)
                    throws IOException {

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