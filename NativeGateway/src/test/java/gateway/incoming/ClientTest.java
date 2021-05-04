package gateway.incoming;

import com.carrotsearch.hppc.IntObjectMap;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 3/08/15.
 */
public class ClientTest {

    @Test
    public void testLoadClientData() throws Exception {
        System.out.println(Paths.get("").toAbsolutePath().getParent() + "/data");
        IntObjectMap<Client> clients = new Client().loadClientData(Paths.get("").toAbsolutePath().getParent() + "/data");
        assertEquals(11,clients.size());
    }
}