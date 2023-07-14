package config;

import java.beans.BeanProperty;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The class is responsible for loading the configs from config.properties file.
 */
public class AppConfig {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
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
