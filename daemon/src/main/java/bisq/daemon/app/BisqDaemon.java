package bisq.daemon.app;

import bisq.app.BisqApp;
import bisq.core.BisqCore;
import bisq.core.node.BisqNode;
import bisq.core.service.api.rest.RestApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BisqDaemon implements BisqApp {

    private static final Logger log = LoggerFactory.getLogger(BisqDaemon.class);

    public static void main(String[] args) {
        bisqd(args);
    }

    public static int bisqd(String... args) {
        log.info("{} version {}", APP_INFO.getName(), APP_INFO.getVersion());
        new BisqNode(new RestApiService(new BisqCore(), RestApiService.DEFAULT_PORT)).run();
        return 0;
    }
}
