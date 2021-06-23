package bisq.core.service.api.rest;

import bisq.api.Bisq;
import bisq.core.BisqCore;

import com.google.gson.Gson;
import spark.Spark;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class RestApiService {

    public static final int DEFAULT_PORT = 2140;
    public static final int RANDOM_PORT = 0;

    private final Gson gson = new Gson();
    private final List<String> offers = new ArrayList<>();

    private final Bisq bisq;
    private final int port;

    /**
     * Create a new {@link RestApiService} that will bind to port {@value DEFAULT_PORT}.
     */
    public RestApiService() {
        this(DEFAULT_PORT);
    }

    /**
     * @param port the port to bind to, or a random available port if set to {@value #RANDOM_PORT}.
     */
    public RestApiService(int port) {
        this.bisq = new BisqCore();

        if (port == RANDOM_PORT) {
            try (ServerSocket random = new ServerSocket(RANDOM_PORT)) {
                this.port = random.getLocalPort();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        else {
            this.port = port;
        }
    }

    public void start() {
        Spark.port(port);
        System.out.println("listening on port " + port);

        exception(Exception.class, (ex, req, res) -> ex.printStackTrace(System.err));

        get("/price", (req, res) -> bisq.getPrice());

        get("/offer", (req, res) -> {
            res.type("application/json");
            return offers;
        }, gson::toJson);

        get("/offer/:id", (req, res) -> {
            res.type("application/json");
            return offers.get(Integer.parseInt(req.params("id")) - 1);
        }, gson::toJson);

        post("/offer", (req, res) -> {
            var offer = gson.fromJson(req.body(), String.class);
            offers.add(offer);
            res.status(201);
            res.header("Location", "/offer/" + (offers.lastIndexOf(offer) + 1));

            res.type("application/json");
            return gson.toJson(offer);
        });

        delete("/offer", (req, res) -> {
            offers.clear();
            res.status(204);
            res.type("application/json");
            return "";
        });

        delete("/offer/:id", (req, res) -> {
            res.type("application/json");
            offers.remove(Integer.parseInt(req.params("id")) - 1);
            res.status(204);
            res.type("application/json");
            return "";
        }, gson::toJson);
    }

    public void stop() {
        Spark.stop();
    }

    public int getPort() {
        return this.port;
    }
}
