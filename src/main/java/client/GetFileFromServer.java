package client;

import alert.Alert;
import audit.AuditManagement;
import config.AppConfig;
import config.ConfigValidation;
import connection.MySqlConnection;
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
import java.sql.Connection;
import java.util.Date;

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
    private final AuditManagement auditManagement;

    private final MySqlConnection conn;

    long executionFinishTime;
    long executionFinalTime;

    public GetFileFromServer(final AppConfig appConfig) throws IOException {
        this.appConfig = appConfig;
        this.conn = new MySqlConnection();
        fileOperations = new FileOperations();
        fileValidation = new FileValidation();
        auditManagement = new AuditManagement();
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

    public void downloadFile() throws Exception {
        final Date date = new Date();
        final long executionStartTime = date.getTime();
        log.info("Execution Start Time = " + executionStartTime);
        log.info("Download File function started.");
        Connection dbConnection = conn.getConnection(appConfig.getDecryptorPath());
        // initial entry
        auditManagement.createAudit(201, "INITIAL", "NA", "File Download",
                "EIR HTTP Client", dbConnection);
        String alertUrl = null;
        try {
            log.info("Reading Config Values");
            final String serverUrl = appConfig.getServerUrl();
            String filePath = appConfig.getFilePath();
            filePath = filePath.replace("${DATA_HOME}", System.getenv("DATA_HOME"));
            final String fileName = appConfig.getFileName();
            alertUrl = appConfig.getAlertUrl();
            log.info("Validation config values");
            ConfigValidation.validator(serverUrl, filePath, fileName, alertUrl, dbConnection, auditManagement, executionStartTime);
            log.info("Config values validated");
            log.info("Starting url connection for url: {}", serverUrl);
            final URL url = new URL(serverUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            final int responseCode = connection.getResponseCode();
            log.info("Connection Established with url: {}", serverUrl);
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
                Date finishDate = new Date();
                executionFinishTime = finishDate.getTime();
                log.info("First Execution Finish Time for deletion " + executionFinishTime);
                log.info("Subtract Execution Finish Time deletion " + Math.subtractExact(executionFinishTime, executionStartTime));
                executionFinalTime = executionFinishTime - executionStartTime;
                log.info("Execution Finish Time for deletion " + executionFinalTime);
                auditManagement.updateAudit(200, "SUCCESS", "NA",  executionFinalTime, "File Download is completed.", receivedLength ,dbConnection);
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                log.error("File does not exists on the server.");
                alert.raiseAnAlert("alert1702", "", "", 0, alertUrl);
                Date finishDate = new Date();
                executionFinishTime = finishDate.getTime();
                log.info("First Execution Finish Time for deletion " + executionFinishTime);
                log.info("Subtract Execution Finish Time deletion " + Math.subtractExact(executionFinishTime, executionStartTime));
                executionFinalTime = executionFinishTime - executionStartTime;
                log.info("Execution Finish Time for deletion " + executionFinalTime);
                auditManagement.updateAudit(501, "FAIL", "File does not exists on the server.",  executionFinalTime, dbConnection);
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
            Date finishDate = new Date();
            executionFinishTime = finishDate.getTime();
            log.info("First Execution Finish Time for deletion " + executionFinishTime);
            log.info("Subtract Execution Finish Time deletion " + Math.subtractExact(executionFinishTime, executionStartTime));
            executionFinalTime = executionFinishTime - executionStartTime;
            log.info("Execution Finish Time for deletion " + executionFinalTime);
            auditManagement.updateAudit(501, "FAIL", "The server is not reachable.",  executionFinalTime, dbConnection);
            System.exit(1);
        } catch (Exception e) {
            log.error("An exception occurred. {}", e.getLocalizedMessage());
            alert.raiseAnAlert("alert1705", e.getLocalizedMessage(), "", 0, alertUrl);
            String err = "An exception occurred. {} " +  e.getLocalizedMessage();
            Date finishDate = new Date();
            executionFinishTime = finishDate.getTime();
            log.info("First Execution Finish Time for deletion " + executionFinishTime);
            log.info("Subtract Execution Finish Time deletion " + Math.subtractExact(executionFinishTime, executionStartTime));
             executionFinalTime = executionFinishTime - executionStartTime;
            log.info("Execution Finish Time for deletion " + executionFinalTime);
            auditManagement.updateAudit(501, "FAIL", err,  executionFinalTime, dbConnection);
            System.exit(1);
        }
    }
}
