package bisq.core.node;

import bisq.core.service.api.rest.RestApiService;

public class BisqNode implements Runnable {

    private final RestApiService restApiService;

    public BisqNode(RestApiService restApiService) {
        this.restApiService = restApiService;
    }

    public void run() {
        restApiService.start();
    }

    public void stop() {
        restApiService.stop();
    }
}
