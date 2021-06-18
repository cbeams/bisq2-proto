package bisq.app.cli;

import java.io.PrintStream;

public class SystemConsole implements Console {

    private final PrintStream out = System.out;
    private final PrintStream err = System.err;

    @Override
    public void outln(Object value) {
        out.println(value);
    }

    @Override
    public void errln(Object value) {
        err.println(value);
    }
}
