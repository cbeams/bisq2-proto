package bisq.api.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

/**
 * A general-purpose (Bisq-agnostic) config file parser modeled after the INI-like format
 * used by git-config.
 */
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private final Map<Section, Map<String, String>> data;

    public Config() {
        this(new HashMap<>());
    }

    public Config(Map<Section, Map<String, String>> data) {
        this.data = data;
    }

    public static Config parse(File file) {
        try {
            return parse(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Config parse(String config) {
        return parse(new StringReader(config));
    }

    public static Config parse(Reader reader) {
        Map<Section, Map<String, String>> data = new HashMap<>();
        Pattern sectionPattern = Pattern.compile("^\\[(?<type>\\S+)( \"(?<name>\\S+)\")?]$");
        try (BufferedReader lineReader = new BufferedReader(reader)) {
            String line;
            int lineNo = 0;
            var section = new Section("", "");
            while ((line = lineReader.readLine()) != null) {
                line = line.trim();
                lineNo++;
                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                Matcher sectionMatcher = sectionPattern.matcher(line);
                if (sectionMatcher.find()) {
                    var type = sectionMatcher.group("type");
                    var name = sectionMatcher.group("name");
                    section = new Section(type, name != null ? name : "");
                    if (!data.containsKey(section))
                        data.put(section, new HashMap<>());
                    continue;
                }

                if (line.contains("=")) {
                    var entry = line.split("=");
                    var key = entry[0].trim();
                    var val = entry[1].trim();
                    data.get(section).put(key, val);
                    log.debug("detected option '{}={}' in config file section {}", key, val, section);
                    continue;
                }

                throw new IllegalArgumentException(format("malformed input at line %d: %s", lineNo, line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new Config(data);
    }

    public boolean hasOptions(Section section) {
        return data.containsKey(section);
    }

    public Map<String, String> getOptions(Section section) {
        return data.get(section);
    }

    public Map<Section, Map<String, String>> getSectionsOfType(String type) {
        return data.entrySet().stream()
                .filter(entry -> entry.getKey().type.equals(type))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, String> getOptions(String sectionType, String sectionName) {
        Section section = new Section(sectionType, sectionName);
        return data.containsKey(section) ? data.get(section) : new HashMap<>();
    }

    public static record Section(String type, String name) {
        @Override
        public String toString() {
            return format("[%s \"%s\"]", type, name);
        }
    }
}
