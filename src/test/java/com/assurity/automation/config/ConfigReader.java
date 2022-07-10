package com.assurity.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private final Properties properties;

    public ConfigReader() {
        properties = new Properties();
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(APPLICATION_PROPERTIES);
            properties.load(inputStream);
            logger.info(APPLICATION_PROPERTIES + " loaded successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
