package bisq.app.cli;

import bisq.app.BisqConsole;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

@Command(name = "help")
public class HelpCommand implements Callable<Integer> {

    @Spec
    CommandSpec spec;

    @Override
    public Integer call() {
        spec.parent().commandLine().usage(BisqConsole.out);
        return 8;
    }
}
