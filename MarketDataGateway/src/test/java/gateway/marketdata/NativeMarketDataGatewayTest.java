package gateway.marketdata;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.real_logic.aeron.driver.MediaDriver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class NativeMarketDataGatewayTest {


    private MediaDriver driver;
    private MarketDataGateway marketDataGateway;

    @Before
    public void setup() throws IOException {
        File dir = new File("/run/aeronTest/marketDataGateway");
        FileUtils.deleteDirectory(dir);

        MediaDriver.Context mdctx = new MediaDriver.Context();
        mdctx = mdctx.aeronDirectoryName("/run/aeronTest/marketDataGateway");
        driver = MediaDriver.launch(mdctx);

        marketDataGateway = new NativeMarketDataGateway();
    }

    @After
    public void tearDown(){
        driver.close();
    }

    @Test
    public void testStart() throws Exception{

        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(()->{
            marketDataGateway.initialize();
            marketDataGateway.start();
        });

        while(marketDataGateway.status() == false){
            Thread.currentThread().sleep(1000);
        }

        assertEquals(true, marketDataGateway.status());
        marketDataGateway.stop();
        assertEquals(false, marketDataGateway.status());
        es.shutdownNow();
    }
}
