package bisq.core.app;

import bisq.core.BisqCore;
import bisq.core.service.api.rest.RestApiService;

public class BisqDaemon implements Runnable {

    private final RestApiService restApiService;

    public BisqDaemon(RestApiService restApiService) {
        this.restApiService = restApiService;
    }

    public static void main(String[] args) {
        bisqd(args);
    }

    public static int bisqd(String... args) {
        new BisqDaemon(new RestApiService(new BisqCore(), RestApiService.DEFAULT_PORT)).run();
        return 0;
    }

    public void run() {
        restApiService.start();
    }

    public void stop() {
        restApiService.stop();
    }
}
