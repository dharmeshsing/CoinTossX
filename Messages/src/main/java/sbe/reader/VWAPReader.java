package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.VWAPDecoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class VWAPReader implements Serializable {
    private int bufferIndex;
    private VWAPDecoder vwap;
    private MessageHeaderDecoder messageHeader;
    private int compID;
    private int securityId;
    private long bidVWAP;
    private long offerVWAP;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocateDirect(106));

    public VWAPReader(){
        vwap = new VWAPDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public void read(DirectBuffer buffer) throws UnsupportedEncodingException {
        bufferIndex = 0;
        temp.wrap(buffer);
        messageHeader = messageHeader.wrap(temp, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        compID = messageHeader.compID();
        bufferIndex += messageHeader.encodedLength();

        vwap.wrap(temp, bufferIndex, actingBlockLength, actingVersion);
        securityId = vwap.securityId();
        bidVWAP = vwap.bidVWAP().mantissa();
        offerVWAP = vwap.offerVWAP().mantissa();
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public long getBidVWAP() {
        return bidVWAP;
    }

    public void setBidVWAP(long bidVWAP) {
        this.bidVWAP = bidVWAP;
    }

    public long getOfferVWAP() {
        return offerVWAP;
    }

    public void setOfferVWAP(long offerVWAP) {
        this.offerVWAP = offerVWAP;
    }
}
