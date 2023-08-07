package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * This class is responsible for validating the configs used.
 */

public class ConfigValidation {


    private static final Logger log = LogManager.getLogger(ConfigValidation.class); // remove as not being used
    private static final String URL_REGEX = "((https|http)\\:\\/\\/)([\\w\\d-]+\\.)*[\\w-]+[\\.\\:]\\w+([\\/\\?\\=\\&\\#\\.]?[\\w-]+)*\\/?";
    private static final String FILE_NAME_REGEX = "([a-zA-Z0-9\\s_\\-\\(\\)])+[.]([a-zA-Z0-9])+$";
    private static final Pattern patternUrlRegex = Pattern.compile(URL_REGEX);
    private static final Pattern patternFileNameRegex = Pattern.compile(FILE_NAME_REGEX);

    /**
     * Validate the server Url from the config.
     * @param serverUrl: URL of the server where the requests hit.
     * @return boolean value is returned. True if valid url and false if url is invalid.
     */
    public static boolean validateServerUrl(final String serverUrl) {
        return patternUrlRegex.matcher(serverUrl).matches();
    }

    /**
     * Validate the filePath from the config.
     * @param filePath: File path where the file is located.
     * @return boolean value is returned. True if valid file path and false if file path is invalid.
     */
    public static boolean validateFilePath(final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        final Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * check for fileName
     * currently only allows lowercase characters, uppercase characters, brackets (), _, -, followed by extension
     * @param fileName: Name of the file.
     * @return boolean value is returned. True if valid file name and false if file name is invalid.
     */
    public static boolean validateFileName(final String fileName) {
        return patternFileNameRegex.matcher(fileName).matches();
    }

    /**
     * A common function that calls individual function to check the validation.
     * @param serverUrl: URL of the server where the requests hit.
     * @param filePath: File path where the file is located.
     * @param fileName: Name of the file.
     */
    public static void validator(final String serverUrl, final String filePath, final String fileName) { // better to have a single function and call it thrice

        if(!validateServerUrl(serverUrl)) {
//            log.error("Server url is invalid");
            throw new RuntimeException("Server URL is not a valid url."); // logging error and throwing error with custom msg is anti-pattern https://rolf-engelhard.de/2013/04/logging-anti-patterns-part-ii/#:~:text=Log%20and%20Throw&text=Never%20do%20both%20at%20a,might%20log%20that%20exception%20too.
        }

        if(!validateFilePath(filePath)) {
//            log.error("File path is invalid");
            throw new RuntimeException("File path is invalid"); //same as above
        }
        if(!validateFileName(fileName)) {
//            log.error("File Name does not follow the naming convention." +
//                    "Allowed is lowercase characters (a-z), uppercase character(A-Z), brackets (), underscore _" +
//                    "followed by . and then the extension");
            throw new RuntimeException("File Name does not follow the naming convention." +
                    "Allowed is lowercase characters (a-z), uppercase character(A-Z), brackets (), underscore _" +
                    "followed by . and then the extension"); //same as above
        }
    }
}
