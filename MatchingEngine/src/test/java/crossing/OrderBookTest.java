package crossing;

import dao.StockDAO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class OrderBookTest {

    private OrderBook orderBook;
    private OrderBook expectedOrderBook;

    static {
        StockDAO.loadStocks("/home/ivanjericevich/CoinTossX/data");
    }


    @Before
    public void setup(){
        orderBook = new OrderBook(1);
        expectedOrderBook = new OrderBook(1);
    }


    @Test
    @Parameters(method = "provideData")
    public void testProcess(OrderData orderData) throws Exception {
       /* ObjectArrayList<OrderEntry> initStateList = null;
        OrderEntry aggOrder = null;
        ObjectArrayList<OrderEntry> expStateList = null;

        try {

            Stock stock = orderData.getStock();
            if (stock != null) {
                orderBook.setStock(stock);
            }

            initStateList = orderData.getInitState();
            Object[] initArr = initStateList.buffer;
            for(int i=0; i<initStateList.size(); i++){
                OrderEntry oe = (OrderEntry)initArr[i];
                orderBook.process(oe, oe.getSide());
            }

            //printQuantity();

            aggOrder = orderData.getAggOrder();
            orderBook.process(aggOrder, aggOrder.getSide());

           // printQuantity();

            expStateList = orderData.getExpState();
            Object[] expArr = expStateList.buffer;
            for(int i=0; i<expStateList.size(); i++){
                OrderEntry oe = (OrderEntry)expArr[i];
                expectedOrderBook.process(oe, oe.getSide());
            }

            //printQuantity();

            assertEquals("Test " + orderData.getTestNumber() + " failed", expectedOrderBook, orderBook);
            //assertEquals("Test Trades Size " + orderData.getTestNumber() + " failed", orderData.getTrades().size(), orderBook.getTrades().size());
            if(orderData.getTrades().size() > 0) {
                assertEquals("Test Trades" + orderData.getTestNumber() + " failed", orderData.getTrades(), orderBook.getTrades());
            }
        }finally{
            Util.freeOrderEntryMemory(initStateList);
            Util.freeOrderEntryMemory(aggOrder);
            Util.freeOrderEntryMemory(expStateList);

        }*/
    }

   /* private void printQuantity(){
        System.out.println("Buy");
        BPlusTree.BPlusTreeIterator bidTreeIterator = orderBook.getBidTreeIterator();

        bidTreeIterator.reset();
        while (bidTreeIterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = bidTreeIterator.next();

            OrderList orderList = entry.getValue();
            long size = orderList.size();
            for(int i=0; i<size; i++){
                OrderEntry existingOrder = orderList.getEntry(i);
                if(!existingOrder.isEmpty()) {
                    System.out.println(existingOrder.getQuantity());
                }
            }
        }

        System.out.println("Sell");
        BPlusTree.BPlusTreeIterator offerTreeIterator = orderBook.getOfferTreeIterator();

        offerTreeIterator.reset();
        while (offerTreeIterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = offerTreeIterator.next();

            OrderList orderList = entry.getValue();
            long size = orderList.size();
            for(int i=0; i<size; i++){
                OrderEntry existingOrder = orderList.getEntry(i);
                if(!existingOrder.isEmpty()) {
                    System.out.println(existingOrder.getQuantity());
                }
            }
        }
    }

    public static Object[] provideData() {
        OrderLoader orderLoader = new OrderLoader();
        try {
            ObjectArrayList<OrderData> orderDataList = orderLoader.readMaster();
            //TODO:Remove this. Only used for testing
            orderDataList.trimToSize();
            Object[] arr = orderDataList.buffer;
            //org.apache.commons.lang3.ArrayUtils.reverse(arr);
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }*/
}
