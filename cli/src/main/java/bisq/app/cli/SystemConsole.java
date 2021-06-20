package bisq.app.cli;

import java.io.PrintStream;

class SystemConsole implements Console {

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

    @Override
    public PrintStream getOut() {
        return out;
    }

    @Override
    public PrintStream getErr() {
        return err;
    }
}
