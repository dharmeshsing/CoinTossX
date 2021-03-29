package receiver;

import com.lmax.disruptor.EventHandler;
import dao.OffHeapStorage;
import vo.TradeVO;

import java.time.LocalDateTime;

public class TradeVOHandler implements EventHandler<TradeVOMessageEvent> {
    private OffHeapStorage offHeapStorage;
    private TradeVO tradeVO = new TradeVO();

    public TradeVOHandler(OffHeapStorage offHeapStorage){
        this.offHeapStorage = offHeapStorage;
    }

    @Override
    public void onEvent(TradeVOMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        tradeVO.setTradeId(event.getTradeId());
        tradeVO.setClientOrderId(event.getClientOrderId());
        tradeVO.setPrice(event.getPrice());
        tradeVO.setQuantity(event.getQuantity());
        tradeVO.setCreationTime(LocalDateTime.now());

        offHeapStorage.addTrades(event.getSecurityId(), tradeVO);
    }
}
