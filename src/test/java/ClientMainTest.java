
import client.GetFileFromServer;
import config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientMain.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.dom.*", "javax.management.*"})
public class ClientMainTest {

    private ClientMain clientMain;

    @Mock
    private GetFileFromServer getFileFromServer;
    @Mock
    private AppConfig appConfig;

    public ClientMainTest() {
        MockitoAnnotations.openMocks(this);
        clientMain = new ClientMain();
    }

    @Test(expected = RuntimeException.class)
    public void configFilePathMissing() throws IOException {
        clientMain.main(new String[0]);
    }

    @Test
    public void configFilePathHappyCase() throws Exception {
        PowerMockito.whenNew(AppConfig.class).withArguments(anyString()).thenReturn(appConfig);
        PowerMockito.whenNew(GetFileFromServer.class).withArguments(appConfig).thenReturn(getFileFromServer);
        doNothing().when(getFileFromServer).downloadFile();
        clientMain.main(new String[]{"./config"});
    }
}
