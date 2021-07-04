package bisq.fx.app;

import bisq.app.BisqApp;
import bisq.app.picocli.BisqExecutionStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.PrintWriter;

import static bisq.app.BisqConsole.*;

public class BisqFXMain implements BisqApp {

    private static final Logger log = LoggerFactory.getLogger(BisqFXMain.class);

    public static void main(String[] args) {
        System.exit(bisqfx(args));
    }

    static int bisqfx(String... args) {
        log.info(BisqApp.nameAndVerison());
        return new CommandLine(BisqFXCommand.class)
                .setOut(new PrintWriter(out))
                .setErr(new PrintWriter(err))
                .setColorScheme(bisqColorScheme)
                .setExecutionStrategy(new BisqExecutionStrategy())
                .setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
                    err.println("error: " + ex.getMessage());
                    // should check for --stacktrace
                    throw ex;
                })
                .execute(args);
    }
}
