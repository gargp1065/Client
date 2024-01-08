package alert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class Alert {


    final private static Logger logger = LogManager.getLogger(Alert.class);


//    public void raiseAnAlert(String alertCode, String alertMessage, String alertProcess, int userId) {
//        try {   // <e>  alertMessage    //      <process_name> alertProcess
//            String path = System.getenv("APP_HOME") + "alert/start.sh";
//            ProcessBuilder pb = new ProcessBuilder(path, alertCode, alertMessage, alertProcess, String.valueOf(userId));
//            Process p = pb.start();
//            logger.error(p);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line = null;
//            String response = null;
//            while ((line = reader.readLine()) != null) {
//                response += line;
//            }
//            logger.info("Alert is generated :response " + response);
//        } catch (Exception ex) {
//            logger.error("Not able to execute Alert management jar ", ex.getLocalizedMessage() + " ::: " + ex.getMessage());
//        }
//    }

    public void raiseAnAlert(final String alertId, final String alertMessage, final String alertProcess, final int userId, final String alertUrl) {

        try {
            // Specify the URL you want to connect to

            // Create a URL object
            URL url = new URL(alertUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method (GET, POST, etc.)

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            AlertDto alertDto = new AlertDto();
            alertDto.setAlertId(alertId);
            alertDto.setUserId(userId);
            alertDto.setAlertMessage(alertMessage);
            alertDto.setAlertProcess(alertProcess);
            // Read the response from the server

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(alertDto);

            try(OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestBody.getBytes());
            }
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            StringBuilder response = new StringBuilder();
//
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();

            // Print the response
            int responseCode = connection.getResponseCode();
            System.out.println("Response from raising alert: " + responseCode);

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            logger.error("Error while sending Alert Error: {} for alert: {}",
                    e.getMessage(),
                    alertId,
                    e);
        }
    }

}
