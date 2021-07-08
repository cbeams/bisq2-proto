package bisq.app.picocli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.ParameterException;

/**
 * Picocli command execution strategy that checks for {@value CommonOptions#verboseOpt}
 * on the commandline and sets the {@value BISQ_PACKAGE} logger log level to DEBUG if
 * set, and then delegates to its parent {@link InitializeAndRunLast} strategy. See
 * {@literal logback.xml} for defaults.
 */
public class BisqExecutionStrategy extends InitializeAndRunLast {

    public static final String BISQ_PACKAGE = "bisq";

    @Override
    public int execute(CommandLine.ParseResult parseResult) throws ExecutionException, ParameterException {
        Logger bisqLogger = (Logger) LoggerFactory.getLogger(BISQ_PACKAGE);
        if (parseResult.hasMatchedOption(CommonOptions.verboseOpt))
            bisqLogger.setLevel(Level.DEBUG);
        return super.execute(parseResult);
    }
}
