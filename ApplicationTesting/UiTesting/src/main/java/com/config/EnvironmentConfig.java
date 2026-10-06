package com.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentConfig {

    private static final String CONFIG_DIRECTORY = "src/test/resources/configurations";

    public static String readData(String environment, String text) {
        if (environment == null || environment.trim().isEmpty()) {
            throw new IllegalArgumentException("Environment cannot be null or empty");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Property name cannot be null or empty");
        }

        String configFileName = "config_" + environment.trim().toLowerCase() + ".properties";
        File configFile = new File(CONFIG_DIRECTORY, configFileName);

        if (!configFile.exists()) {
            throw new IllegalArgumentException("Config file not found: " + configFile.getAbsolutePath());
        }

        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(configFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read config file: " + configFile.getAbsolutePath(), e);
        }

        String value = properties.getProperty(text.trim());
        if (value == null) {
            throw new RuntimeException("Property '" + text + "' not found in config file: " + configFileName);
        }

        return value.trim();
    }
}
