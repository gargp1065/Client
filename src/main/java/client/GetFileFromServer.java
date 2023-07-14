package client;

import config.AppConfig;
import validation.FileValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import static config.ConfigValidation.*;
import static file.FileOperations.createFile;
import static file.FileOperations.deleteFile;

/**
 * This class is responsible for calling the server and download the file on the machine.
 * It takes AppConfig object as an input.
 */
public class GetFileFromServer {
    private final Logger log = Logger.getLogger(GetFileFromServer.class.getName());

    private final AppConfig appConfig;

    public GetFileFromServer(final AppConfig appConfig) throws IOException {
        this.appConfig = appConfig;
    }

//    private String fileNameExtractor(String headerValue) {
//
//        String fileName = "";
//        String[] parts = headerValue.split(";");
//
//        for(String part: parts) {
//            if(part.trim().startsWith("filename")) {
//                String[] fileNameParts = part.split("=");
//                fileName = fileNameParts[1].replace("\"", "");
//                break;
//            }
//        }
//
//        return fileName;
//    }

    /**
     * Function is responsible to call the server and initiate the downloading process.
     */
    public void downloadFile() {

        final String serverUrl = appConfig.getServerUrl();
        final String filePath = appConfig.getFilePath();
        final String fileName = appConfig.getFileName();
        try {
            if(!validateServerUrl(serverUrl)) {
                log.severe("Server url is invalid");
                throw new RuntimeException("Server URL is not a valid url.");
            }
            if(!validateFilePath(filePath)) {
                log.severe("File path is invalid");
                throw new RuntimeException("Failed to download the file." +
                        " Requested resource is not a file");
            }
            if(!validateFileName(fileName)) {
                log.severe("File Name does not follow the naming convention." +
                        "Allowed is lowercase charaters (a-z), uppercase charatcer(A-Z), brackets (), underscore _" +
                        "followed by . and then the extension");
                throw new RuntimeException("File Name does not follow the naming convention.");
            }
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {

                if(!createFile(filePath, fileName)) {
                    log.severe("File Already exists on the machine");
                    throw new RuntimeException("File Already exists on the machine");
                }
                log.info("New File created at path "+ filePath + fileName);
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(filePath + fileName);

                byte[] buffer = new byte[4096];
                int byteRead;
                int receivedLength = 0;
                while((byteRead = inputStream.read(buffer)) != -1) {
                    receivedLength += byteRead;
                    outputStream.write(buffer, 0, byteRead);
                }

                if(!FileValidation.checkContentLength(receivedLength, Integer.parseInt(connection.getHeaderField("Content-Length")))) {
                    deleteFile(filePath + fileName);
                    log.severe("File validation failed!");
                    throw new RuntimeException("File validation failed");
                }

                outputStream.close();
                inputStream.close();
            }
            else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                log.severe("Failed to download the file. Error Code: " + responseCode +
                        " File requested doesn't exists on the server");
                throw new RuntimeException("Failed to download the file. Error Code: " + responseCode +
                        " File requested doesn't exists on the server");
            }
            else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                log.severe("Failed to download the file. Error Code: " + responseCode +
                        " Requested resource is not a file");
                throw new RuntimeException("Failed to download the file. Error Code: " + responseCode +
                        " Requested resource is not a file");
            }
            else {
                log.severe("Failed to download the file. Error Code: " + responseCode);
                throw new RuntimeException("Failed to download the file. Error Code: " + responseCode);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
