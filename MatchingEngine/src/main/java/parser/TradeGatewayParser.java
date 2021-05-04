package parser;

import com.carrotsearch.hppc.LongObjectHashMap;
import data.BusinessRejectReportData;
import data.ExecutionReportData;
import data.MarketData;
import leafNode.OrderEntry;
import leafNode.OrderEntryFactory;
import orderBook.OrderBook;
import sbe.msg.*;
import sbe.msg.marketData.TradingSessionDecoder;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class TradeGatewayParser {
    private MessageHeaderDecoder messageHeader = new MessageHeaderDecoder();
    private OrderEntry orderEntry;
    private NewOrderParser newOrderParser;
    private OrderCancelRequestParser orderCancelRequestParser;
    private OrderCancelReplaceRequestParser orderCancelReplaceRequestParser;
    private TradingSessionParser tradingSessionParser;
    private AdminMessageParser adminMessageParser;
    private TradingSessionEnum tradingSessionEnum;
    private AdminTypeEnum adminTypeEnum;
    private int securityId;
    private int bufferIndex;
    private int templateId;
    private int actingBlockLength;
    private int actingVersion;

    public TradeGatewayParser(){
        messageHeader = new MessageHeaderDecoder();
        orderEntry = OrderEntryFactory.getOrderEntry();
        newOrderParser = new NewOrderParser();
        orderCancelRequestParser = new OrderCancelRequestParser();
        orderCancelReplaceRequestParser = new OrderCancelReplaceRequestParser();
        tradingSessionParser = new TradingSessionParser();
        adminMessageParser = new AdminMessageParser();
    }

    private void init(DirectBuffer buffer){
        bufferIndex = 0;
        messageHeader.wrap(buffer, 0);
        templateId = messageHeader.templateId();
        actingBlockLength = messageHeader.blockLength();
        actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        initOrderEntry();
        setReportData();
    }

    private void initOrderEntry(){
        orderEntry.clear();
        orderEntry.setSubmittedTime(System.nanoTime());
    }

    public void parse(DirectBuffer buffer) throws UnsupportedEncodingException{
        init(buffer);

        if(templateId == NewOrderEncoder.TEMPLATE_ID){
            parseNewOrder(buffer);
        }else if(templateId == OrderCancelRequestEncoder.TEMPLATE_ID){
            parseCancelOrderRequest(buffer);
        }else if(templateId == OrderCancelReplaceRequestEncoder.TEMPLATE_ID){
            parseCancelReplaceOrderRequest(buffer);
        }else if(templateId == TradingSessionDecoder.TEMPLATE_ID){
            parseTradingSession(buffer);
        }else if(templateId == AdminDecoder.TEMPLATE_ID){
            parseAdminMessage(buffer);
        }else if(templateId == LOBEncoder.TEMPLATE_ID){
            parseAdminMessage(buffer);
        }
    }

    private void parseNewOrder(DirectBuffer buffer) throws UnsupportedEncodingException {
        newOrderParser.decode(buffer,orderEntry,bufferIndex,actingBlockLength,actingVersion);
        securityId = newOrderParser.getSecurityId();
    }

    private void parseCancelOrderRequest(DirectBuffer buffer) throws UnsupportedEncodingException {
        orderCancelRequestParser.decode(buffer,orderEntry,bufferIndex,actingBlockLength,actingVersion);
        securityId = orderCancelRequestParser.getSecurityId();
    }

    private void parseCancelReplaceOrderRequest(DirectBuffer buffer) throws UnsupportedEncodingException {
        orderCancelReplaceRequestParser.decode(buffer,orderEntry,bufferIndex,actingBlockLength,actingVersion);
        securityId = orderCancelReplaceRequestParser.getSecurityId();
    }

    private void parseTradingSession(DirectBuffer buffer) throws UnsupportedEncodingException {
        tradingSessionParser.decode(buffer,bufferIndex,actingBlockLength,actingVersion);
        tradingSessionEnum = tradingSessionParser.getTradingSessionEnum();
        securityId = tradingSessionParser.getSecurityId();
    }

    private void parseAdminMessage(DirectBuffer buffer) throws UnsupportedEncodingException {
        adminMessageParser.decode(buffer, bufferIndex, actingBlockLength, actingVersion);
        adminTypeEnum = adminMessageParser.getAdminTypeEnum();
        securityId = adminMessageParser.getSecurityId();
    }


    public BusinessRejectEnum isValid(LongObjectHashMap<OrderBook> orderBooks){
        if(orderBooks.get(securityId) == null){
            return BusinessRejectEnum.UnknownInstrument;
        }

        return BusinessRejectEnum.NULL_VAL;
    }

    private void setReportData(){
        ExecutionReportData.INSTANCE.setCompID(messageHeader.compID());
        BusinessRejectReportData.INSTANCE.setCompID(messageHeader.compID());
        MarketData.INSTANCE.setCompID(messageHeader.compID());
    }


    public OrderEntry getOrderEntry(){
        return orderEntry;
    }

    public int getTemplateId() {
        return templateId;
    }

    public int getSecurityId() {
        return securityId;
    }

    public TradingSessionEnum getTradingSessionEnum(){
        return this.tradingSessionEnum;
    }

    public AdminTypeEnum getAdminTypeEnum() {
        return adminTypeEnum;
    }
}
