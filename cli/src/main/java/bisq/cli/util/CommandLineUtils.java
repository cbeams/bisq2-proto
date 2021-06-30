package bisq.cli.util;

import picocli.CommandLine;

public class CommandLineUtils {
    public static String stripOptionPrefix(CommandLine.Model.OptionSpec optionSpec) {
        return stripOptionPrefix(optionSpec.longestName());
    }

    public static String stripOptionPrefix(String optionName) {
        return optionName.replaceFirst("^--?", "");
    }
}
