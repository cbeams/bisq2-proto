package bisq.api.http.service;

import bisq.api.client.CoreApiClient;
import bisq.api.service.ApiService;
import com.google.gson.Gson;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class HttpApiService implements ApiService {

    public static final int DEFAULT_PORT = 2140;

    private final Gson gson = new Gson();
    private final List<String> offers = new ArrayList<>();

    private final CoreApiClient core;
    private final int port;

    public HttpApiService() {
        this.core = new CoreApiClient();
        this.port = DEFAULT_PORT;
    }

    @Override
    public void run() {
        Spark.port(port);
        System.out.println("listening on port " + port);

        exception(Exception.class, (ex, req, res) -> ex.printStackTrace(System.err));

        get("/price", (req, res) -> core.getPrice());

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

    @Override
    public void stop() {
        Spark.stop();
    }

    public static void main(String[] args) {
        new HttpApiService().run();
    }
}
