package sbe.reader;

import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.OrderCancelRejectBuilder;
import sbe.msg.OrderBookEnum;
import sbe.msg.OrderCancelRejectEncoder;
import sbe.msg.RejectCode;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 29/10/15.
 */
public class OrderCancelRejectReaderTest {
    @Test
    public void testRead() throws Exception {
        OrderCancelRejectReader orderCancelRejectReader = new OrderCancelRejectReader();
        DirectBuffer buffer = build();

        StringBuilder sb = orderCancelRejectReader.read(buffer);
        assertEquals("PartitionId=1SequenceNumber=1ClientOrderId=2                   OrderId=1TransactTime=10000RejectCode=InvalidCompIDOrPasswordOrderBook=Regular",sb.toString());

    }

    private DirectBuffer build(){
        OrderCancelRejectBuilder orderCancelRejectBuilder = new OrderCancelRejectBuilder();
        String clientOrderId = BuilderUtil.fill("2", OrderCancelRejectEncoder.clientOrderIdLength());

        return orderCancelRejectBuilder.compID(1)
                .partitionId((short)1)
                .sequenceNumber(1)
                .clientOrderId(clientOrderId.getBytes())
                .orderId(1)
                .transactTime(10000)
                .rejectCode(RejectCode.InvalidCompIDOrPassword)
                .orderBook(OrderBookEnum.Regular)
                .build();
    }

}