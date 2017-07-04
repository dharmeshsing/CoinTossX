package sbe.reader;

import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.BusinessRejectBuilder;
import sbe.msg.BusinessRejectEncoder;
import sbe.msg.BusinessRejectEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 22/08/15.
 */
public class BusinessRejectReaderTest {

    @Test
    public void testRead() throws Exception {
        BusinessRejectReader businessRejectReader = new BusinessRejectReader();
        DirectBuffer buffer = build();

        StringBuilder sb = businessRejectReader.read(buffer);
        assertEquals("PartitionId=1SequenceNumber=1RejectCode=UnknownInstrumentClientOrderId=2" +
                     "                   OrderId=1TransactTime=36000000000000",sb.toString());
    }

    private DirectBuffer build(){
        BusinessRejectBuilder businessRejectBuilder = new BusinessRejectBuilder();
        businessRejectBuilder.partitionId((short)1)
                             .sequenceNumber(1)
                             .businessRejectEnum(BusinessRejectEnum.UnknownInstrument);

        String clientOrderId = BuilderUtil.fill("2", BusinessRejectEncoder.clientOrderIdLength());
        businessRejectBuilder.clientOrderId(clientOrderId.getBytes());

        long time = LocalTime.of(10,0).getLong(ChronoField.NANO_OF_DAY);
        businessRejectBuilder.orderId(1).transactTime(time);

        return businessRejectBuilder.build();
    }

}