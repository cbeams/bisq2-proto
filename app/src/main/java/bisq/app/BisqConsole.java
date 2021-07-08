package bisq.app;

import picocli.CommandLine;
import picocli.CommandLine.Help.Ansi.Style;

import java.io.PrintStream;

import static picocli.CommandLine.Help.Ansi.Style.*;

public class BisqConsole {

    public static PrintStream out = System.out;
    public static PrintStream err = System.err;

    public static final CommandLine.Help.ColorScheme bisqColorScheme =
            new CommandLine.Help.ColorScheme.Builder()
                .commands(fg_green, bold)
                        .options(fg_green)
                        .optionParams(italic)
                        .errors(fg_red, bold)
                        .stackTraces(italic)
                        .build();
}
