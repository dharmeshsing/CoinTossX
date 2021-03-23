package sbe.reader;

import sbe.builder.LOBBuilder;
import sbe.msg.LOBDecoder;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class LOBReader implements Serializable {
    private int bufferIndex;
    private LOBDecoder lob;
    private MessageHeaderDecoder messageHeader;
    private int compID;
    private int securityId;
    private LOBDecoder.OrdersDecoder ordersDecoder;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocateDirect(LOBBuilder.BUFFER_SIZE));

    public LOBReader(){
        lob = new LOBDecoder();
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

        lob.wrap(temp, bufferIndex, actingBlockLength, actingVersion);
        securityId = lob.securityId();
        ordersDecoder = new LOBDecoder.OrdersDecoder();
        ordersDecoder.wrap(lob,buffer);
    }

    public boolean hasNext(){
        return ordersDecoder.hasNext();
    }

    public void next(LOBBuilder.Order order){
        LOBDecoder.OrdersDecoder od = ordersDecoder.next();
        if(od != null) {
            order.setOrderId(od.orderId());
            order.setOrderQuantity(od.orderQuantity());
            order.setPrice(od.price().mantissa());
            order.setSide(od.side());
        }
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
}
