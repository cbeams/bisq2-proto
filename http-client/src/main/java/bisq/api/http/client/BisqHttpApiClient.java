package bisq.api.http.client;

import bisq.api.client.BisqApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.Socket;

public class BisqHttpApiClient implements BisqApiClient {

    private final String host;
    private final int port;

    public BisqHttpApiClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
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

    @Override
    public String getPrice() {
        return "$42.00";
    }
}
