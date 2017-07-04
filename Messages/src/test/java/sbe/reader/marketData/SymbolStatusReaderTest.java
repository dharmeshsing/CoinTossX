package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.SymbolStatusBuilder;
import sbe.msg.marketData.HaltReasonEnum;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.SessionChangedReasonEnum;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 26/08/15.
 */
public class SymbolStatusReaderTest {

    @Test
    public void testRead() throws Exception {
        SymbolStatusReader symbolStatusReader = new SymbolStatusReader();
        DirectBuffer buffer = build();

        symbolStatusReader.read(buffer);
        assertEquals(TradingSessionEnum.VolatilityAuctionCall,symbolStatusReader.getTradingSession());
        assertEquals(1,symbolStatusReader.getSecurityId());
        assertEquals(SessionChangedReasonEnum.CircuitBreakerTripped,symbolStatusReader.getSessionChangedReason());
    }

    private DirectBuffer build(){
        SymbolStatusBuilder symbolStatusBuilder = new SymbolStatusBuilder();
        symbolStatusBuilder.messageType(MessageTypeEnum.SymbolStatus)
                           .nanosecond(0)
                           .sessionChangedReason(SessionChangedReasonEnum.CircuitBreakerTripped)
                           .haltReason(HaltReasonEnum.ReasonNotAvailable)
                            .instrumentId(1)
                           .tradingSession(TradingSessionEnum.VolatilityAuctionCall);

        return symbolStatusBuilder.build();

    }

}