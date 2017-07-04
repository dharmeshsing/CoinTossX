package sbe.reader.marketData;

import sbe.msg.marketData.OrderExecutedDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class OrderExecutedReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderExecutedDecoder orderExecuted;
    private sbe.msg.marketData.MessageHeaderDecoder messageHeader;

    public OrderExecutedReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        orderExecuted = new OrderExecutedDecoder();
        messageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderExecuted.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + orderExecuted.messageType());
        sb.append("Nanosecond=" + orderExecuted.nanosecond());
        sb.append("OrderId=" + orderExecuted.orderId());
        sb.append("ExecutedQuantity=" + orderExecuted.executedQuantity());
        sb.append("TradeId=" + orderExecuted.tradeId());

        return sb;
    }
}
