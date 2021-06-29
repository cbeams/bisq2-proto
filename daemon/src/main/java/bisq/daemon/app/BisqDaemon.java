package bisq.daemon.app;

import bisq.core.BisqCore;
import bisq.core.node.BisqNode;
import bisq.core.service.api.rest.RestApiService;

public class BisqDaemon {

    public static void main(String[] args) {
        bisqd(args);
    }

    public static int bisqd(String... args) {
        new BisqNode(new RestApiService(new BisqCore(), RestApiService.DEFAULT_PORT)).run();
        return 0;
    }
}
