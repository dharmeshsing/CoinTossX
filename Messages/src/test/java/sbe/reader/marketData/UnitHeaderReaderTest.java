package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.UnitHeaderBuilder;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class UnitHeaderReaderTest {

    @Test
    public void testRead() throws Exception {
        UnitHeaderReader unitHeaderReader = new UnitHeaderReader();
        DirectBuffer buffer = build();

        StringBuilder sb = unitHeaderReader.read(buffer);
        assertEquals("MessageCount=1MarketDataGroup=2SequenceNumber=1",sb.toString());

    }

    private DirectBuffer build(){
        UnitHeaderBuilder unitHeaderBuilder = new UnitHeaderBuilder();
        return unitHeaderBuilder.sequenceNumber(1)
                .marketDataGroup((byte)2)
                .messageCount(1)
                .build();
    }
}