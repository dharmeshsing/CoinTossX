package crossing.strategy;

import crossing.TestOrderEntryFactory;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListImpl;
import orderBook.OrderBook;
import org.openjdk.jmh.annotations.*;

/**
 * Created by dharmeshsing on 18/07/15.
 */
@State(Scope.Thread)
public class PriceTimePriorityStrategyPerfTest {

    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private OrderBook orderBook;
    private OrderEntry aggOrder;

    @Setup(Level.Trial)
    public void setup(){
        priceTimePriorityStrategy = new PriceTimePriorityStrategy();
        setupOrderBook();
    }

    @Benchmark
    public boolean testProcess() throws Exception {
        priceTimePriorityStrategy.process(MATCHING_ACTION.AGGRESS_ORDER,orderBook,aggOrder);
        return true;
    }

    private void setupOrderBook(){
        orderBook = new OrderBook(1);
        OrderList orderList1 = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setSide((byte)1);
        orderEntry1.setPrice(100L);
        orderEntry1.setQuantity(1000);
        orderList1.add(orderEntry1);
        orderBook.getBidTree().put(orderEntry1.getPrice(), orderList1);
        orderBook.setBestVisibleBid(orderEntry1.getPrice());

        OrderList orderList2 = new OrderListImpl();
        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry2.setSide((byte)2);
        orderEntry2.setPrice(105L);
        orderEntry2.setQuantity(1000);
        orderList2.add(orderEntry2);
        orderBook.getOfferTree().put(orderEntry2.getPrice(), orderList2);
        orderBook.setBestVisibleOffer(orderEntry2.getPrice());

        aggOrder = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setSide((byte)1);
        aggOrder.setPrice(105);
    }
}