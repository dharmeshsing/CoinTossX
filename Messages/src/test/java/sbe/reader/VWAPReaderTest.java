package sbe.reader;

import org.junit.Test;
import sbe.builder.VWAPBuilder;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class VWAPReaderTest {

    @Test
    public void testRead() throws Exception {
        VWAPReader vwapReader = new VWAPReader();
        DirectBuffer buffer = build();

        vwapReader.read(buffer);

        assertEquals(1000,vwapReader.getBidVWAP());
        assertEquals(2000,vwapReader.getOfferVWAP());

    }

    private DirectBuffer build(){
        VWAPBuilder vwapBuilder = new VWAPBuilder();
        vwapBuilder.compID(0)
                .securityId(1)
                .bidVWAP(1000)
                .offerVWAP(2000);


        return vwapBuilder.build();
    }
}