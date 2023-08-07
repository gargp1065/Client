package config;

import client.GetFileFromServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The class is responsible for loading the configs from config.properties file.
 */
public class AppConfig {

    private final String CONFIG_FILE_PATH;
    private static final Properties properties = new Properties();
    private static final Logger log = LogManager.getLogger(AppConfig.class);

    public AppConfig(final String configFilePath) throws IOException {

        CONFIG_FILE_PATH = configFilePath;
        File file = new File(CONFIG_FILE_PATH);
        if(!file.exists()) {
            log.error("The path for config file is incorrect.");
            throw new RuntimeException("The path for config file is incorrect.");
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!properties.containsKey("server.url")) {
            throw new IOException("Missing configuration: server.url");
        }

        if (!properties.containsKey("file.path")) {
            throw new IOException("Missing configuration: file.path");
        }

        if (!properties.containsKey("file.name")) {
            throw new IOException("Missing configuration: file.name");
        }

    }

    public String getFilePath() {
        return properties.getProperty("file.path");
    }

    public String getFileName() {
        return properties.getProperty("file.name");
    }

    public String getServerUrl() {
        return properties.getProperty("server.url");
    }

}
