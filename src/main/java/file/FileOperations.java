package file;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for handling file operations required.
 */
public class FileOperations {

    /**
     * This method checks and creates a new file with fileName in filePath directory
     * @param filePath contains the directory path
     * @param fileName contain the file name to be saved
     * @return boolean value determining the successful creation of a file.
     * @throws IOException
     */
    public static boolean createFile(final String filePath, final String fileName) throws IOException {
        File file = new File(filePath + "/" + fileName);
        if(file.exists()) {
            int lastIndex = fileName.lastIndexOf(".");
            String fileExtension = fileName.substring(lastIndex + 1);
            String newFileName = fileName.substring(0, lastIndex) + "(1)." + fileExtension;
//            final String ext = fileName.split(".")[0];
//            String newFileName = fileName.split(".")[0];
            File newFile = new File(filePath + "/" + newFileName);
            file.renameTo(newFile);
        }
        return file.createNewFile();
    }

    /**
     *
     * @param fileName contain the fileName to be deleted.
     * @return boolean value determining successful deletion of a file
     * @throws IOException
     */

    public static boolean deleteFile(final String fileName) throws IOException {
        File file = new File(fileName);
        return file.delete();
    }

}
