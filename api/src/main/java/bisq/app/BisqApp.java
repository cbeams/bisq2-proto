package bisq.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

import static java.lang.String.format;

/**
 * Marker interface used to identify Bisq applications runnable via a main method. The
 * {@link #APP_INFO} field provides application name and version information.
 * @see Info
 */
public interface BisqApp {

    Info APP_INFO = new Info();

    /**
     * Static information about the currently running {@link BisqApp} similar to what
     * would be found in a jar's {@literal META-INF/MANIFEST.MF} file. This custom
     * approach is used in order to support running applications without packaging them as
     * jars, e.g. via the Gradle build with a `run` task or directly within IDEA.
     * Applications making use of this class must provide a properties file on the
     * classpath at {@value PROPERTIES_FILE_PATH} containing {@value NAME_PROPERTY} and
     * {@value VERSION_PROPERTY} properties with corresponding {@literal ${...}
     * placeholders as values to be replaced at build time.
     */
    class Info {
        private static final String PROPERTIES_FILE_PATH = "/app-info.properties";
        private static final String NAME_PROPERTY = "name";
        private static final String VERSION_PROPERTY = "version";

        private final String name;
        private final String version;

        public Info() {
            InputStream in = BisqApp.class.getResourceAsStream(PROPERTIES_FILE_PATH);
            if (in == null)
                throw new IllegalStateException("required resource not found: " + PROPERTIES_FILE_PATH);
            try {
                var properties = new Properties();
                properties.load(in);
                this.version = properties.getProperty(VERSION_PROPERTY);
                if (version == null)
                    throw new IllegalStateException(
                            format("required property '%s' not found in %s", VERSION_PROPERTY, PROPERTIES_FILE_PATH));
                this.name = properties.getProperty(NAME_PROPERTY);
                if (name == null)
                    throw new IllegalStateException(
                            format("required property '%s' not found in %s", NAME_PROPERTY, PROPERTIES_FILE_PATH));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }
    }
}
