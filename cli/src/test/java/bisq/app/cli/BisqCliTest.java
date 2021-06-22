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

    private static BisqDaemon daemon;

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private ByteArrayOutputStream errors = new ByteArrayOutputStream();

    @BeforeAll
    static void startDaemon() {
        daemon = new BisqDaemon();
        new Thread(daemon).start();
    }

    @AfterAll
    static void stopDaemon() {
        daemon.stop();
    }

    @BeforeEach
    void initConsole() {
        output = new ByteArrayOutputStream();
        errors = new ByteArrayOutputStream();
        BisqCli.out = new PrintStream(output);
        BisqCli.err = new PrintStream(errors);
    }

    @AfterEach
    void resetConsole() {
        BisqCli.out = System.out;
        BisqCli.err = System.err;
    }

    private String stdout() {
        return output.toString();
    }

    private String stderr() {
        return errors.toString();
    }

    @Test
    void price() {
        assertEquals(EXIT_OK, BisqCli.execute("price"));
        // price returns a random value between 0 and 100,000
        String price = stdout().trim();
        assertTrue(price.matches("^\\d+.00$"), "got: [" + price + "]");
    }

    @Test
    void bogus() {
        assertEquals(EXIT_USER_ERROR, BisqCli.execute("bogus"));
        assertEquals("""
                        Unmatched argument at index 0: 'bogus'
                        Usage: bisq [COMMAND]
                        Commands:
                          price
                          offer
                        """,
                stderr());
    }
}