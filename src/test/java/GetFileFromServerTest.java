import client.GetFileFromServer;
import config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GetFileFromServer.class)
public class GetFileFromServerTest {

    GetFileFromServer getFileFromServer;

    @Mock
    AppConfig appConfig;

    @Mock
    URL url;

    @Mock
    HttpURLConnection httpURLConnectionMock;

    @Mock
    File file;

    public GetFileFromServerTest() throws IOException {

        MockitoAnnotations.openMocks(this);
        appConfig = Mockito.mock(AppConfig.class);
        file = Mockito.mock(File.class);
        httpURLConnectionMock = Mockito.mock(HttpURLConnection.class);
        url = Mockito.mock(URL.class);
        getFileFromServer = new GetFileFromServer(appConfig);

    }

    @Test
    public void invalidPathFromConfig() {

        Mockito.when(appConfig.getFilePath()).thenReturn("src/package/");
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost.com:8080/download");
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
    }

    @Test
    public void invalidUrlFromConfig() throws IOException {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("ftp://localhost.com:8080/download");
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        tempDir.deleteOnExit();
    }

    @Test
    public void invalidFileNameFromConfig() throws IOException {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testF*&.json");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost.com:8080/download");
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        tempDir.deleteOnExit();
    }

    @Test
    public void downloadSuccess() throws Exception {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost:8080/download");
        PowerMockito.whenNew(URL.class).withArguments("http://localhost:8080/download").thenReturn(url);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnectionMock);
        Mockito.when(httpURLConnectionMock.getResponseCode()).thenReturn(HttpsURLConnection.HTTP_OK);
        InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes());
        Mockito.when(httpURLConnectionMock.getInputStream()).thenReturn(anyInputStream);
        Mockito.when(httpURLConnectionMock.getHeaderField("Content-Length")).thenReturn(String.valueOf(9));
        getFileFromServer.downloadFile();
        verify(httpURLConnectionMock).getResponseCode();
        verify(url).openConnection();
        verify(httpURLConnectionMock).getInputStream();
        tempDir.deleteOnExit();

    }

    @Test
    public void downloadFailure_ContentLength() throws Exception {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost:8080/download");
        PowerMockito.whenNew(URL.class).withArguments("http://localhost:8080/download").thenReturn(url);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnectionMock);
        Mockito.when(httpURLConnectionMock.getResponseCode()).thenReturn(HttpsURLConnection.HTTP_OK);
        InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes());
        Mockito.when(httpURLConnectionMock.getInputStream()).thenReturn(anyInputStream);
        Mockito.when(httpURLConnectionMock.getHeaderField("Content-Length")).thenReturn(String.valueOf(8));
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        tempDir.deleteOnExit();
    }

    @Test
    public void downloadFailure_FileAlreadyExists() throws Exception {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        File filePath = new File(myTempDir.toString() + "/" + "testFile.txt");
        filePath.createNewFile();
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost:8080/download");
        PowerMockito.whenNew(URL.class).withArguments("http://localhost:8080/download").thenReturn(url);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnectionMock);
        Mockito.when(httpURLConnectionMock.getResponseCode()).thenReturn(HttpsURLConnection.HTTP_OK);
        Mockito.when(file.createNewFile()).thenReturn(false);
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        tempDir.deleteOnExit();
    }

    @Test
    public void downloadFailure_FileNotExistOnServer() throws Exception {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost:8080/download");
        PowerMockito.whenNew(URL.class).withArguments("http://localhost:8080/download").thenReturn(url);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnectionMock);
        Mockito.when(httpURLConnectionMock.getResponseCode()).thenReturn(HttpsURLConnection.HTTP_NOT_FOUND);
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        verify(httpURLConnectionMock).getResponseCode();
        verify(url).openConnection();
        tempDir.deleteOnExit();

    }

    @Test
    public void downloadFailure_ResourceIsNotAFile() throws Exception {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost:8080/download");
        PowerMockito.whenNew(URL.class).withArguments("http://localhost:8080/download").thenReturn(url);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnectionMock);
        Mockito.when(httpURLConnectionMock.getResponseCode()).thenReturn(HttpsURLConnection.HTTP_BAD_REQUEST);
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        verify(httpURLConnectionMock).getResponseCode();
        verify(url).openConnection();
        tempDir.deleteOnExit();

    }

    @Test
    public void downloadFailure_NotKnown() throws Exception {

        Path myTempDir = Files.createTempDirectory("myTemp");
        File tempDir = new File(myTempDir.toString());
        Mockito.when(appConfig.getFilePath()).thenReturn(myTempDir.toString());
        Mockito.when(appConfig.getFileName()).thenReturn("testFile.txt");
        Mockito.when(appConfig.getServerUrl()).thenReturn("http://localhost:8080/download");
        PowerMockito.whenNew(URL.class).withArguments("http://localhost:8080/download").thenReturn(url);
        Mockito.when(url.openConnection()).thenReturn(httpURLConnectionMock);
        Mockito.when(httpURLConnectionMock.getResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAVAILABLE);
        assertThrows(RuntimeException.class, getFileFromServer::downloadFile);
        verify(httpURLConnectionMock).getResponseCode();
        verify(url).openConnection();
        tempDir.deleteOnExit();

    }
}
