package bisq.app.daemon;

import bisq.api.client.CoreApiClient;
import bisq.api.http.service.HttpApiService;
import bisq.api.service.ApiService;

public class BisqDaemon implements Runnable {

    private ApiService apiService;

    public BisqDaemon() {
        this(new HttpApiService(new CoreApiClient(), 9999));
    }

    public BisqDaemon(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void run() {
        apiService.run();
    }

    public static void main(String[] args) {
        new BisqDaemon().run();
    }
}
