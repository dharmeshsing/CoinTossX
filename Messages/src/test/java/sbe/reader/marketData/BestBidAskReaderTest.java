package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.BestBidOfferBuilder;
import sbe.msg.marketData.MessageTypeEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 23/03/16.
 */
public class BestBidAskReaderTest {

    @Test
    public void testRead() throws Exception {
        BestBidAskReader bestBidAskReader = new BestBidAskReader();
        DirectBuffer buffer = build();

        bestBidAskReader.read(buffer);
        assertEquals(200,bestBidAskReader.getBid());
        assertEquals(1000,bestBidAskReader.getBidQuantity());

        assertEquals(100,bestBidAskReader.getOffer());
        assertEquals(2000,bestBidAskReader.getOfferQuantity());
        assertEquals(1,bestBidAskReader.getInstrumentId());

    }


    private DirectBuffer build(){
        BestBidOfferBuilder bestBidAskBuilder = new BestBidOfferBuilder();
        return bestBidAskBuilder.messageType(MessageTypeEnum.BestBidAsk)
                .instrumentId(1)
                .bid(200)
                .bidQuantity(1000)
                .offer(100)
                .offerQuantity(2000)
                .build();

    }
}