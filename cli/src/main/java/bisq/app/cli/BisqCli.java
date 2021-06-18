/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package bisq.app.cli;

import bisq.api.client.ApiClient;
import bisq.api.http.client.HttpApiClient;

public class BisqCli implements Runnable {

    private Console console = new SystemConsole();

    private final ApiClient client;
    private final String command;

    public BisqCli(String... args) {
        if (args.length != 1)
            throw new IllegalArgumentException("usage: bisq-cli <command>");
        this.client = new HttpApiClient("localhost", 9999);
        this.command = args[0];
    }

    @Override
    public void run() {
        switch (command) {
            case "getversion" -> console.outln(client.getVersion());
            case "getprice" -> console.outln(client.getPrice());
            default -> throw new UnsupportedOperationException("unsupported command: " + command);
        }
    }

    void setConsole(Console console) {
        this.console = console;
    }

    public static void main(String[] args) {
        new BisqCli(args).run();
    }
}
