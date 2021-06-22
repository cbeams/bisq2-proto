package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static bisq.app.cli.BisqCli.EXIT_OK;
import static bisq.app.cli.BisqCli.EXIT_USER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BisqCliTest {

    static BisqDaemon daemon;

    static ByteArrayOutputStream output = new ByteArrayOutputStream();
    static ByteArrayOutputStream errors = new ByteArrayOutputStream();

    int expectedStatus = EXIT_OK;
    int actualStatus = -1;

    @BeforeAll
    static void setUp() {
        daemon = new BisqDaemon();
        new Thread(daemon).start();

        BisqCli.out = new PrintStream(output);
        BisqCli.err = new PrintStream(errors);
    }

    @AfterEach
    void checkExitStatus() {
        assertEquals(expectedStatus, actualStatus);
    }

    @AfterAll
    static void tearDown() {
        daemon.stop();

        BisqCli.out = System.out;
        BisqCli.err = System.err;
    }

    @Test
    void price() {
        runcli("price");
        // price returns a random value between 0 and 100,000
        String price = output.toString().trim();
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
                errors.toString());
    }

    private void runcli(String... args) {
        actualStatus = new BisqCli().run(args);
    }
}