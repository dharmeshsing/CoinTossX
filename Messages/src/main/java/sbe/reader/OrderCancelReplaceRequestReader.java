package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.OrderCancelReplaceRequestDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class OrderCancelReplaceRequestReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderCancelReplaceRequestDecoder orderCancelReplaceRequest;
    private MessageHeaderDecoder messageHeader;
    private byte[] clientOrderId;
    private byte[] origClientOrderId;
    private byte[] traderMnemonic;
    private byte[] account;
    private byte[] expireTime;

    public OrderCancelReplaceRequestReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        orderCancelReplaceRequest = new OrderCancelReplaceRequestDecoder();
        clientOrderId = new byte[OrderCancelReplaceRequestDecoder.clientOrderIdLength()];
        origClientOrderId = new byte[OrderCancelReplaceRequestDecoder.origClientOrderIdLength()];
        traderMnemonic = new byte[OrderCancelReplaceRequestDecoder.traderMnemonicLength()];
        account = new byte[OrderCancelReplaceRequestDecoder.accountLength()];
        expireTime = new byte[OrderCancelReplaceRequestDecoder.expireTimeLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderCancelReplaceRequest.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("ClientOrderId=" + new String(clientOrderId, 0, orderCancelReplaceRequest.getClientOrderId(clientOrderId, 0), OrderCancelReplaceRequestDecoder.clientOrderIdCharacterEncoding()));
        sb.append("OrigClientOrderId=" + new String(origClientOrderId, 0, orderCancelReplaceRequest.getOrigClientOrderId(origClientOrderId, 0), OrderCancelReplaceRequestDecoder.origClientOrderIdCharacterEncoding()));
        sb.append("OrderId=" + orderCancelReplaceRequest.orderId());
        sb.append("SecurityId=" + orderCancelReplaceRequest.securityId());
        sb.append("TraderMnemonic=" + new String(traderMnemonic, 0, orderCancelReplaceRequest.getTraderMnemonic(traderMnemonic, 0), OrderCancelReplaceRequestDecoder.traderMnemonicCharacterEncoding()));
        sb.append("Account=" + new String(account, 0, orderCancelReplaceRequest.getAccount(account, 0), OrderCancelReplaceRequestDecoder.accountCharacterEncoding()));
        sb.append("ExpireTime=" + new String(expireTime, 0, orderCancelReplaceRequest.getExpireTime(expireTime, 0), OrderCancelReplaceRequestDecoder.expireTimeCharacterEncoding()));
        sb.append("Side=" + orderCancelReplaceRequest.side());
        sb.append("OrderQuantity=" + orderCancelReplaceRequest.orderQuantity());
        sb.append("DisplayQuantity=" + orderCancelReplaceRequest.displayQuantity());
        sb.append("MinQuantity=" + orderCancelReplaceRequest.minQuantity());
        sb.append("LimitPrice=" + orderCancelReplaceRequest.limitPrice().mantissa());
        sb.append("StopPrice=" + orderCancelReplaceRequest.stopPrice().mantissa());
        sb.append("OrderBook=" + orderCancelReplaceRequest.orderBook());

        return sb;
    }
}
