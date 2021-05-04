package gateway.incoming;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.real_logic.aeron.driver.MediaDriver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 2014/12/13.
 */
public class NativeGatewayTest {

    private MediaDriver driver;
    private TradingGateway tradingGateway;

    @Before
    public void setup() {
        MediaDriver.Context mdctx = new MediaDriver.Context();
        mdctx = (MediaDriver.Context)mdctx.aeronDirectoryName("/dev/shm/aeron");
        driver = MediaDriver.launch(mdctx);

        tradingGateway = new NativeGateway();
    }

    @After
    public void tearDown(){
        driver.close();
    }

    @Test
    public void testStart() throws Exception{

        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(()->{
            tradingGateway.initialize();
            tradingGateway.start();
        });

        while(tradingGateway.status() == false){
            Thread.currentThread().sleep(1000);
        }

        assertEquals(true, tradingGateway.status());
        tradingGateway.stop();
        assertEquals(false, tradingGateway.status());
        es.shutdownNow();
    }
}
