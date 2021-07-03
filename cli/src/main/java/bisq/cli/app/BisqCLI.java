package bisq.cli.app;

import bisq.app.BisqApp;
import bisq.cli.util.InitializeAndRunLast;

import picocli.CommandLine;

import java.io.PrintWriter;

import static bisq.cli.app.BisqConsole.*;

public class BisqCLI implements BisqApp {

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
                .setExecutionStrategy(new InitializeAndRunLast())
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
