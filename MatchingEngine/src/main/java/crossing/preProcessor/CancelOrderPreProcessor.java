package crossing.preProcessor;

import bplusTree.BPlusTree;
import crossing.MatchingContext;
import crossing.MatchingUtil;
import data.ExecutionReportData;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;
import sbe.msg.ExecutionTypeEnum;
import sbe.msg.OrderCancelRequestEncoder;
import sbe.msg.OrderStatusEnum;

import java.util.Iterator;

/**
 * Created by dharmeshsing on 22/08/15.
 */
public class CancelOrderPreProcessor implements MatchingPreProcessor {

    @Override
    public void preProcess(MatchingContext context) {
        //context.getTemplateId() == OrderCancelRequestEncoder.TEMPLATE_ID
        if(context.getOrderType() == null) {
            MATCHING_ACTION action = process(context.getOrderBook(), context.getOrderEntry());
            context.setAction(action);
        }
    }

    private MATCHING_ACTION process(OrderBook orderBook,OrderEntry orderEntry) {
        populateExecutionData(orderEntry);

        System.out.println(orderEntry.getOrigClientOrderId() + "Hello");

        boolean isParkedOrder = MatchingUtil.isParkedOrder(orderEntry);
        OrderBook.SIDE side = OrderBook.getSide(orderEntry.getSide());
        BPlusTree<Long, OrderList> tree = getTree(side,orderBook,orderEntry,isParkedOrder);

        long price = orderEntry.getPrice();
        OrderList orderList = tree.get(price);
        if(orderList != null) {
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                if (orderListIterator.next().value.getOrderId() == orderEntry.getOrderId()) {
                    orderListIterator.remove();
                }
            }

            if (!isParkedOrder && orderList.total() == 0) {
                orderBook.removePrice(price, side);
            }
        }
        return MATCHING_ACTION.NO_ACTION;
    }

    private BPlusTree<Long, OrderList> getTree(OrderBook.SIDE side,OrderBook orderBook,OrderEntry orderEntry,boolean isParkedOrder){
        if(isParkedOrder) {
            if (side == OrderBook.SIDE.BID) {
                return orderBook.getParkedBidTree();
            } else {
                return orderBook.getParkedOfferTree();
            }
        }else{
            if (side == OrderBook.SIDE.BID) {
                return orderBook.getBidTree();
            } else {
                return orderBook.getOfferTree();
            }
        }
    }

    private void populateExecutionData(OrderEntry orderEntry){
        ExecutionReportData executionReportData = ExecutionReportData.INSTANCE;
        executionReportData.setOrderId((int)orderEntry.getOrderId());
        executionReportData.setExecutionType(ExecutionTypeEnum.Cancelled);
        executionReportData.setOrderStatus(OrderStatusEnum.Cancelled);
    }
}
