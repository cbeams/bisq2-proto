package bisq.cli.app;

import bisq.app.BisqApp;
import bisq.cli.util.InitializeAndRunLast;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.ParameterException;

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
                .setExecutionStrategy(new BisqCLI.ExecutionStrategy())
                .setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
                    err.println("error: " + ex.getMessage());
                    if (parseResult.hasMatchedOption(BisqCommand.stacktraceOpt)) {
                        throw ex;
                    }
                    return EXIT_APP_ERROR;
                })
                .execute(args);
    }

    /**
     * Picocli command execution strategy that checks for {@value BisqCommand#verboseOpt}
     * on the commandline and sets the {@value BISQ_PACKAGE} logger log level to DEBUG if
     * set, and then delegates to its parent {@link InitializeAndRunLast} strategy. See
     * {@literal logback.xml} for defaults.
     */
    static class ExecutionStrategy extends InitializeAndRunLast {

        public static final String BISQ_PACKAGE = "bisq";

        @Override
        public int execute(CommandLine.ParseResult parseResult) throws ExecutionException, ParameterException {
            Logger bisqLogger = (Logger) LoggerFactory.getLogger(BISQ_PACKAGE);
            if (parseResult.hasMatchedOption(BisqCommand.verboseOpt))
                bisqLogger.setLevel(Level.DEBUG);
            return super.execute(parseResult);
        }
    }
}
