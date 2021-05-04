package sbe.reader.marketData;

import sbe.msg.marketData.OrderExecutedWithPriceSizeDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class OrderExecutedWithPriceSizeReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderExecutedWithPriceSizeDecoder orderExecutedWithPriceSize;
    private sbe.msg.marketData.MessageHeaderDecoder messageHeader;

    private int executedQuantity;
    private int tradeId;
    private long clientOrderId;
    private long price;
    private int instrumentId;
    private long executedTime;

    public OrderExecutedWithPriceSizeReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        orderExecutedWithPriceSize = new OrderExecutedWithPriceSizeDecoder();
        messageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderExecutedWithPriceSize.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + orderExecutedWithPriceSize.messageType());
        sb.append("Nanosecond=" + orderExecutedWithPriceSize.nanosecond());
        sb.append("OrderId=" + orderExecutedWithPriceSize.orderId());
        sb.append("ClientOrderId=" + orderExecutedWithPriceSize.clientOrderId());
        sb.append("ExecutedQuantity=" + orderExecutedWithPriceSize.executedQuantity());
        sb.append("DisplayQuantity=" + orderExecutedWithPriceSize.displayQuantity());
        sb.append("TradeId=" + orderExecutedWithPriceSize.tradeId());
        sb.append("Printable=" + orderExecutedWithPriceSize.printable());
        sb.append("Price=" + orderExecutedWithPriceSize.price().mantissa());
        sb.append("InstrumentId=" + orderExecutedWithPriceSize.instrumentId());
        sb.append("ExecutedTime=" + orderExecutedWithPriceSize.executedTime());

        return sb;
    }

    public void readBuffer(DirectBuffer buffer) throws UnsupportedEncodingException {
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderExecutedWithPriceSize.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        orderExecutedWithPriceSize.messageType();
        orderExecutedWithPriceSize.nanosecond();
        orderExecutedWithPriceSize.orderId();
        executedQuantity = (int)orderExecutedWithPriceSize.executedQuantity();
        orderExecutedWithPriceSize.displayQuantity();
        tradeId = (int)orderExecutedWithPriceSize.tradeId();
        clientOrderId = orderExecutedWithPriceSize.clientOrderId();
        orderExecutedWithPriceSize.printable();
        price = orderExecutedWithPriceSize.price().mantissa();
        instrumentId = (int)orderExecutedWithPriceSize.instrumentId();
        executedTime = orderExecutedWithPriceSize.executedTime();
    }

    public int getExecutedQuantity() {
        return executedQuantity;
    }

    public int getTradeId() {
        return tradeId;
    }

    public long getClientOrderId() {
        return clientOrderId;
    }

    public long getPrice() {
        return price;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public long getExecutedTime() {
        return executedTime;
    }
}
