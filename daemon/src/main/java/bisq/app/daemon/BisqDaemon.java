package bisq.app.daemon;

import bisq.api.client.CoreApiClient;
import bisq.api.http.server.BisqHttpApiServer;
import bisq.api.server.BisqApiServer;

public class BisqDaemon implements Runnable {

    private BisqApiServer apiServer;

    public BisqDaemon(BisqApiServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    public void run() {
        apiServer.run();
    }

    public static void main(String[] args) {
        new BisqDaemon(new BisqHttpApiServer(new CoreApiClient(), 9999)).run();
    }
}
