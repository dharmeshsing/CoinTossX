package receiver;

import com.lmax.disruptor.EventHandler;
import dao.OffHeapStorage;
import vo.OrderVO;

/**
 * Created by dharmeshsing on 19/12/16.
 */
public class OrderVOHandler implements EventHandler<OrderVOMessageEvent> {
    private OffHeapStorage offHeapStorage;
    private OrderVO orderVO = new OrderVO();

    public OrderVOHandler(OffHeapStorage offHeapStorage){
        this.offHeapStorage = offHeapStorage;
    }

    @Override
    public void onEvent(OrderVOMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        orderVO.setSecurityId(event.getSecurityId());
        orderVO.setOrderId(event.getOrderId());
        orderVO.setSide(event.getSide().toString());
        orderVO.setSubmittedTime(event.getSubmittedTime());
        orderVO.setVolume(event.getVolume());
        orderVO.setPrice(event.getPrice());

        offHeapStorage.addSubmittedOrder(event.getSecurityId(), orderVO);
    }
}
