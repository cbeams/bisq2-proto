package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.*;

import static bisq.app.cli.BisqCli.EXIT_OK;
import static bisq.app.cli.BisqCli.EXIT_USER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BisqCliTest {

    static BisqDaemon daemon;

    TestConsole console = new TestConsole();
    int expectedStatus = EXIT_OK;
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
    void price() {
        runcli("price");
        // price returns a random value between 0 and 100,000
        String price = console.output().trim();
        assertTrue(price.matches("^\\d+.00$"), "got: [" + price + "]");
    }

    @Test
    void bogus() {
        runcli("bogus");
        expectedStatus = EXIT_USER_ERROR;
        assertEquals("""
                        Unmatched argument at index 0: 'bogus'
                        Usage: bisq [COMMAND]
                        Commands:
                          price
                          offer
                        """,
                console.errors());
    }

    private void runcli(String... args) {
        var cli = new BisqCli();
        cli.console = this.console;
        actualStatus = cli.run(args);
    }
}