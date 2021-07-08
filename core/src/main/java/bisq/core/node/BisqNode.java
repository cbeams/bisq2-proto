package bisq.core.node;

import bisq.core.service.api.ApiService;

public class BisqNode implements Runnable {

    private final ApiService apiService;

    public BisqNode(ApiService apiService) {
        this.apiService = apiService;
    }

    public void run() {
        apiService.start();
    }

    public void stop() {
        apiService.stop();
    }
}
