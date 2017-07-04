package sbe.reader;

import org.junit.Test;
import sbe.builder.TransmissionCompleteBuilder;
import sbe.msg.TransmissionCompleteStatus;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 29/10/15.
 */
public class TransmissionCompleteReaderTest {
    @Test
    public void testRead() throws Exception {
        TransmissionCompleteReader transmissionCompleteReader = new TransmissionCompleteReader();
        DirectBuffer buffer = build();

        StringBuilder sb = transmissionCompleteReader.read(buffer);
        assertEquals("Status=AllMessageTransmitted",sb.toString());

    }

    private DirectBuffer build(){
        TransmissionCompleteBuilder transmissionCompleteBuilder = new TransmissionCompleteBuilder();

        return transmissionCompleteBuilder.compID(1)
                .status(TransmissionCompleteStatus.AllMessageTransmitted)
                .build();
    }
}