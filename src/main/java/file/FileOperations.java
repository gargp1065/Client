package file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public File createFile(final String filePath, final String fileName) throws IOException {
        final File file = new File(filePath + "/" + fileName);
        if(file.exists()) {
            final int lastIndex = fileName.lastIndexOf(".");
            final String fileExtension = fileName.substring(lastIndex + 1);
            final String newFileName = fileName.substring(0, lastIndex) + "(1)." + fileExtension;
            final File newFile = new File(filePath + "/" + newFileName);
            file.renameTo(newFile);
        }
        return file;
    }

    /**
     * Delete a file with a given file Name and
     * @param fileName contain the fileName to be deleted.
     * @return boolean value determining successful deletion of a file
     * @throws IOException
     */

    public boolean deleteFile(final String fileName) throws IOException {
        final File file = new File(fileName);
        return file.delete();
    }

    /**
     * Saves the content, received from server to a file.
     * @param inputStream
     * @param outputStream
     * @return the number of bytes written.
     * @throws IOException
     */

    public int saveFile(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int byteRead;
        int receivedLength = 0;
        while((byteRead = inputStream.read(buffer)) != -1) {
            receivedLength += byteRead;
            outputStream.write(buffer, 0, byteRead);
        }
        return receivedLength;
    }

}
