package bisq.cli;

import bisq.app.config.NodeConfig;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.List;

import static bisq.app.BisqConsole.out;

@Command(name = "node")
@SuppressWarnings("unused")
class NodeCommand implements Runnable {

    @ParentCommand
    private BisqCommand bisq;

    /**
     * Ensure that running `bisq node` produces the same results as explicitly running
     * `bisq node list`. This is similar to the way `git branch` and `git branch --list`
     * work.
     */
    @Override
    public void run() {
        list();
    }

    @Command(name = "list")
    public void list() {
        List<String> allNiceNames = NodeConfig.findAllNiceNamesIn(bisq.conf);
        var selectedNiceName = bisq.node.niceName();
        if (!allNiceNames.contains(selectedNiceName))
            allNiceNames.add(selectedNiceName);
        allNiceNames.stream()
                .sorted()
                .forEach(niceName -> {
                    // mark up selected node with a asterisk and ansi green color similar
                    // to the way `git branch` does for the current branch
                    var selected = niceName.equals(selectedNiceName);
                    out.printf("%s %s%s%s\n",
                            selected ? "*" : " ",
                            selected ? "\033[32m" : "",
                            niceName,
                            selected ? "\033[0m" : ""
                    );
                });
    }
}
