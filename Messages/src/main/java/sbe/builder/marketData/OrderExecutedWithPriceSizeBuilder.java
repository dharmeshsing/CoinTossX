package sbe.builder.marketData;

import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.OrderExecutedWithPriceSizeEncoder;
import sbe.msg.marketData.PrintableEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class OrderExecutedWithPriceSizeBuilder {
    private int bufferIndex;
    private OrderExecutedWithPriceSizeEncoder orderExecutedWithPriceSize;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private MessageTypeEnum messageType;
    private long nanosecond;
    private long orderId;
    private int executedQuantity;
    private int displayQuantity;
    private int tradeId;
    private long clientOrderId;
    private PrintableEnum printable;
    private long price;
    private int instrumentId;

    public static int BUFFER_SIZE = 54;

    public OrderExecutedWithPriceSizeBuilder(){
        orderExecutedWithPriceSize = new OrderExecutedWithPriceSizeEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public OrderExecutedWithPriceSizeBuilder messageType(MessageTypeEnum value){
        this.messageType = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder nanosecond(long value){
        this.nanosecond = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder orderId(long value){
        this.orderId = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder clientOrderId(long value){
        this.clientOrderId = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder executedQuantity(int value){
        this.executedQuantity = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder tradeId(int value){
        this.tradeId = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder displayQuantity(int value){
        this.displayQuantity = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder printable(PrintableEnum value){
        this.printable = value;
        return this;
    }
    public OrderExecutedWithPriceSizeBuilder price(int value){
        this.price = value;
        return this;
    }

    public OrderExecutedWithPriceSizeBuilder instrumentId(int value){
        this.instrumentId = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderExecutedWithPriceSize.sbeBlockLength())
                .templateId(orderExecutedWithPriceSize.sbeTemplateId())
                .schemaId(orderExecutedWithPriceSize.sbeSchemaId())
                .schemaId(orderExecutedWithPriceSize.sbeSchemaId())
                .version(orderExecutedWithPriceSize.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        orderExecutedWithPriceSize.wrap(encodeBuffer, bufferIndex);

        orderExecutedWithPriceSize.messageType(messageType)
                .nanosecond(nanosecond)
                .orderId(orderId)
                .clientOrderId(clientOrderId)
                .executedQuantity(executedQuantity)
                .displayQuantity(displayQuantity)
                .tradeId(tradeId)
                .printable(printable)
                .instrumentId(instrumentId);

        orderExecutedWithPriceSize.price().mantissa(price);


        return encodeBuffer;
    }

}
