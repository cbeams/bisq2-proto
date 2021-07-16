package bisq.app.daemon;

import bisq.app.BisqApp;

import bisq.core.CoreBisqService;
import bisq.core.node.BisqNode;
import bisq.core.service.api.ApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BisqDaemon implements BisqApp {

    private static final Logger log = LoggerFactory.getLogger(BisqDaemon.class);

    public static void main(String[] args) {
        bisqd(args);
    }

    public static int bisqd(String... args) {
        log.info(BisqApp.nameAndVerison());
        new BisqNode(new ApiService(new CoreBisqService(), ApiService.DEFAULT_PORT)).run();
        return 0;
    }
}
