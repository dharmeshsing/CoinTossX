package sbe.reader;

import org.junit.Test;
import sbe.builder.SystemStatusBuilder;
import sbe.msg.SystemStatusEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 29/10/15.
 */
public class SystemStatusReaderTest {
    @Test
    public void testRead() throws Exception {
        SystemStatusReader systemStatusReader = new SystemStatusReader();
        DirectBuffer buffer = build();

        StringBuilder sb = systemStatusReader.read(buffer);
        assertEquals("Status=RecoveryServiceResumed",sb.toString());

    }

    private DirectBuffer build(){
        SystemStatusBuilder systemStatusBuilder = new SystemStatusBuilder();

        return systemStatusBuilder.compID(1)
                .status(SystemStatusEnum.RecoveryServiceResumed)
                .build();
    }

}