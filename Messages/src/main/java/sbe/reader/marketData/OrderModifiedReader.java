package sbe.reader.marketData;

import sbe.msg.marketData.OrderModifiedDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class OrderModifiedReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderModifiedDecoder orderModified;
    private sbe.msg.marketData.MessageHeaderDecoder messageHeader;

    public OrderModifiedReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        orderModified = new OrderModifiedDecoder();
        messageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderModified.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + orderModified.messageType());
        sb.append("Nanosecond=" + orderModified.nanosecond());
        sb.append("OrderId=" + orderModified.orderId());
        sb.append("NewQuantity=" + orderModified.newQuantity());
        sb.append("NewPrice=" + orderModified.newPrice().mantissa());
        sb.append("Flags=" + orderModified.flags());


        return sb;
    }
}
