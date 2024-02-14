import client.GetFileFromServer;
import config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;

/**
 * This is the main class. This class calls the methods responsible for client to execute.
 */
public class ClientMain {

    private static final Logger log = LogManager.getLogger(ClientMain.class);


    /**
     * The main is the entry point in the code.
     * Creates an object of AppConfig and calls downloadFile() method.
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
//        BasicConfigurator.configure();
        Configurator.initialize(null, "src/main/resources/log4j2.xml");
//        Configurator.defaultConfig()
//                .formatPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
//                .configure();
        if(args.length == 0) {
//            log.error("Configuration File Path is not provided.");
            throw new RuntimeException("Configuration File Path is not provided.");
        }
        final String CONFIG_FILE_PATH = args[0];
        try {
            AppConfig appConfig = new AppConfig(CONFIG_FILE_PATH);
            GetFileFromServer getFileFromServer = new GetFileFromServer(appConfig);
            getFileFromServer.downloadFile();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}