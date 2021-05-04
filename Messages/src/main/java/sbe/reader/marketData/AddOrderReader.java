package sbe.reader.marketData;

import sbe.msg.marketData.AddOrderDecoder;
import sbe.msg.marketData.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class AddOrderReader {
    private StringBuilder sb;
    private int bufferIndex;
    private AddOrderDecoder addOrder;
    private MessageHeaderDecoder messageHeader;

    public AddOrderReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        addOrder = new AddOrderDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        addOrder.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + addOrder.messageType());
        sb.append("Nanosecond=" + addOrder.nanosecond());
        sb.append("OrderId=" + addOrder.orderId());

        sb.append("Side=" + addOrder.side());
        sb.append("Quantity=" + addOrder.quantity());
        sb.append("InstrumentId=" + addOrder.instrumentId());

        sb.append("Price=" + addOrder.price().mantissa());
        sb.append("Flags=" + addOrder.flags());

        return sb;
    }
}
