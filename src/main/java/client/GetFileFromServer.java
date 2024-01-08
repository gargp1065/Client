package client;

import alert.Alert;
import config.AppConfig;
import config.ConfigValidation;
import file.FileOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validation.FileValidation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * This class is responsible for calling the server and download the file on the machine.
 * It takes AppConfig object as an input.
 */
public class GetFileFromServer {

    private static final Logger log = LogManager.getLogger(GetFileFromServer.class);
    private final AppConfig appConfig;
    private final FileOperations fileOperations;
    private final FileValidation fileValidation;
    private final Alert alert = new Alert();

    public GetFileFromServer(final AppConfig appConfig) throws IOException {
        this.appConfig = appConfig;
        fileOperations = new FileOperations();
        fileValidation = new FileValidation();
    }

    // reading error messages from the server
    private static String readStringFromStream(final InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder stringBuilder = new StringBuilder();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }
        return stringBuilder.toString();
    }

    /**
     * Function is responsible to call the server and initiate the downloading process.
     */

    public void downloadFile() {
        log.info("Download File function started.");
        String alertUrl = null;
        try {
            log.info("Reading Config Values");
            final String serverUrl = appConfig.getServerUrl();
            String filePath = appConfig.getFilePath();
            filePath = filePath.replace("${DATA_HOME}", System.getenv("DATA_HOME"));
            final String fileName = appConfig.getFileName();
            alertUrl = appConfig.getAlertUrl();
            log.info("Validation config values");
            ConfigValidation.validator(serverUrl, filePath, fileName, alertUrl);
            log.info("Config values validated");
            log.info("Starting url connection");
            final URL url = new URL(serverUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            final int responseCode = connection.getResponseCode();
            log.info("Connection Established");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                final File file = fileOperations.createFile(filePath, fileName);
                log.info("New File created at path " + filePath + fileName);
                final InputStream inputStream = connection.getInputStream();
                final FileOutputStream outputStream = new FileOutputStream(file);
                final int receivedLength = fileOperations.saveFile(inputStream, outputStream);
                fileValidation.checkContentLength(receivedLength, Integer.parseInt(connection.getHeaderField("Content-Length")), alertUrl);
                outputStream.close();
                inputStream.close();
                log.info("File Download Successful.");
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                log.error("File does not exists on the server.");
                alert.raiseAnAlert("alert1702", "", "", 0, alertUrl);
                System.exit(1);
                ;
            } else {
                final String errMessage = readStringFromStream(connection.getErrorStream());
                log.error(errMessage);
                throw new RuntimeException(errMessage);
            }
        } catch (UnknownHostException e) {
            log.error("The server is not reachable.");
            alert.raiseAnAlert("alert1701", "", "", 0, alertUrl);
            System.exit(1);
        } catch (Exception e) {
            log.error("An exception occurred. {}", e.getLocalizedMessage());
            alert.raiseAnAlert("alert1705", e.getLocalizedMessage(), "", 0, alertUrl);
            System.exit(1);
        }
    }
}
