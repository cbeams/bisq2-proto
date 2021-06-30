package bisq.cli.conf;

import bisq.api.conf.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public record Node(String niceName, String host, int port) {

    private static final Logger log = LoggerFactory.getLogger(Node.class);

    public static final String DEFAULT_NICE_NAME = "local";
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 2140;

    static final String hostOpt = "host";
    static final String portOpt = "port";
    static final String nodeSectionType = "node";

    public Node {
        if (host == null)
            throw new IllegalArgumentException(String.format("%s value must not be null", hostOpt));
    }

    public static Node extractFrom(String nodeSpec, Config conf) {
        final String host;
        final int port;
        var nodeSection = new Config.Section(nodeSectionType, nodeSpec);
        if (conf.hasOptions(nodeSection)) {
            var nodeOptions = conf.getOptions(nodeSection);
            if (!nodeOptions.containsKey(hostOpt))
                throw new IllegalArgumentException(
                        String.format("[%s] sections must contain a '%s' option", nodeSectionType, hostOpt));
            host = nodeOptions.get(hostOpt);
            log.debug("selected option '{}={}' from config file section {}", hostOpt, host, nodeSection);
            if (nodeOptions.containsKey(portOpt)) {
                port = Integer.parseInt(nodeOptions.get(portOpt));
                log.debug("selected option '{}={}' from config file section {}", portOpt, port, nodeSection);
            } else {
                port = DEFAULT_PORT;
                log.debug("selected option '{}={}' from node defaults", portOpt, port);
            }
        } else if (nodeSpec.contains(":")) {
            host = nodeSpec.split(":")[0];
            port = Integer.parseInt(nodeSpec.split(":")[1]);
            log.debug("selected option '{}={}' from node spec '{}'", hostOpt, host, nodeSpec);
            log.debug("selected option '{}={}' from node spec '{}'", portOpt, port, nodeSpec);
        } else if (nodeSpec.equals(DEFAULT_NICE_NAME)) {
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
            log.debug("selected option '{}={}' from node defaults", hostOpt, host);
            log.debug("selected option '{}={}' from node defaults", portOpt, port);
        } else {
            host = nodeSpec;
            port = DEFAULT_PORT;
            log.debug("selected option '{}={}' from node spec '{}'", hostOpt, host, nodeSpec);
            log.debug("selected option '{}={}' from node defaults", portOpt, port);
        }
        return new Node(nodeSpec, host, port);
    }

    public static List<String> findAllNiceNamesIn(Config conf) {
        return new ArrayList<>(
                conf.getSectionsOfType(nodeSectionType).keySet().stream().map(Config.Section::name).toList());
    }
}
