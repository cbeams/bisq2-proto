package bisq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.Socket;

public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getVersion() {
        try {
            try (Socket client = new Socket(host, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                String version = in.readLine();
                return version;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
