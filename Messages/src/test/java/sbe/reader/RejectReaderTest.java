package sbe.reader;

import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.RejectBuilder;
import sbe.msg.RejectEncoder;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 28/10/15.
 */
public class RejectReaderTest {
    @Test
    public void testRead() throws Exception {
        RejectReader rejectReader = new RejectReader();
        DirectBuffer buffer = build();

        StringBuilder sb = rejectReader.read(buffer);
        assertEquals("RejectCode=1MessageType=65RejectReason=test                          ClientOrderId=testClient          ",sb.toString());

    }

    private DirectBuffer build(){
        RejectBuilder rejectBuilder = new RejectBuilder();
        String rejectReason = BuilderUtil.fill("test", RejectEncoder.rejectReasonLength());
        String clientOrderId = BuilderUtil.fill("testClient", RejectEncoder.clientOrderIdLength());

        return rejectBuilder.compID(1)
                .rejectCode(1)
                .messageType('A')
                .rejectReason(rejectReason.getBytes())
                .clientOrderId(clientOrderId.getBytes())
                .build();
    }

}