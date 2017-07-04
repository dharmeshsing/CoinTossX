package sbe.reader;

import org.junit.Test;
import sbe.builder.LOBBuilder;
import sbe.msg.SideEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LOBReaderTest {

    @Test
    public void testRead() throws Exception {
        LOBReader lobReader = new LOBReader();
        DirectBuffer buffer = build();

        lobReader.read(buffer);

        assertEquals(0,lobReader.getCompID());
        assertEquals(1,lobReader.getSecurityId());

        LOBBuilder.Order order = new LOBBuilder.Order();

        while(lobReader.hasNext()) {
            lobReader.next(order);
        }

    }

    private DirectBuffer build(){
        LOBBuilder lobBuilder = new LOBBuilder();
        lobBuilder.compID(0)
                .securityId(1);

        for(int i=0; i< 100; i++) {
            lobBuilder.addOrder(i+1, 1, SideEnum.Buy, 100);
        }

        return lobBuilder.build();
    }
}