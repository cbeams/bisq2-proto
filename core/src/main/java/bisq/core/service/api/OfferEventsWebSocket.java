package bisq.core.service.api;

import bisq.api.event.Event;

import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
@SuppressWarnings("unused") // for @OnWebSocket* annotated methods
public class OfferEventsWebSocket {

    private static final Logger log = LoggerFactory.getLogger(OfferEventsWebSocket.class);

    private final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private final Gson gson = new Gson();

    @OnWebSocketConnect
    public void connected(Session session) {
        log.debug("opened offer event connection with {}", session.getRemoteAddress());
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        log.debug("closed offer event connection with {}", session.getRemoteAddress());
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        session.getRemote().sendString(message); // and send it back
    }

    public void send(Event<String> event) {
        String eventJson = gson.toJson(event);
        log.debug("sending offer event {}", eventJson);
        sessions.forEach(s -> {
            try {
                s.getRemote().sendString(eventJson);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
