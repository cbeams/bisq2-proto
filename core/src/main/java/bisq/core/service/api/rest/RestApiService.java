package bisq.core.service.api.rest;

import bisq.api.offer.OfferBook;
import bisq.core.BisqCore;
import bisq.api.event.Event;
import bisq.api.event.EventListener;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static spark.Spark.*;

public class RestApiService implements EventListener<String> {

    public static final Logger log = LoggerFactory.getLogger(RestApiService.class);

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
        log.info("starting REST api service on port {}", port);
        Spark.port(port);

        log.info("starting /offerevents WebSocket api service on port {}", port);
        webSocket("/offerevents", offerEventsWebSocket);
        init();

        exception(Exception.class, (ex, req, res) -> ex.printStackTrace(System.err));

        get("/offer", (req, res) -> {
            log.debug("handling request to view all offers");
            res.type("application/json");
            return offerBook.findAll();
        }, gson::toJson);

        get("/offer/:id", (req, res) -> {
            var id = Integer.parseInt(req.params("id")) - 1;
            log.debug("handling request to view offer {}", id);
            res.type("application/json");
            return offerBook.findById(id);
        }, gson::toJson);

        post("/offer", (req, res) -> {
            log.debug("handling request to create offer {}", req.body());
            var offer = gson.fromJson(req.body(), String.class);
            offerBook.save(offer);
            res.status(201);
            res.header("Location", "/offer/" + (offerBook.findAll().indexOf(offer) + 1));

            res.type("application/json");
            return gson.toJson(offer);
        });

        delete("/offer", (req, res) -> {
            log.debug("handling request to delete all offers");
            offerBook.deleteAll();
            res.status(204);
            res.type("application/json");
            return "";
        });

        delete("/offer/:id", (req, res) -> {
            var id = Integer.parseInt(req.params("id")) - 1;
            log.debug("handling request to delete offer {}", id);
            offerBook.delete(id);
            res.status(204);
            res.type("application/json");
            return "";
        }, gson::toJson);
    }

    @Override
    public void onEvent(Event<String> event) {
        offerEventsWebSocket.send(event);
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
