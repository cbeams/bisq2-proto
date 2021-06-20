package bisq.app.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class TestConsole implements Console {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errors = new ByteArrayOutputStream();
    private final PrintStream out = new PrintStream(output);
    private final PrintStream err = new PrintStream(errors);

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

    public String output() {
        return output.toString();
    }

    public String errors() {
        return errors.toString();
    }
}
