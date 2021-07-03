package bisq.cli.app;

import bisq.api.conf.Config;
import bisq.app.BisqApp;
import bisq.cli.conf.Node;
import bisq.cli.util.CommandLineUtils;
import bisq.cli.util.InitializableCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.Spec;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

import static bisq.cli.app.BisqCommand.VersionProvider;
import static bisq.cli.app.BisqCommand.bisq;
import static java.lang.String.format;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

@Command(name = bisq,
        versionProvider = VersionProvider.class,
        subcommands = {
                HelpCommand.class,
                NodeSubcommand.class,
                OfferSubcommand.class
        })
@SuppressWarnings("unused")
class BisqCommand implements InitializableCommand {

    private static final Logger log = LoggerFactory.getLogger(BisqCommand.class);

    static final String bisq = "bisq";
    static final String appSectionType = "app";

    static final String versionOpt = "--version";
    @Option(names = {"-V", versionOpt},
            versionHelp = true,
            description = "Print version information and exit")
    boolean versionRequested;

    static final String helpOpt = "--help";
    @Option(names = {"-h", helpOpt},
            usageHelp = true,
            description = "Print this usage help")
    boolean helpRequested;

    static final String verboseOpt = "--verbose";
    @Option(names = {"-v", verboseOpt},
            description = "Enable verbose mode to diagnose problems")
    boolean verbose = false;

    static final String stacktraceOpt = "--stacktrace";
    @Option(names = {"-s", stacktraceOpt},
            description = "Print stack trace when execution errors occur")
    boolean stacktrace = false;

    static final String nodeOpt = "--node";
    @Option(names = {"-n", nodeOpt},
            paramLabel = "<node>",
            description = "Nicename or host[:port] address of the Bisq node to use",
            showDefaultValue = ALWAYS)
    String nodeSpec = Node.DEFAULT_NICE_NAME;

    final String confOpt = "--conf";
    @Option(names = {"-c", confOpt},
            paramLabel = "<conf>",
            description = "Path to config file",
            showDefaultValue = ALWAYS)
    File confFile = defaultConfFilePath().toFile();

    Config conf;
    Node node;

    @Spec
    CommandSpec spec;

    @Override
    public void init() {
        if (confFile.exists()) {
            log.debug("parsing config file '{}'", confFile);
            conf = Config.parse(confFile);
        } else if (spec.commandLine().getParseResult().hasMatchedOption(confOpt)) {
            throw new IllegalArgumentException(format("specified config file '%s' does not exist", confFile));
        } else {
            log.debug("default config file '{}' does not exist", confFile);
            conf = new Config();
        }
        var cliParseResult = spec.commandLine().getParseResult();
        var confOptions = conf.getOptions(appSectionType, bisq);

        stacktrace = selectOptionValue(cliParseResult, confOptions, stacktraceOpt, stacktrace, Boolean::parseBoolean);
        nodeSpec = selectOptionValue(cliParseResult, confOptions, nodeOpt, nodeSpec, s -> s);

        node = Node.extractFrom(nodeSpec, conf);
    }

    private static <T> T selectOptionValue(ParseResult cliParseResult, Map<String, String> confOptions,
                                           String cliOptionName, T cliOptionValue,
                                           Function<String, T> confOptionValueConverter) {
        var plainOptionName = CommandLineUtils.stripOptionPrefix(cliOptionName);
        var confOptionIsPresent = confOptions.containsKey(plainOptionName);
        var cliOptionIsPresent = cliParseResult.hasMatchedOption(cliOptionName);
        var cliOptionDefaultValue = cliParseResult.commandSpec().findOption(cliOptionName).defaultValueString();
        if (confOptionIsPresent) {
            var confOptionValue = confOptions.get(plainOptionName);
            if (cliOptionIsPresent) {
                log.debug("selected option '{}={}' on commandline over '{}={}' in config file",
                        plainOptionName, cliOptionValue, plainOptionName, confOptionValue);
                return cliOptionValue;
            } else {
                log.debug("selected option '{}={}' in config file over '{}={}' in commandline defaults",
                        plainOptionName, confOptionValue, plainOptionName, cliOptionDefaultValue);
                return confOptionValueConverter.apply(confOptionValue);
            }
        } else if (cliOptionIsPresent) {
            log.debug("selected option '{}={}' on commandline over '{}={}' in commandline defaults",
                    plainOptionName, cliOptionValue, plainOptionName, cliOptionDefaultValue);
        } else {
            log.debug("selected option '{}={}' in commandline defaults", plainOptionName, cliOptionDefaultValue);
        }
        return cliOptionValue;
    }

    private static Path defaultConfFilePath() {
        return Path.of(System.getProperty("user.home"), ".bisq", "config");
    }

    static class VersionProvider implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            return new String[]{
                    format("%s version %s", BisqApp.APP_INFO.getName(), BisqApp.APP_INFO.getVersion())
            };
        }
    }
}
