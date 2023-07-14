import client.GetFileFromServer;
import config.AppConfig;

import java.io.IOException;

/**
 * This is the main class. This class calls the methods responsible for client to execute.
 */
public class ClientMain {

    /**
     * The main is the entry point in the code.
     * Creates an object of AppConfig and calls downloadFile() method.
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {

        AppConfig appConfig = new AppConfig();
        GetFileFromServer getFileFromServer = new GetFileFromServer(appConfig);
        getFileFromServer.downloadFile();
    }
}