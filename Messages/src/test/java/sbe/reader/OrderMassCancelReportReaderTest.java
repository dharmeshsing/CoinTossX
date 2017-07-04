package sbe.reader;

import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.OrderMassCancelReportBuilder;
import sbe.msg.OrderBookEnum;
import sbe.msg.OrderCancelRejectEncoder;
import sbe.msg.OrderMassCancelReportStatusEnum;
import sbe.msg.RejectCode;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 1/11/15.
 */
public class OrderMassCancelReportReaderTest {
    @Test
    public void testRead() throws Exception {
        OrderMassCancelReportReader orderMassCancelReport = new OrderMassCancelReportReader();
        DirectBuffer buffer = build();

        StringBuilder sb = orderMassCancelReport.read(buffer);
        assertEquals("PartitionId=1SequenceNumber=1ClientOrderId=2                   Status=AcceptedTransactTime=10000RejectCode=InvalidCompIDOrPasswordOrderBook=Regular",sb.toString());

    }

    private DirectBuffer build(){
        OrderMassCancelReportBuilder orderMassCancelReport = new OrderMassCancelReportBuilder();
        String clientOrderId = BuilderUtil.fill("2", OrderCancelRejectEncoder.clientOrderIdLength());

        return orderMassCancelReport.compID(1)
                .partitionId((short)1)
                .sequenceNumber(1)
                .clientOrderId(clientOrderId.getBytes())
                .status(OrderMassCancelReportStatusEnum.Accepted)
                .transactTime(10000)
                .rejectCode(RejectCode.InvalidCompIDOrPassword)
                .orderBook(OrderBookEnum.Regular)
                .build();
    }

}