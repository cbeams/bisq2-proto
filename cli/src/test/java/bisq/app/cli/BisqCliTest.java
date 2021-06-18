package bisq.app.cli;

import bisq.app.daemon.BisqDaemon;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BisqCliTest {

    @Test
    void test() {
        new Thread(new BisqDaemon()).start();
        var out = new ByteArrayOutputStream();
        var cli = new BisqCli("getversion");
        cli.out = new PrintStream(out);
        cli.run();
        assertEquals( """
                43
                """,
                out.toString());
    }
}