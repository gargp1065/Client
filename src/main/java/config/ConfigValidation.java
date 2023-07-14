package config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * This class is responsible for validating the configs used.
 */

public class ConfigValidation {


    private static String URL_REGEX = "((https|http)\\:\\/\\/)([\\w\\d-]+\\.)*[\\w-]+[\\.\\:]\\w+([\\/\\?\\=\\&\\#\\.]?[\\w-]+)*\\/?";
    private static String FILE_NAME_REGEX = "([a-zA-Z0-9\\s_\\-\\(\\)])+[.]([a-zA-Z0-9])+$";
    private static final Pattern patternUrlRegex = Pattern.compile(URL_REGEX);
    private static final Pattern patternFileNameRegex = Pattern.compile(FILE_NAME_REGEX);
    public ConfigValidation() {

    }
    public static boolean validateServerUrl(final String serverUrl) {
        return patternUrlRegex.matcher(serverUrl).matches();
    }


    public static boolean validateFilePath(final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    // check for fileName
    // currently only allows lowercase characters, uppercase charatcers, brackets (), _, -, followed by extension
    public static boolean validateFileName(final String fileName) {
        return patternFileNameRegex.matcher(fileName).matches();
    }

}
