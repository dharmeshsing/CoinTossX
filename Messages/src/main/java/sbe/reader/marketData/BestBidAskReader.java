package sbe.reader.marketData;

import sbe.msg.marketData.BestBidOfferDecoder;
import sbe.msg.marketData.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class BestBidAskReader {
    private int bufferIndex;
    private BestBidOfferDecoder bestBidOffer;
    private MessageHeaderDecoder messageHeader;
    private long bid;
    private long offer;
    private long bidQuantity;
    private long offerQuantity;
    private long instrumentId;

    public BestBidAskReader(){
        bufferIndex = 0;
        bestBidOffer = new BestBidOfferDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    private void reset(){
        bid = 0L;
        offer = 0L;
        instrumentId = 0L;
        bidQuantity = 0;
        offerQuantity = 0;
    }

    public void read(DirectBuffer buffer) throws UnsupportedEncodingException {
        reset();
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        bestBidOffer.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        bestBidOffer.messageType();
        instrumentId = bestBidOffer.instrumentId();
        bid = bestBidOffer.bid().mantissa();
        bidQuantity = bestBidOffer.bidQuantity();

        offer = bestBidOffer.offer().mantissa();
        offerQuantity = bestBidOffer.offerQuantity();
    }


    public long getBid() {
        return bid;
    }

    public long getOffer() {
        return offer;
    }

    public long getInstrumentId() {
        return instrumentId;
    }

    public long getBidQuantity() {
        return bidQuantity;
    }

    public long getOfferQuantity() {
        return offerQuantity;
    }
}
