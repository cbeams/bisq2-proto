package bisq.cli.app;

import bisq.api.client.BisqApiClient;
import bisq.api.offer.OfferBook;
import bisq.app.BisqApp;

import com.google.gson.Gson;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

import static java.lang.String.format;

public class BisqCLI implements BisqApp {

    // exit statuses
    static final int EXIT_OK = CommandLine.ExitCode.OK;
    static final int EXIT_APP_ERROR = CommandLine.ExitCode.SOFTWARE;
    static final int EXIT_USER_ERROR = CommandLine.ExitCode.USAGE;

    // command and subcommand names
    static final String bisq = "bisq";
    static final String offer = "offer";
    static final String create = "create";
    static final String list = "list";
    static final String view = "view";
    static final String delete = "delete";

    // option names
    static final String hostOpt = "--host";
    static final String portOpt = "--port";
    static final String debugOpt = "--debug";

    // mutable for testing
    static PrintStream out = System.out;
    static PrintStream err = System.err;

    public static void main(String[] args) {
        System.exit(bisq(args));
    }

    public static int bisq(String... args) {

        return new CommandLine(BisqCommand.class)
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
            versionProvider = VersionProvider.class,
            subcommands = {
                    BisqCommand.OfferSubcommand.class
            })
    @SuppressWarnings("unused") // to avoid warnings on @Command methods below
    static class BisqCommand {

        @Option(names = { "-v", "--version" },
                versionHelp = true,
                description = "Print version information and exit")
        boolean versionRequested;

        @Option(names = debugOpt, description = "Print stack trace when execution errors occur")
        boolean debug = false;

        @Option(names = hostOpt, paramLabel = "<host>", description = "api host")
        String host = BisqApiClient.DEFAULT_HOST;

        @Option(names = portOpt, paramLabel = "<n>", description = "api port")
        int port = BisqApiClient.DEFAULT_PORT;

        @Command(name = offer)
        static class OfferSubcommand {

            @ParentCommand
            private BisqCommand bisqCommand;

            private final Gson gson = new Gson();

            public OfferBook offerBook() {
                return new BisqApiClient(bisqCommand.host, bisqCommand.port).getOfferBook();
            }

            @Command(name = create)
            public void create(
                    @Parameters(
                            paramLabel = "<json>",
                            description = "Offer json or '-' to read from stdin") String json) throws IOException {

                if ("-".equals(json))
                    json = new BufferedReader(new InputStreamReader(System.in)).readLine();

                out.println(offerBook().save(gson.fromJson(json, String.class)));
            }

            @Command(name = list)
            public void list() throws IOException {
                out.println(offerBook().findAll());
            }

            @Command(name = view)
            public void view(@Parameters(paramLabel = "<id>") int id) throws IOException {
                out.println(offerBook().findById(id));
            }

            @Command(name = delete)
            public void delete(
                    @Parameters(
                            paramLabel = "<id>",
                            description = "Offer id to delete or 'all' to delete all offers") String id)
                    throws IOException {

                if ("all".equals(id)) {
                    offerBook().deleteAll();
                    out.println("deleted all offers");
                    return;
                }

                offerBook().delete(Integer.parseInt(id));
                out.println("deleted offer " + id);
            }
        }
    }


    static class VersionProvider implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            return new String[] {
                    format("%s version %s", BisqApp.APP_INFO.getName(), BisqApp.APP_INFO.getVersion())
            };
        }
    }
}
