package bisq.cli.app;

import bisq.app.BisqApp;
import bisq.core.BisqCore;
import bisq.core.node.BisqNode;
import bisq.core.service.api.rest.RestApiService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static bisq.cli.app.BisqCLI.*;
import static bisq.core.service.api.rest.RestApiService.RANDOM_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BisqCLITest {

    private static BisqNode node;
    private static int restApiPort;

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private ByteArrayOutputStream errors = new ByteArrayOutputStream();

    @BeforeAll
    static void beforeAll() {
        var restApiService = new RestApiService(new BisqCore(), RANDOM_PORT);
        restApiPort = restApiService.getPort();
        node = new BisqNode(restApiService);
        new Thread(node).start();
    }

    @AfterAll
    static void afterAll() {
        node.stop();
        BisqCLI.out = System.out;
        BisqCLI.err = System.err;
    }

    @BeforeEach
    void reset() {
        output = new ByteArrayOutputStream();
        errors = new ByteArrayOutputStream();
        BisqCLI.out = new PrintStream(output);
        BisqCLI.err = new PrintStream(errors);
    }

    private String stdout() {
        return output.toString();
    }

    private String stderr() {
        return errors.toString();
    }

    @Test
    void whenVersionOptionIsProvided_thenPrintVersionAndExit() {
        assertEquals(EXIT_OK, bisq("-v"), stderr());
        var name = BisqApp.APP_INFO.getName();
        var version = BisqApp.APP_INFO.getVersion();
        assertFalse(name.contains("${") || version.contains("${"), "resources were not processed");
        assertEquals(String.format("""
                %s version %s
                """,
                name, version), stdout());
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
                offerA
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq(offer, create, "offerB"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, create, "offerC"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, list), stderr());
        assertEquals("""
                [offerA, offerB, offerC]
                """, stdout());
        reset();
        assertEquals(EXIT_OK, bisq(offer, delete, "1"), stderr());
        reset();
        assertEquals(EXIT_OK, bisq(offer, list), stderr());
        assertEquals("""
                [offerB, offerC]
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
                        Unmatched argument at index 2: 'bogus'
                        Usage: bisq [-v] [--debug] [--node=<host>] [--port=<n>] [COMMAND]
                              --debug         Print stack trace when execution errors occur
                              --node=<host>   Specify hostname of the target Bisq node
                              --port=<n>      Specify API port of the target Bisq node
                                                Default: 2140
                          -v, --version       Print version information and exit
                        Commands:
                          offer
                        """,
                stderr());
    }

    private static int bisq(String... args) {
        var newArgs = new ArrayList<String>();
        newArgs.add("--debug");
        newArgs.add("--port=" + restApiPort);
        newArgs.addAll(Arrays.asList(args));
        return BisqCLI.bisq(newArgs.toArray(new String[]{}));
    }
}
