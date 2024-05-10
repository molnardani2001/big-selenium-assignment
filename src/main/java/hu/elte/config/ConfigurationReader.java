package hu.elte.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigurationReader {

    public static <T> T readValue(String key, Class<T> valueType) {
        InputStream inputStream = ConfigurationReader.class.getResourceAsStream("/application.yaml");
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(inputStream);

        String[] splitted = key.split("\\.");
        for(int i = 0; i < splitted.length - 1; ++i) {
            yamlMap = (LinkedHashMap<String, Object>) yamlMap.get(splitted[i]);
        }

        Object value = yamlMap.get(splitted[splitted.length - 1]);

        if(valueType.isInstance(value)) { //isInstance checks if the value is null as well
            return valueType.cast(value);
        }else {
            return null;
        }
    }
}
