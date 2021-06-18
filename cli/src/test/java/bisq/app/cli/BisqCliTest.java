package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bisq.app.cli.BisqCli.EXIT_FAILURE;
import static bisq.app.cli.BisqCli.EXIT_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BisqCliTest {

    BisqDaemon daemon;
    TestConsole console = new TestConsole();
    int expectedStatus = EXIT_SUCCESS;
    int actualStatus = -1;

    @BeforeEach
    void startDaemon() {
        daemon = new BisqDaemon();
        new Thread(daemon).start();
    }

    @AfterEach
    void stopDaemon() {
        assertEquals(expectedStatus, actualStatus);
        daemon.stop();
    }

    @Test
    void getversion() {
        runcli("getversion");
        assertEquals("""
                43
                """, console.getOutput());
    }

    @Test
    void getprice() {
        runcli("getprice");
        assertEquals("""
                43
                """, console.getOutput());
    }

    @Test
    void bogus() {
        runcli("bogus");
        expectedStatus = EXIT_FAILURE;
        assertEquals("""
                        unsupported command: bogus
                        """,
                console.getErrors());
    }

    private void runcli(String... args) {
        var cli = new BisqCli(args);
        cli.setConsole(console);
        actualStatus = cli.run();
    }
}