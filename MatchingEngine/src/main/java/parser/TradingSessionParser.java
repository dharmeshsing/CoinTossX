package parser;

import sbe.msg.marketData.TradingSessionDecoder;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 14/11/15.
 */
public class TradingSessionParser {
    private TradingSessionDecoder tradingSession = new TradingSessionDecoder();
    private int securityId;
    private TradingSessionEnum tradingSessionEnum;

    public void decode(DirectBuffer buffer,int bufferOffset,int actingBlockLength,int actingVersion) throws UnsupportedEncodingException {
        tradingSession.wrap(buffer, bufferOffset, actingBlockLength, actingVersion);
        tradingSessionEnum = tradingSession.tradingSession();
        securityId = tradingSession.securityId();
    }


    public TradingSessionEnum getTradingSessionEnum() {
        return tradingSessionEnum;
    }

    public int getSecurityId() {
        return securityId;
    }
}
