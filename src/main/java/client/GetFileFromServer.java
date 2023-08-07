package client;

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

        try {
            final String serverUrl = appConfig.getServerUrl();
            final String filePath = appConfig.getFilePath();
            final String fileName = appConfig.getFileName();
            ConfigValidation.validator(serverUrl, filePath, fileName);
            final URL url = new URL(serverUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            final int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                final File file = fileOperations.createFile(filePath, fileName);
                log.info("New File created at path "+ filePath + fileName);
                final InputStream inputStream = connection.getInputStream();
                final FileOutputStream outputStream = new FileOutputStream(file);
                final int receivedLength = fileOperations.saveFile(inputStream, outputStream);
                fileValidation.checkContentLength(receivedLength, Integer.parseInt(connection.getHeaderField("Content-Length")));
                outputStream.close();
                inputStream.close();
            }
            else {
                final String errMessage = readStringFromStream(connection.getErrorStream());
                log.error(errMessage);
                throw new RuntimeException(errMessage);
            }
        }
        catch (IOException e) {
            log.error("An exception occurred.", e);
        }
    }
}
