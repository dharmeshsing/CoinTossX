package gateway.incoming;

import com.carrotsearch.hppc.IntObjectMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 3/08/15.
 */
public class ClientTest {

    @Test
    public void testLoadClientData() throws Exception {
        IntObjectMap<Client> clients = new Client().loadClientData("/Users/dharmeshsing/Documents/Masters/Software/data");
        assertEquals(3,clients.size());
    }
}