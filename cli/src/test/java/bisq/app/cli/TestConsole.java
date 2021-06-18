package bisq.app.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class TestConsole implements Console {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream out = new PrintStream(output);

    @Override
    public void outln(Object value) {
        out.println(value);
    }

    public String getOutput() {
        return output.toString();
    }
}
