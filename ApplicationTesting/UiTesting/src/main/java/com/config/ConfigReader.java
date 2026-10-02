package com.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final Map<String, String> PROPERTY_ALIASES = new HashMap<>();

    static {
        PROPERTY_ALIASES.put("baseUrl", "base.url");
        PROPERTY_ALIASES.put("base.url", "base.url");
        PROPERTY_ALIASES.put("browser", "browser.type");
        PROPERTY_ALIASES.put("browserType", "browser.type");
        PROPERTY_ALIASES.put("headless", "headless.mode");
        PROPERTY_ALIASES.put("headlessMode", "headless.mode");

        String env = System.getProperty("env", "qa");
        String configFilePath = "src/test/resources/configurations/config_" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(new File(configFilePath))) {
            properties.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config file: " + configFilePath, e);
        }
    }

    public static String get(String key) {
        String resolvedKey = resolveKey(key);
        String value = properties.getProperty(resolvedKey);
        if (value == null) {
            throw new RuntimeException("Property " + key + " is not specified in the config file");
        }
        return value.trim();
    }

    private static String resolveKey(String key) {
        if (key == null) {
            return null;
        }
        String trimmedKey = key.trim();
        if (properties.containsKey(trimmedKey)) {
            return trimmedKey;
        }
        if (PROPERTY_ALIASES.containsKey(trimmedKey)) {
            return PROPERTY_ALIASES.get(trimmedKey);
        }
        String normalized = trimmedKey.replace(".", "").replace("-", "").replace("_", "");
        for (String existingKey : properties.stringPropertyNames()) {
            if (existingKey.replace(".", "").replace("-", "").replace("_", "").equalsIgnoreCase(normalized)) {
                return existingKey;
            }
        }
        return trimmedKey;
    }
}
