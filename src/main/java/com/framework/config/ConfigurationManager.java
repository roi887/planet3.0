package com.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigurationManager {

    private static final Properties properties = new Properties();

    private ConfigurationManager() {
        // Prevent object creation
    }

    static {
        loadProperties();
    }

    private static void loadProperties() {

        String configPath =
                System.getProperty("user.dir")
                        + "/src/test/resources/config.properties";

        try (FileInputStream fis = new FileInputStream(configPath)) {

            properties.load(fis);

            System.out.println(
                    "[ConfigurationManager] Configuration loaded successfully.");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load config.properties from: "
                            + configPath,
                    e);
        }
    }

    /**
     * Get property value
     */
    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null) {

            throw new RuntimeException(
                    "Property not found in config.properties: "
                            + key);
        }

        return value.trim();
    }

    /**
     * Get property with default value
     */
    public static String get(String key, String defaultValue) {

        return properties.getProperty(key, defaultValue).trim();
    }

    /**
     * Get Integer property
     */
    public static int getInt(String key) {

        return Integer.parseInt(get(key));
    }

    /**
     * Get Boolean property
     */
    public static boolean getBoolean(String key) {

        return Boolean.parseBoolean(get(key));
    }

    /**
     * Reload properties if needed
     */
    public static void reload() {
        properties.clear();
        loadProperties();
    }
}