package crossing;

import com.carrotsearch.hppc.LongObjectHashMap;
import common.MessageGenerator;
import dao.OrderBookDAO;
import dao.TraderDAO;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;
import sbe.reader.BusinessRejectReader;
import sbe.reader.ExecutionReportReader;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class CrossingProcessorReportTest {

    private static final int STOCK_ID = 1;
    private CrossingProcessor crossingProcessor;

    @Before
    public void setup() throws IOException {
        String dataPath = "/home/ivanjericevich/CoinTossX/data";
        LongObjectHashMap<OrderBook> orderBooks = OrderBookDAO.loadOrderBooks(dataPath);
        TraderDAO.loadTraders(dataPath);
        crossingProcessor = new CrossingProcessor(orderBooks);
    }

    @Test
    public void testOrderCancelRequest() throws Exception {

        DirectBuffer msg = MessageGenerator.buildOrderCancelRequest();
        DirectBuffer response = crossingProcessor.processOrder(msg);

        ExecutionReportReader executionReportReader = new ExecutionReportReader();
        StringBuilder sb = executionReportReader.read(response);
        System.out.println(sb.toString());
        assertNotNull(sb);
    }

    @Test
    public void testOrderCancelRequestRejected() throws Exception {

        DirectBuffer msg = MessageGenerator.buildOrderCancelRequestInvalidSecurity();
        DirectBuffer response = crossingProcessor.processOrder(msg);

        BusinessRejectReader businessRejectReader = new BusinessRejectReader();
        StringBuilder sb = businessRejectReader.read(response);
        System.out.println(sb.toString());
        assertNotNull(sb);
    }


}
