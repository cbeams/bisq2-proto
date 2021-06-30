package bisq.cli.app;

import bisq.cli.conf.Node;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.List;

import static bisq.cli.app.BisqConsole.out;

@Command(name = "node")
@SuppressWarnings("unused")
class NodeSubcommand {

    @ParentCommand
    private BisqCommand bisq;

    @Command(name = OfferSubcommand.list)
    public void list() {
        List<String> allNiceNames = Node.findAllNiceNamesIn(bisq.conf);
        var selectedNiceName = bisq.node.niceName();
        if (!allNiceNames.contains(selectedNiceName))
            allNiceNames.add(selectedNiceName);
        allNiceNames.stream()
                .sorted()
                .forEach(niceName -> {
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
