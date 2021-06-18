package bisq.api.http.service;

import bisq.api.client.CoreApiClient;
import bisq.api.service.ApiService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpApiService implements ApiService {

    private CoreApiClient coreApiClient;
    private final int port;

    public HttpApiService(CoreApiClient coreApiClient, int port) {
        this.coreApiClient = coreApiClient;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);
            System.out.println("listening on port " + port);
            Socket s = socket.accept();
            OutputStream out = s.getOutputStream();
            out.write(coreApiClient.getVersion().getBytes());
            out.write('\n');
            out.flush();
            System.out.println("exiting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
