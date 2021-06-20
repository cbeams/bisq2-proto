package bisq.app.cli;

import java.io.PrintStream;

interface Console {

    void outln(Object value);

    void errln(Object value);

    PrintStream getOut();

    PrintStream getErr();
}
