package bisq.core.service.api.rest;

import bisq.api.offer.OfferBook;
import bisq.core.BisqCore;
import bisq.util.event.Event;
import bisq.util.event.EventListener;

import com.google.gson.Gson;

import spark.Spark;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static spark.Spark.*;

public class RestApiService implements EventListener<String> {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 2140;
    public static final int RANDOM_PORT = 0;

    private final Gson gson = new Gson();
    private final OfferEventsWebSocket offerEventsWebSocket = new OfferEventsWebSocket();

    private final OfferBook offerBook;
    private final int port;

    /**
     * @param port the port to bind to, or a random available port if set to {@value #RANDOM_PORT}.
     */
    public RestApiService(BisqCore bisq, int port) {
        this.offerBook = bisq.getOfferBook();

        this.offerBook.addEventListener(this);

        if (port == RANDOM_PORT) {
            try (ServerSocket random = new ServerSocket(RANDOM_PORT)) {
                this.port = random.getLocalPort();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            this.port = port;
        }
    }

    public void start() {
        Spark.port(port);
        System.out.println("listening on port " + port);

        exception(Exception.class, (ex, req, res) -> ex.printStackTrace(System.err));

        webSocket("/offerevents", offerEventsWebSocket);
        init();

        get("/offer", (req, res) -> {
            res.type("application/json");
            return offerBook.findAll();
        }, gson::toJson);

        get("/offer/:id", (req, res) -> {
            res.type("application/json");
            return offerBook.findById(Integer.parseInt(req.params("id")) - 1);
        }, gson::toJson);

        post("/offer", (req, res) -> {
            var offer = gson.fromJson(req.body(), String.class);
            offerBook.save(offer);
            res.status(201);
            //res.header("Location", "/offer/" + (offerBook.lastIndexOf(offer) + 1));

            res.type("application/json");
            return gson.toJson(offer);
        });

        delete("/offer", (req, res) -> {
            offerBook.deleteAll();
            res.status(204);
            res.type("application/json");
            return "";
        });

        delete("/offer/:id", (req, res) -> {
            offerBook.delete(Integer.parseInt(req.params("id")) - 1);
            res.status(204);
            res.type("application/json");
            return "";
        }, gson::toJson);
    }

    @Override
    public void onEvent(Event<String> event) {
        offerEventsWebSocket.broadcast(event);
    }

    public void stop() {
        Spark.stop();
    }

    public int getPort() {
        return this.port;
    }

    public static boolean isRunningLocally(int port) {
        try (Socket socket = new Socket()) {
            var address = new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
            socket.connect(address, 40);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
