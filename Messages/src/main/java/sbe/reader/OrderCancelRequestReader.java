package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.OrderCancelRequestDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class OrderCancelRequestReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderCancelRequestDecoder orderCancelRequest;
    private MessageHeaderDecoder messageHeader;
    private byte[] clientOrderId;
    private byte[] origClientOrderId;
    private byte[] traderMnemonic;

    public OrderCancelRequestReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        orderCancelRequest = new OrderCancelRequestDecoder();
        clientOrderId = new byte[OrderCancelRequestDecoder.clientOrderIdLength()];
        origClientOrderId = new byte[OrderCancelRequestDecoder.origClientOrderIdLength()];
        traderMnemonic = new byte[OrderCancelRequestDecoder.traderMnemonicLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderCancelRequest.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("ClientOrderId=" + new String(clientOrderId, 0, orderCancelRequest.getClientOrderId(clientOrderId, 0), OrderCancelRequestDecoder.clientOrderIdCharacterEncoding()));
        sb.append("OrigClientOrderId=" + new String(origClientOrderId, 0, orderCancelRequest.getOrigClientOrderId(origClientOrderId, 0), OrderCancelRequestDecoder.origClientOrderIdCharacterEncoding()));
        sb.append("OrderId=" + orderCancelRequest.orderId());
        sb.append("SecurityId=" + orderCancelRequest.securityId());
        sb.append("TraderMnemonic=" + new String(traderMnemonic, 0, orderCancelRequest.getTraderMnemonic(traderMnemonic, 0), OrderCancelRequestDecoder.traderMnemonicCharacterEncoding()));
        sb.append("Side=" + orderCancelRequest.side());
        sb.append("OrderBook=" + orderCancelRequest.orderBook());
        sb.append("LimitPrice=" + orderCancelRequest.limitPrice().mantissa());

        return sb;
    }
}
