package crossing;

import com.carrotsearch.hppc.LongObjectHashMap;
import crossing.tradingSessions.TradingSessionFactory;
import crossing.tradingSessions.TradingSessionProcessor;
import data.BusinessRejectReportData;
import data.ExecutionReportData;
import data.HDRData;
import data.MarketData;
import engine.MatchingEngine;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import parser.TradeGatewayParser;
import sbe.msg.AdminDecoder;
import sbe.msg.AdminTypeEnum;
import sbe.msg.BusinessRejectEnum;
import sbe.msg.marketData.SessionChangedReasonEnum;
import sbe.msg.marketData.TradingSessionDecoder;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

public class   CrossingProcessor implements LOBManager {

    public static AtomicInteger sequenceNumber = new AtomicInteger();
    private TradeGatewayParser tradeGatewayParser;
    private LongObjectHashMap<OrderBook> orderBooks;
    private boolean clientMarketDataRequest;

    public CrossingProcessor(LongObjectHashMap<OrderBook> orderBooks){
        this.orderBooks = orderBooks;
        this.tradeGatewayParser = new TradeGatewayParser();
    }


    public OrderBook getOrderBook(int stockid){
        return orderBooks.get(stockid);
    }

    @Override
    public DirectBuffer processOrder(DirectBuffer message) {
        ExecutionReportData.INSTANCE.reset();
        MarketData.INSTANCE.reset();
        BusinessRejectReportData.INSTANCE.reset();
        BusinessRejectEnum rejectEnum = BusinessRejectEnum.NULL_VAL;
        clientMarketDataRequest = false;

        try {
            tradeGatewayParser.parse(message);
        } catch (UnsupportedEncodingException e) {
            //TODO: handle exception
            e.printStackTrace();
            return null;
        }

        int template = tradeGatewayParser.getTemplateId();

        if(template == AdminDecoder.TEMPLATE_ID){
            processAdminMessage(tradeGatewayParser.getAdminTypeEnum(),tradeGatewayParser.getSecurityId());
        } else if(template == TradingSessionDecoder.TEMPLATE_ID){
            changeTradingSession(tradeGatewayParser.getSecurityId(), tradeGatewayParser.getTradingSessionEnum(),SessionChangedReasonEnum.ScheduledTransition);
        } else {
            OrderEntry orderEntry = tradeGatewayParser.getOrderEntry();
            int securityId = tradeGatewayParser.getSecurityId();
            processOrder(template,securityId,orderEntry);

            return ExecutionReportData.INSTANCE.buildExecutionReport(orderEntry, securityId);
        }

        //Return correct trading report here
        return null;
    }

    @Override
    public boolean isClientMarketDataRequest() {
        return clientMarketDataRequest;
    }

    private void changeTradingSession(int securityId, TradingSessionEnum newTradingSession, SessionChangedReasonEnum sessionChangedReason){
        OrderBook orderBook = orderBooks.get(securityId);
        if(sessionChangedReason == null){
            sessionChangedReason = SessionChangedReasonEnum.ScheduledTransition;
        }

        TradingSessionProcessor tradingSessionProcessor = getOrderBookTradingSession(securityId);
        tradingSessionProcessor.endSession(orderBook);

        tradingSessionProcessor = TradingSessionFactory.getTradingSessionProcessor(newTradingSession);

        MatchingContext.INSTANCE.setOrderBookTradingSession(securityId,newTradingSession);
        MarketData.INSTANCE.addSymbolStatus(securityId, sessionChangedReason,newTradingSession);

        tradingSessionProcessor.startSession(orderBook);
    }

    public void processOrder(int template,int securityId,OrderEntry orderEntry){
        OrderBook orderBook = orderBooks.get(securityId);
        TradingSessionProcessor tradingSessionProcessor = getOrderBookTradingSession(securityId);
        if(tradingSessionProcessor.isOrderValid(orderEntry,template)) {
            tradingSessionProcessor.process(orderBook, orderEntry);
        }

        if(orderBook.isCircuitBreakerBreached()){
            changeTradingSession(securityId, TradingSessionEnum.VolatilityAuctionCall, SessionChangedReasonEnum.CircuitBreakerTripped);
        }
    }

    private TradingSessionProcessor getOrderBookTradingSession(long securityId){
        TradingSessionEnum tradingSessionEnum = MatchingContext.INSTANCE.getOrderBookTradingSession(securityId);
        return TradingSessionFactory.getTradingSessionProcessor(tradingSessionEnum);
    }

    private void processAdminMessage(AdminTypeEnum adminTypeEnum, int securityId){
        switch(adminTypeEnum){
            case WarmUpComplete:warmupComplete();break;
            case SimulationComplete:simulationComplete();break;
            case LOB: lobSnapShot(securityId);break;
            case VWAP: calculateVWAP(securityId);break;
            case ShutDown:{
                MarketData.INSTANCE.addShutDownRequest();
                MatchingEngine.setRunning(false);
            }break;
            case BestBidOfferRequest:resendBBO(securityId);break;
            case StartMessage:MarketData.INSTANCE.addStartMessage(securityId);break;
            case EndMessage:MarketData.INSTANCE.addEndMessage(securityId);break;
            default: return;
        }
    }

    private void warmupComplete(){
        HDRData.INSTANCE.reset();
        for(int i=0; i<orderBooks.size(); i++){
            if(orderBooks.get(i) != null) {
                orderBooks.get(i).freeAll();
            }
        }
    }

    private void simulationComplete(){
        HDRData.INSTANCE.storeHDRStats();
    }

    private void lobSnapShot(int securityId){
        clientMarketDataRequest = true;
        MarketData.INSTANCE.setSnapShotRequest(true);
        MarketData.INSTANCE.setOrderBook(orderBooks.get(securityId));
    }

    private void calculateVWAP(int securityId){
        clientMarketDataRequest = true;
        MarketData.INSTANCE.calcVWAP(orderBooks.get(securityId));
    }

    private void resendBBO(int securityId){
        MatchingUtil.publishBestBidOffer(orderBooks.get(securityId));
    }
}
