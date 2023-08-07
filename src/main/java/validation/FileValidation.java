package validation;


/**
 * This class is responsible for validating the file received.
 */
public class FileValidation {

    /**
     * Validates the length of the content-received.
     * @param receivedLength length received in the body of the response.
     * @param headerLength value in the Content-Length header of the response.
     * @return boolean value determining the length of file received.
     */
    public static void checkContentLength(final int receivedLength, final int headerLength) {
        if(receivedLength != headerLength) {
//            log.error("File validation failed!");
            throw new RuntimeException("File validation failed");
        }
        return ;
    }

}
