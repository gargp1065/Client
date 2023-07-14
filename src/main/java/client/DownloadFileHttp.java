//package client;
//
//import validation.CustomHostNameVerifier;
//import validation.FileValidation;
//
//import javax.net.ssl.HttpsURLConnection;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.logging.Logger;
//
//import static file.FileOperations.createFile;
//import static file.FileOperations.deleteFile;
//
//public class DownloadFileHttp {
//
//    private static final Logger log = Logger.getLogger(GetFileFromServer.class.getName());
//    public static void downloadFileHttp(String serverUrl, String filePath, String fileName) {
//
//        try {
//
//            System.out.println(serverUrl);
//            URL url = new URL(serverUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            if(responseCode == HttpURLConnection.HTTP_OK) {
//
//                if(!createFile(filePath + "/" + fileName)) {
//                    log.severe("File Already exists on the machine");
//                    throw new RuntimeException("File Already exists on the machine");
//                }
//                log.info("New File created");
//                InputStream inputStream = connection.getInputStream();
//                FileOutputStream outputStream = new FileOutputStream(filePath + fileName);
//
//                byte[] buffer = new byte[4096];
//                int byteRead;
//                int receivedLength = 0;
//                while((byteRead = inputStream.read(buffer)) != -1) {
//                    receivedLength += byteRead;
//                    outputStream.write(buffer, 0, byteRead);
//                }
//
//                if(!FileValidation.checkContentLength(receivedLength, Integer.parseInt(connection.getHeaderField("Content-Length")))) {
//                    deleteFile(filePath + fileName);
//                    log.severe("File validation failed!");
//                    throw new RuntimeException("File validation failed");
//                }
//
//                outputStream.close();
//                inputStream.close();
//            }
//            else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
//                log.severe("Failed to download the file. Error Code: " + responseCode +
//                        " File requested doesn't exists on the server");
//                throw new RuntimeException("Failed to download the file. Error Code: " + responseCode +
//                        " File requested doesn't exists on the server");
//            }
//            else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
//                log.severe("Failed to download the file. Error Code: " + responseCode +
//                        " Requested resource is not a file");
//                throw new RuntimeException("Failed to download the file. Error Code: " + responseCode +
//                        " Requested resource is not a file");
//            }
//            else {
//                log.severe("Failed to download the file. Error Code: " + responseCode);
//                throw new RuntimeException("Failed to download the file. Error Code: " + responseCode);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
