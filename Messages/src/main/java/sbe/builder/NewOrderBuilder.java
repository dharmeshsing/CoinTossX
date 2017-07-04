package sbe.builder;

import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class NewOrderBuilder {
    private int bufferIndex;
    private NewOrderEncoder newOrder;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private int securityId;
    private int orderQuantity;
    private int displayQuantity;
    private int minQuantity;
    private UnsafeBuffer clientOrderId;
    private UnsafeBuffer traderMnemonic;
    private UnsafeBuffer account;
    private OrdTypeEnum orderType;
    private TimeInForceEnum timeInForce;
    private UnsafeBuffer expireTime;
    private SideEnum side;
    private long limitPrice;
    private long stopPrice;
    private CapacityEnum capacity;
    private CancelOnDisconnectEnum cancelOnDisconnect;
    private OrderBookEnum orderBook;

    public static int BUFFER_SIZE = 114;

    public NewOrderBuilder(){
        newOrder = new NewOrderEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(NewOrderEncoder.clientOrderIdLength()));
        traderMnemonic = new UnsafeBuffer(ByteBuffer.allocateDirect(NewOrderEncoder.traderMnemonicLength()));
        account = new UnsafeBuffer(ByteBuffer.allocateDirect(NewOrderEncoder.accountLength()));
        expireTime = new UnsafeBuffer(ByteBuffer.allocateDirect(NewOrderEncoder.expireTimeLength()));
    }

    public NewOrderBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public NewOrderBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }

    public NewOrderBuilder clientOrderId(String value){
        value = BuilderUtil.fill(value, NewOrderEncoder.clientOrderIdLength());
        this.clientOrderId.wrap(value.getBytes());
        return this;
    }

    public NewOrderBuilder securityId(int value){
        this.securityId = value;
        return this;
    }

    public NewOrderBuilder traderMnemonic(byte[] value){
        this.traderMnemonic.wrap(value);
        return this;
    }

    public NewOrderBuilder traderMnemonic(String value){
        value = BuilderUtil.fill(value, NewOrderEncoder.traderMnemonicLength());
        this.traderMnemonic.wrap(value.getBytes());
        return this;
    }

    public NewOrderBuilder account(byte[] value){
        this.account.wrap(value);
        return this;
    }

    public NewOrderBuilder orderType(OrdTypeEnum value){
        this.orderType = value;
        return this;
    }

    public NewOrderBuilder timeInForce(TimeInForceEnum value){
        this.timeInForce = value;
        return this;
    }
    public NewOrderBuilder expireTime(byte[] value){
        this.expireTime.wrap(value);
        return this;
    }

    public NewOrderBuilder side(SideEnum value){
        this.side = value;
        return this;
    }

    public NewOrderBuilder orderQuantity(int value){
        this.orderQuantity = value;
        return this;
    }

    public NewOrderBuilder displayQuantity(int value){
        this.displayQuantity = value;
        return this;
    }

    public NewOrderBuilder minQuantity(int value){
        this.minQuantity = value;
        return this;
    }

    public NewOrderBuilder limitPrice(long value){
        this.limitPrice = value;
        return this;
    }

    public NewOrderBuilder stopPrice(long value){
        this.stopPrice = value;
        return this;
    }

    public NewOrderBuilder capacity(CapacityEnum value){
        this.capacity = value;
        return this;
    }

    public NewOrderBuilder cancelOnDisconnect(CancelOnDisconnectEnum value){
        this.cancelOnDisconnect = value;
        return this;
    }

    public NewOrderBuilder orderBook(OrderBookEnum value){
        this.orderBook = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(newOrder.sbeBlockLength())
                .templateId(newOrder.sbeTemplateId())
                .schemaId(newOrder.sbeSchemaId())
                .version(newOrder.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        newOrder.wrap(encodeBuffer, bufferIndex)
                .putClientOrderId(clientOrderId.byteArray(),0)
                .securityId(securityId)
                .putTraderMnemonic(traderMnemonic.byteArray(),0)
                .putAccount(account.byteArray(),0)
                .orderType(orderType)
                .timeInForce(timeInForce)
                .putExpireTime(expireTime.byteArray(),0)
                .side(side)
                .orderQuantity(orderQuantity)
                .displayQuantity(displayQuantity)
                .minQuantity(minQuantity);

        newOrder.limitPrice().mantissa(limitPrice);
        newOrder.stopPrice().mantissa(stopPrice);

        newOrder.capacity(capacity)
                .cancelOnDisconnect(cancelOnDisconnect)
                .orderBook(orderBook);


        return encodeBuffer;
    }

}
