package bisq.cli;

import bisq.api.client.BisqApiClient;
import bisq.api.offer.OfferBook;
import bisq.app.picocli.InitializableCommand;

import com.google.gson.Gson;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static bisq.app.BisqConsole.out;
import static bisq.cli.OfferCommand.*;

@Command(name = offer)
@SuppressWarnings("unused")
class OfferCommand implements InitializableCommand {

    public static final String offer = "offer";
    public static final String create = "create";
    public static final String list = "list";
    public static final String view = "view";
    public static final String delete = "delete";

    @ParentCommand
    private BisqCommand bisq;

    private OfferBook offerBook;

    private final Gson gson = new Gson();

    @Override
    public void init() {
        this.offerBook = new BisqApiClient(bisq.node.host(), bisq.node.port()).getOfferBook();
    }

    @Command(name = create)
    public void create(
            @CommandLine.Parameters(
                    paramLabel = "<json>",
                    description = "Offer json or '-' to read from stdin") String json) throws IOException {

        if ("-".equals(json))
            json = new BufferedReader(new InputStreamReader(System.in)).readLine();

        out.println(offerBook.save(gson.fromJson(json, String.class)));
    }

    @Command(name = list)
    public void list() throws IOException {
        out.println(offerBook.findAll());
    }

    @Command(name = view)
    public void view(@CommandLine.Parameters(paramLabel = "<id>") int id) throws IOException {
        out.println(offerBook.findById(id));
    }

    @Command(name = delete)
    public void delete(
            @CommandLine.Parameters(
                    paramLabel = "<id>",
                    description = "Offer id to delete or 'all' to delete all offers") String id)
            throws IOException {

        if ("all".equals(id)) {
            offerBook.deleteAll();
            out.println("deleted all offers");
            return;
        }

        offerBook.delete(Integer.parseInt(id));
        out.println("deleted offer " + id);
    }
}
