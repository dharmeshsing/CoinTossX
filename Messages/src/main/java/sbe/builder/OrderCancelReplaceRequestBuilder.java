package sbe.builder;

import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class OrderCancelReplaceRequestBuilder {
    private int bufferIndex;
    private OrderCancelReplaceRequestEncoder orderCancelReplaceRequest;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private UnsafeBuffer clientOrderId;
    private UnsafeBuffer origClientOrderId;
    private int orderId;
    private int securityId;
    private UnsafeBuffer traderMnemonic;
    private UnsafeBuffer account;
    private OrdTypeEnum orderType;
    private TimeInForceEnum timeInForce;
    private UnsafeBuffer expireTime;
    private SideEnum side;
    private int orderQuantity;
    private int displayQuantity;
    private int minQuantity;
    private long limitPrice;
    private long stopPrice;
    private OrderBookEnum orderBook;

    public static int BUFFER_SIZE = 256;

    public OrderCancelReplaceRequestBuilder(){
        orderCancelReplaceRequest = new OrderCancelReplaceRequestEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelReplaceRequestEncoder.clientOrderIdLength()));
        origClientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelReplaceRequestEncoder.origClientOrderIdLength()));
        traderMnemonic = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelReplaceRequestEncoder.traderMnemonicLength()));
        account = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelReplaceRequestEncoder.accountLength()));
        expireTime = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelReplaceRequestEncoder.expireTimeLength()));
    }

    public OrderCancelReplaceRequestBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }

    public OrderCancelReplaceRequestBuilder origClientOrderId(byte[] value){
        this.origClientOrderId.wrap(value);
        return this;
    }

    public OrderCancelReplaceRequestBuilder securityId(int value){
        this.securityId = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder orderId(int value){
        this.orderId = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder traderMnemonic(byte[] value){
        this.traderMnemonic.wrap(value);
        return this;
    }

    public OrderCancelReplaceRequestBuilder account(byte[] value){
        this.account.wrap(value);
        return this;
    }

    public OrderCancelReplaceRequestBuilder orderType(OrdTypeEnum value){
        this.orderType = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder timeInForce(TimeInForceEnum value){
        this.timeInForce = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder expireTime(byte[] value){
        this.expireTime.wrap(value);
        return this;
    }


    public OrderCancelReplaceRequestBuilder orderQuantity(int value){
        this.orderQuantity = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder displayQuantity(int value){
        this.displayQuantity = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder minQuantity(int value){
        this.minQuantity = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder limitPrice(long value){
        this.limitPrice = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder stopPrice(long value){
        this.stopPrice = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder side(SideEnum value){
        this.side = value;
        return this;
    }

    public OrderCancelReplaceRequestBuilder orderBook(OrderBookEnum value){
        this.orderBook = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderCancelReplaceRequest.sbeBlockLength())
                .templateId(orderCancelReplaceRequest.sbeTemplateId())
                .schemaId(orderCancelReplaceRequest.sbeSchemaId())
                .version(orderCancelReplaceRequest.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        orderCancelReplaceRequest.wrap(encodeBuffer, bufferIndex)
                .putClientOrderId(clientOrderId.byteArray(),0)
                .putOrigClientOrderId(origClientOrderId.byteArray(),0)
                .orderId(orderId)
                .securityId(securityId)
                .putTraderMnemonic(traderMnemonic.byteArray(),0)
                .putAccount(account.byteArray(),0)
                .orderType(orderType)
                .timeInForce(timeInForce)
                .putExpireTime(expireTime.byteArray(), 0)
                .side(side)
                .orderQuantity(orderQuantity)
                .displayQuantity(displayQuantity)
                .minQuantity(minQuantity);

        orderCancelReplaceRequest.limitPrice().mantissa(limitPrice);
        orderCancelReplaceRequest.stopPrice().mantissa(stopPrice);
        orderCancelReplaceRequest.orderBook(orderBook);

        return encodeBuffer;
    }
}
