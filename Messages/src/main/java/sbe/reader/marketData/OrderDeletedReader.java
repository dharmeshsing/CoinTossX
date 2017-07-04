package sbe.reader.marketData;

import sbe.msg.marketData.OrderDeletedDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class OrderDeletedReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderDeletedDecoder orderDeleted;
    private sbe.msg.marketData.MessageHeaderDecoder messageHeader;

    public OrderDeletedReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        orderDeleted = new OrderDeletedDecoder();
        messageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderDeleted.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + orderDeleted.messageType());
        sb.append("Nanosecond=" + orderDeleted.nanosecond());
        sb.append("OrderId=" + orderDeleted.orderId());

        return sb;
    }
}
