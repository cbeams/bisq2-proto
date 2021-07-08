package bisq.api.client;

import bisq.offer.OfferBook;
import bisq.event.Event;
import bisq.event.EventListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class OfferBookApiClient implements AutoCloseable, OfferBook {

    private static final Logger log = LoggerFactory.getLogger(OfferBookApiClient.class);

    private final OkHttpClient httpClient;
    private final String restApiUrl;
    private final String eventWsUrl;
    private final List<EventListener<String>> eventListeners = new ArrayList<>();
    private final Gson gson = new Gson();

    private WebSocket eventWebSocket;

    public OfferBookApiClient(OkHttpClient httpClient, String host, int port) {
        this.httpClient = httpClient;
        this.restApiUrl = format("http://%s:%d/offer", host, port);
        this.eventWsUrl = format("ws://%s:%d/offerevents", host, port);
    }

    @Override
    public List<String> findAll() throws IOException {
        log.debug("sending request to view all offers");
        var json = httpClient.newCall(new Request.Builder()
                .url(restApiUrl)
                .build()).execute().body().string();
        return Arrays.asList(gson.fromJson(json, String[].class));
    }

    @Override
    public String findById(int id) throws IOException {
        log.debug("sending request to view offer {}", id);
        var json = httpClient.newCall(new Request.Builder()
                .url(restApiUrl + "/" + id)
                .build()).execute().body().string();
        return gson.fromJson(json, String.class);
    }

    @Override
    public String save(String offer) throws IOException {
        var json = gson.toJson(offer);
        log.debug("sending request to create offer {}", json);
        var savedJson = httpClient.newCall(new Request.Builder()
                .url(restApiUrl)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build()).execute().body().string();
        return gson.fromJson(savedJson, String.class);
    }

    @Override
    public void delete(int id) throws IOException {
        log.debug("sending request to delete offer {}", id);
        httpClient.newCall(new Request.Builder()
                .url(restApiUrl + "/" + id)
                .delete()
                .build()).execute();
    }

    @Override
    public void deleteAll() throws IOException {
        log.debug("sending request to delete all offers");
        httpClient.newCall(new Request.Builder()
                .url(restApiUrl)
                .delete()
                .build()).execute();
    }

    @Override
    public void addEventListener(EventListener<String> eventListener) {
        // avoid establishing WebSocket connection until there is at least one listener
        if (eventListeners.isEmpty())
            subscribeToOfferEvents();
        eventListeners.add(eventListener);
    }

    private void subscribeToOfferEvents() {
        log.info("subscribing to offer events at {}", eventWsUrl);
        Request request = new Request.Builder()
                .url(eventWsUrl)
                .build();
        Type eventType = new TypeToken<Event<String>>() {}.getType();
        this.eventWebSocket = httpClient.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String eventJson) {
                log.debug("receiving offer event {}", eventJson);
                Event<String> event = gson.fromJson(eventJson, eventType);
                eventListeners.forEach(eventListener -> eventListener.onEvent(event));
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                log.debug("connection to offer events at {} failed (no retry logic yet...)", eventWsUrl);
            }
        });
    }

    @Override
    public void close() {
        if (eventWebSocket == null)
            return;

        log.debug("unsubscribing from offer events at {}", eventWsUrl);
        eventWebSocket.close(1000, "user requested closure");
    }
}
