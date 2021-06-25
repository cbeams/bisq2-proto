package bisq.core.service.api.rest;

import bisq.util.event.Event;

import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
@SuppressWarnings("unused") // for @OnWebSocket* annotated methods
public class OfferEventsWebSocket {

    private final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private final Gson gson = new Gson();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        session.getRemote().sendString(message); // and send it back
    }

    public void broadcast(Event<String> event) {
        String message = gson.toJson(event);
        sessions.forEach(s -> {
            try {
                s.getRemote().sendString(message);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
