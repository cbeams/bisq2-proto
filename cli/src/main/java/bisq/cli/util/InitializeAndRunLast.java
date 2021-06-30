package bisq.cli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.ParameterException;

import static bisq.cli.util.CommandLineUtils.stripOptionPrefix;

public class InitializeAndRunLast implements CommandLine.IExecutionStrategy {

    private static final Logger log = LoggerFactory.getLogger(InitializeAndRunLast.class);

    @Override
    public int execute(CommandLine.ParseResult parseResult) throws ExecutionException, ParameterException {
        if (!parseResult.isUsageHelpRequested() && !parseResult.isVersionHelpRequested()) {
            var spec = parseResult.commandSpec();
            if (log.isDebugEnabled()) {
                spec.options().stream()
                        .filter(optionSpec -> !parseResult.hasMatchedOption(optionSpec.longestName()))
                        .forEach(s ->
                                log.debug("detected option '{}={}' in commandline defaults",
                                        stripOptionPrefix(s), s.getValue()));
                parseResult.matchedOptions().forEach(s ->
                        log.debug("detected option '{}={}' on commandline",
                                stripOptionPrefix(s), s.getValue()));
            }
            initialize(spec.commandLine());
            parseResult.subcommands().forEach(subcommandParseResult ->
                    initialize(subcommandParseResult.commandSpec().commandLine()));
        }
        return new CommandLine.RunLast().execute(parseResult);
    }

    private void initialize(CommandLine commandLine) {
        var command = commandLine.getCommand();
        try {
            if (command instanceof InitializableCommand)
                ((InitializableCommand) command).init();
        } catch (Exception e) {
            throw new ExecutionException(commandLine, e.getMessage(), e);
        }
    }
}
