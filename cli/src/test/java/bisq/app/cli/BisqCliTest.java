package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.*;

import static bisq.app.cli.BisqCli.EXIT_FAILURE;
import static bisq.app.cli.BisqCli.EXIT_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BisqCliTest {

    static BisqDaemon daemon;

    TestConsole console = new TestConsole();
    int expectedStatus = EXIT_SUCCESS;
    int actualStatus = -1;

    @BeforeAll
    static void startDaemon() {
        daemon = new BisqDaemon();
        new Thread(daemon).start();
    }

    @AfterEach
    void checkExitStatus() {
        assertEquals(expectedStatus, actualStatus);
    }

    @AfterAll
    static void stopDaemon() {
        daemon.stop();
    }

    @Test
    void getversion() {
        runcli("getversion");
        assertEquals("""
                0.0.1
                """, console.output());
    }

    @Test
    void getprice() {
        runcli("getprice");
        assertEquals("""
                $42.00
                """, console.output());
    }

    @Test
    void bogus() {
        runcli("bogus");
        expectedStatus = EXIT_FAILURE;
        assertEquals("""
                        unsupported command: bogus
                        """,
                console.errors());
    }

    private void runcli(String... args) {
        var cli = new BisqCli(args);
        cli.setConsole(console);
        actualStatus = cli.run();
    }
}