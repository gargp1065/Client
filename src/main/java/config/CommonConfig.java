package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CommonConfig {
    static String commonConfigFile;
    private static final Logger logger = LogManager.getLogger(CommonConfig.class);
    private static final Properties properties = new Properties();


    static {
        try {
//            logger.info("Common config file :{}", commonConfigFile );
            commonConfigFile = System.getenv("commonConfigurationFilePath");
//            logger.info("Common config file :{}", commonConfigFile );
            commonConfigFile = commonConfigFile.replace("${APP_HOME}", System.getenv("APP_HOME"));
            logger.info("Common config file :{}", commonConfigFile );
            String propFileName = commonConfigFile;
            FileInputStream input = new FileInputStream(propFileName);
            if(input != null) {
                properties.load(input);
            } else {
                throw new FileNotFoundException("Property File '" + propFileName + "' not found in the classpath.");
            }
        } catch (IOException io) {
            logger.error(io.toString(), (Throwable) io);
        }
    }

    public static String getProperty(String key) {

        String value = properties.getProperty(key);
        if(value == null) {
            try {
                throw new Exception(key + " not found!");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return value;
    }
}
