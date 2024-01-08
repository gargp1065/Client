package validation;


import alert.Alert;
import client.GetFileFromServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for validating the file received.
 */
public class FileValidation {

    private static final Alert alert = new Alert();
    private static final Logger log = LogManager.getLogger(Alert.class);
    /**
     * Validates the length of the content-received.
     * @param receivedLength length received in the body of the response.
     * @param headerLength value in the Content-Length header of the response.
     * @return boolean value determining the length of file received.
     */
    public static void checkContentLength(final int receivedLength, final int headerLength, final String alertUrl) {
        if(receivedLength != headerLength) {
            log.error("File validation failed!");
            alert.raiseAnAlert("1704", "", "", 0, alertUrl);
            System.exit(1);
        }
        return ;
    }

}
