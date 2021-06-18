package bisq.app.cli;

import java.io.PrintStream;

public class SystemConsole implements Console {

    private final PrintStream out = System.out;

    @Override
    public void outln(Object value) {
        out.println(value);
    }
}
