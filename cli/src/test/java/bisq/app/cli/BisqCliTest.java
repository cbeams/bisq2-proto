package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static bisq.app.cli.BisqCli.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BisqCliTest {

    private static BisqDaemon daemon;

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private ByteArrayOutputStream errors = new ByteArrayOutputStream();

    @BeforeAll
    static void beforeAll() {
        daemon = new BisqDaemon();
        new Thread(daemon).start();
    }

    @AfterAll
    static void afterAll() {
        daemon.stop();
        BisqCli.out = System.out;
        BisqCli.err = System.err;
    }

    @BeforeEach
    void reset() {
        output = new ByteArrayOutputStream();
        errors = new ByteArrayOutputStream();
        BisqCli.out = new PrintStream(output);
        BisqCli.err = new PrintStream(errors);
    }

    private String stdout() {
        return output.toString();
    }

    private String stderr() {
        return errors.toString();
    }

    @Test
    void price() {
        assertEquals(EXIT_OK, bisq("price"));
        // price returns a random value between 0 and 100,000
        String price = stdout().trim();
        assertTrue(price.matches("^\\d+.00$"), "got: [" + price + "]");
    }

    @Test
    void offer() {
        assertEquals(EXIT_USER_ERROR, bisq("offer"));
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
        assertEquals(EXIT_OK, bisq("offer", "list"));
        assertEquals("""
                []
                """, stdout());
        assertEquals(EXIT_OK, bisq("offer", "create", "offerA"));
        reset();
        assertEquals(EXIT_OK, bisq("offer", "view", "1"));
        assertEquals("""
                "offerA"
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq("offer", "create", "offerB"));
        reset();
        assertEquals(EXIT_OK, bisq("offer", "create", "offerC"));
        reset();
        assertEquals(EXIT_OK, bisq("offer", "list"));
        assertEquals("""
                ["offerA","offerB","offerC"]
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq("offer", "delete", "1"));
        reset();
        assertEquals(EXIT_OK, bisq("offer", "list"));
        assertEquals("""
                ["offerB","offerC"]
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq("offer", "delete", "all"));
        reset();
        assertEquals(EXIT_OK, bisq("offer", "list"));
        assertEquals("""
                []
                """, stdout());
    }

    @Test
    void bogus() {
        assertEquals(EXIT_USER_ERROR, bisq("bogus"));
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