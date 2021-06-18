package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BisqCliTest {

    BisqDaemon daemon;

    @BeforeEach
    void startDaemon() {
        daemon = new BisqDaemon();
        new Thread(daemon).start();
    }

    @AfterEach
    void stopDaemon() {
        daemon.stop();
    }

    @Test
    void getversion() {
        assertEquals( """
                43
                """,
                cli("getversion").getOutput());
    }

    @Test
    void getprice() {
        assertEquals( """
                43
                """,
                cli("getprice").getOutput());
    }

    @Test
    void bogus() {
        assertEquals( """
                unsupported command: bogus
                """,
                cli("bogus").getErrors());
    }

    private TestConsole cli(String... args) {
        var console = new TestConsole();
        var cli = new BisqCli(args);
        cli.setConsole(console);
        cli.run();
        return console;
    }
}