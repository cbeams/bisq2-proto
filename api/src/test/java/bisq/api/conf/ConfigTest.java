package bisq.api.conf;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

class ConfigTest {

    @Test
    void whenConfigFileIsWellFormed_thenParsingShouldSucceed() {
        var input = """
                [app "bisq"]
                node = alice

                [node "alice"]
                host = localhost
                port = 2140

                [node "bob"]
                host = localhost
                port = 2240
                """;
        Config config = Config.parse(input);
        assertThat(config.getOptions(new Config.Section("node", "alice")))
                .containsAtLeastEntriesIn(Map.of(
                        "host", "localhost",
                        "port", "2140"));
    }

}
