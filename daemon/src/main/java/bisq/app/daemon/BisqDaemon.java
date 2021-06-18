package bisq.app.daemon;

import bisq.api.client.CoreApiClient;
import bisq.api.http.service.HttpApiService;
import bisq.api.service.ApiService;

public class BisqDaemon implements Runnable {

    private ApiService apiServer;

    public BisqDaemon(ApiService apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    public void run() {
        apiServer.run();
    }

    public static void main(String[] args) {
        new BisqDaemon(new HttpApiService(new CoreApiClient(), 9999)).run();
    }
}
