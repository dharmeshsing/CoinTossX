package crossing.preProcessor;

import common.OrderType;
import crossing.TestOrderEntryFactory;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListImpl;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 30/03/16.
 */
public class HawkesSimulationPreProcessorTest {

    private HawkesSimulationPreProcessor hawkesSimulationPreProcessor;

    @Before
    public void setup(){
        hawkesSimulationPreProcessor = new HawkesSimulationPreProcessor();
    }

    @Test
    public void testAddBuyOrder() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderList.add(orderEntry);
        orderBook.getBidTree().put(orderEntry.getPrice(), orderList);

        MatchingPreProcessor.MATCHING_ACTION result = hawkesSimulationPreProcessor.preProcess(orderBook, OrderBook.SIDE.BID, 2000, OrderType.LIMIT);
        assertEquals(null,result);
    }

    @Test
    public void testSellBuyOrder() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);

        MatchingPreProcessor.MATCHING_ACTION result = hawkesSimulationPreProcessor.preProcess(orderBook, OrderBook.SIDE.OFFER,2000,OrderType.LIMIT);
        assertEquals(null,result);
    }

    @Test
    public void testBuyOrderDepthTooLarge() throws Exception {
        hawkesSimulationPreProcessor.setOrderBookDepth(3);

        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setPrice(1000);
        orderList.add(orderEntry1);
        orderBook.getBidTree().put(orderEntry1.getPrice(), orderList);

        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry2.setPrice(2000);
        orderList.add(orderEntry2);
        orderBook.getBidTree().put(orderEntry2.getPrice(), orderList);

        OrderEntry orderEntry3 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry3.setPrice(3000);
        orderList.add(orderEntry3);
        orderBook.getBidTree().put(orderEntry3.getPrice(), orderList);

        MatchingPreProcessor.MATCHING_ACTION result = hawkesSimulationPreProcessor.preProcess(orderBook, OrderBook.SIDE.BID,4000,OrderType.LIMIT);
        assertEquals(MatchingPreProcessor.MATCHING_ACTION.NO_ACTION,result);
    }

    @Test
    public void testBuyOrderDepthTooLargeMarketOrder() throws Exception {
        hawkesSimulationPreProcessor.setOrderBookDepth(3);

        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setPrice(1000);
        orderList.add(orderEntry1);
        orderBook.getBidTree().put(orderEntry1.getPrice(), orderList);

        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry2.setPrice(2000);
        orderList.add(orderEntry2);
        orderBook.getBidTree().put(orderEntry2.getPrice(), orderList);

        OrderEntry orderEntry3 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry3.setPrice(3000);
        orderList.add(orderEntry3);
        orderBook.getBidTree().put(orderEntry3.getPrice(), orderList);

        MatchingPreProcessor.MATCHING_ACTION result = hawkesSimulationPreProcessor.preProcess(orderBook, OrderBook.SIDE.BID,4000,OrderType.MARKET);
        assertEquals(null,result);
    }

    @Test
    public void testSellOrderDepthTooLarge() throws Exception {
        hawkesSimulationPreProcessor.setOrderBookDepth(3);

        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setPrice(1000);
        orderList.add(orderEntry1);
        orderBook.getOfferTree().put(orderEntry1.getPrice(), orderList);

        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry2.setPrice(2000);
        orderList.add(orderEntry2);
        orderBook.getOfferTree().put(orderEntry2.getPrice(), orderList);

        OrderEntry orderEntry3 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry3.setPrice(3000);
        orderList.add(orderEntry3);
        orderBook.getOfferTree().put(orderEntry3.getPrice(), orderList);

        MatchingPreProcessor.MATCHING_ACTION result = hawkesSimulationPreProcessor.preProcess(orderBook, OrderBook.SIDE.OFFER,4000,OrderType.LIMIT);
        assertEquals(MatchingPreProcessor.MATCHING_ACTION.NO_ACTION,result);
    }

    @Test
    public void testSellOrderDepthTooLargeMarketOrder() throws Exception {
        hawkesSimulationPreProcessor.setOrderBookDepth(3);

        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setPrice(1000);
        orderList.add(orderEntry1);
        orderBook.getOfferTree().put(orderEntry1.getPrice(), orderList);

        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry2.setPrice(2000);
        orderList.add(orderEntry2);
        orderBook.getOfferTree().put(orderEntry2.getPrice(), orderList);

        OrderEntry orderEntry3 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry3.setPrice(3000);
        orderList.add(orderEntry3);
        orderBook.getOfferTree().put(orderEntry3.getPrice(), orderList);

        MatchingPreProcessor.MATCHING_ACTION result = hawkesSimulationPreProcessor.preProcess(orderBook, OrderBook.SIDE.OFFER,4000,OrderType.MARKET);
        assertEquals(null,result);
    }



}