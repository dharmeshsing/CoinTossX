package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sbe.msg.marketData.TradingSessionEnum;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 26/12/16.
 */
public class OffHeapStorageTest {

    private OffHeapStorage offHeapStorage;
    private String dataPath = "/Users/dharmeshsing/Documents/Masters/tmp/data";

    @Before
    public void setUp() throws Exception {
        offHeapStorage = new OffHeapStorage();
        offHeapStorage.init(dataPath,false);
    }

    @After
    public void tearDown() throws Exception {
        offHeapStorage.clearPrices();
        offHeapStorage.clearOrders();
        offHeapStorage.clearTrades();
        offHeapStorage.close();
        Path path = FileSystems.getDefault().getPath(dataPath + "/webStorage.db");
        Files.deleteIfExists(path);
        offHeapStorage = null;
    }

    @Test
    public void testSetSymbolStatus() throws Exception {
        offHeapStorage.setSymbolStatus(1, TradingSessionEnum.ContinuousTrading);
        assertEquals(TradingSessionEnum.ContinuousTrading,offHeapStorage.getSymbolStatus(1));
    }

    @Test
    public void testUpdateHawkesSimulation() throws Exception {
        offHeapStorage.updateHawkesSimulation(1, true);
        assertEquals(true,offHeapStorage.getHawkesSimulation(1));
    }

    @Test
    public void testUpdateHawkesSimulationStatus() throws Exception {
        offHeapStorage.updateHawkesSimulationStatus(true);
        assertEquals(true,offHeapStorage.getHawkesSimulationSttaus());
    }

    @Test
    public void testUpdateWarmupSimulation() throws Exception {

    }

    @Test
    public void testUpdateWarmupSimulationStatus() throws Exception {

    }

    @Test
    public void testGetWarmupSimulationSttaus() throws Exception {

    }

    @Test
    public void testGetLOBStatus() throws Exception {

    }

    @Test
    public void testSetLOBStatus() throws Exception {

    }

    @Test
    public void testAddPrice() throws Exception {

    }

    @Test
    public void testGetPrices() throws Exception {

    }

    @Test
    public void testClearPrices() throws Exception {

    }

    @Test
    public void testClearPrices1() throws Exception {

    }

    @Test
    public void testAddBidOrder() throws Exception {

    }

    @Test
    public void testAddOfferOrder() throws Exception {

    }

    @Test
    public void testAddSubmittedOrder() throws Exception {

    }

    @Test
    public void testAddTrades() throws Exception {

    }

    @Test
    public void testGetBidOrders() throws Exception {

    }

    @Test
    public void testGetOfferOrders() throws Exception {

    }

    @Test
    public void testGetSubmittedOrders() throws Exception {

    }

    @Test
    public void testGetTrades() throws Exception {

    }

    @Test
    public void testClearBidOrders() throws Exception {

    }

    @Test
    public void testClearOfferOrders() throws Exception {

    }

    @Test
    public void testClearSubmittedOrders() throws Exception {

    }

    @Test
    public void testClearTrades() throws Exception {

    }

    @Test
    public void testClearTrades1() throws Exception {

    }

    @Test
    public void testClearOrders() throws Exception {

    }

    @Test
    public void testIsSimultationComplete() throws Exception {

    }

    @Test
    public void testTwoWriters() throws Exception {
        Thread.sleep(10000);
        OffHeapStorage offHeapStorage2 = new OffHeapStorage();
        offHeapStorage2.init(dataPath,true);

        Runnable r = () -> {
            for(int i=0; i<10; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Write " + i);
                if(i % 2 == 0) {
                    offHeapStorage.updateHawkesSimulationStatus(true);
                }else{
                    offHeapStorage.updateHawkesSimulationStatus(false);
                }
            }
        };

        Runnable r2 = () -> {
            for(int i=0; i<10; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Read " + i);
                System.out.println(offHeapStorage2.getHawkesSimulationSttaus());
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        Thread.sleep(10000);

    }
}