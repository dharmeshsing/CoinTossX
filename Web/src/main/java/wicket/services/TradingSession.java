package wicket.services;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.ObjectArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbe.builder.TradingSessionBuilder;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import vo.StockVO;

import javax.inject.Singleton;

/**
 * Created by dharmeshsing on 19/08/16.
 */
@Component
@Singleton
public class TradingSession {

    @Autowired
    private Services services;
    private IntObjectMap<TradingSessionEnum> stockTradingSession;
    private TradingSessionBuilder tradingSessionBuilder;

    public TradingSession(){
        stockTradingSession = new IntObjectHashMap<>();
        tradingSessionBuilder = new TradingSessionBuilder();
    }

    public TradingSessionEnum getTradingSession(int securityId) {
        TradingSessionEnum tradingSessionEnum =  stockTradingSession.get(securityId);
        if(tradingSessionEnum == null){
            tradingSessionEnum = TradingSessionEnum.ContinuousTrading;
            setTradingSessionEnum(securityId,tradingSessionEnum);
        }

        return tradingSessionEnum;
    }

    public void setTradingSessionEnum(int securityId, TradingSessionEnum tradingSessionEnum) {
        stockTradingSession.put(securityId,tradingSessionEnum);
    }

    public void changeTradingSession(TradingSessionEnum tradingSessionEnum){
        System.out.println("Cron has fired and changing session to " + tradingSessionEnum.name());
        ObjectArrayList<StockVO> stockVOs = services.getStockDataProvider().getStocks();

        for (int i=0, max=stockVOs.size(); i < max; i++) {
            DirectBuffer directBuffer = tradingSessionBuilder.tradingSessionEnum(tradingSessionEnum)
                    .compID(999)
                    .securityId(stockVOs.get(i).getSecurityId())
                    .build();

            services.getTradingGatewayPub().send(directBuffer);
        }
    }

}
