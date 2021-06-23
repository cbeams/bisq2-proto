package bisq.app.daemon;

import bisq.api.http.service.HttpApiService;
import bisq.api.service.ApiService;

public class BisqDaemon implements Runnable {

    private final ApiService apiService;

    public BisqDaemon() {
        this(new HttpApiService());
    }

    public BisqDaemon(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void run() {
        apiService.run();
    }

    public void stop() {
        apiService.stop();
    }

    public static void main(String[] args) {
        new BisqDaemon().run();
    }
}
