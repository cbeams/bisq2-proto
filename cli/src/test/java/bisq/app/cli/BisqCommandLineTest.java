package bisq.app.cli;

import bisq.core.app.BisqDaemon;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static bisq.app.cli.BisqCommandLine.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BisqCommandLineTest {

    private static BisqDaemon daemon;

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private ByteArrayOutputStream errors = new ByteArrayOutputStream();

    @BeforeAll
    static void beforeAll() {
        daemon = BisqDaemon.newDaemon();
        new Thread(daemon).start();
    }

    @AfterAll
    static void afterAll() {
        daemon.stop();
        BisqCommandLine.out = System.out;
        BisqCommandLine.err = System.err;
    }

    @BeforeEach
    void reset() {
        output = new ByteArrayOutputStream();
        errors = new ByteArrayOutputStream();
        BisqCommandLine.out = new PrintStream(output);
        BisqCommandLine.err = new PrintStream(errors);
    }

    private String stdout() {
        return output.toString();
    }

    private String stderr() {
        return errors.toString();
    }

    @Test
    void price() {
        assertEquals(EXIT_OK, bisq("price"), stderr());
        // price returns a random value between 0 and 100,000
        String price = stdout().trim();
        assertTrue(price.matches("^\\d+.00$"), "got: [" + price + "]");
    }

    @Test
    void offer() {
        assertEquals(EXIT_USER_ERROR, bisq("offer"), stderr());
        assertEquals("""
                        Missing required subcommand
                        Usage: bisq offer [COMMAND]
                        Commands:
                          create
                          delete
                          list
                          view
                        """,
                stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, list));
        assertEquals("""
                []
                """, stdout());
        assertEquals(EXIT_OK, bisq(offer, create, "offerA"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, view, "1"), stderr());
        assertEquals("""
                "offerA"
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq(offer, create, "offerB"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, create, "offerC"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, list), stderr());
        assertEquals("""
                ["offerA","offerB","offerC"]
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq(offer, delete, "1"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, list), stderr());
        assertEquals("""
                ["offerB","offerC"]
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq(offer, delete, "all"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, list), stderr());
        assertEquals("""
                []
                """, stdout());
    }

    @Test
    void bogus() {
        assertEquals(EXIT_USER_ERROR, bisq("bogus"));
        assertEquals("""
                        Unmatched argument at index 0: 'bogus'
                        Usage: bisq [--debug] [COMMAND]
                              --debug   Print stack trace when execution errors occur
                        Commands:
                          price
                          offer
                        """,
                stderr());
    }
}