package bisq.cli;

import bisq.app.BisqApp;

import bisq.app.picocli.BisqExecutionStrategy;
import picocli.CommandLine;

import java.io.PrintWriter;

import static bisq.app.BisqConsole.*;

public class Main implements BisqApp {

    static final int EXIT_OK = CommandLine.ExitCode.OK;
    static final int EXIT_APP_ERROR = CommandLine.ExitCode.SOFTWARE;
    static final int EXIT_USER_ERROR = CommandLine.ExitCode.USAGE;

    public static void main(String[] args) {
        System.exit(bisq(args));
    }

    static int bisq(String... args) {
        return new CommandLine(BisqCommand.class)
                .setOut(new PrintWriter(out))
                .setErr(new PrintWriter(err))
                .setColorScheme(bisqColorScheme)
                .setExecutionStrategy(new BisqExecutionStrategy())
                .setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
                    err.println("error: " + ex.getMessage());
                    if (parseResult.hasMatchedOption(BisqCommand.stacktraceOpt)) {
                        throw ex;
                    }
                    return EXIT_APP_ERROR;
                })
                .execute(args);
    }

}
