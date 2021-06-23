package bisq.core.app;

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
        newDaemon().run();
        return 0;
    }

    public static BisqDaemon newDaemon() {
        return new BisqDaemon(new RestApiService());
    }

    public void run() {
        restApiService.start();
    }

    public void stop() {
        restApiService.stop();
    }
}