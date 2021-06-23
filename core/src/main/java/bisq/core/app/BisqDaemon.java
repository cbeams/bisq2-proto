package bisq.core.app;

import bisq.core.http.HttpApiService;

public class BisqDaemon implements Runnable {

    private final HttpApiService httpApiService;

    public BisqDaemon(HttpApiService httpApiService) {
        this.httpApiService = httpApiService;
    }

    public static void main(String[] args) {
        bisqd(args);
    }

    public static int bisqd(String... args) {
        newDaemon().run();
        return 0;
    }

    public static BisqDaemon newDaemon() {
        return new BisqDaemon(new HttpApiService());
    }

    public void run() {
        httpApiService.start();
    }

    public void stop() {
        httpApiService.stop();
    }
}