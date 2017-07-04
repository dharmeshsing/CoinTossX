package crossing.preProcessor;

import bplusTree.BPlusTree;
import common.TimeInForce;
import crossing.MatchingContext;
import crossing.MatchingUtil;
import data.ExecutionReportData;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;
import sbe.msg.ExecutionTypeEnum;
import sbe.msg.OrderCancelReplaceRequestEncoder;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dharmeshsing on 22/08/15.
 */
public class ReplaceOrderPreProcessor implements MatchingPreProcessor  {


    @Override
    public void preProcess(MatchingContext context) {
        if(context.getTemplateId() == OrderCancelReplaceRequestEncoder.TEMPLATE_ID) {
            MATCHING_ACTION action = preProcess(context.getOrderBook(), context.getOrderEntry());
            context.setAction(action);
        }
    }

    public MATCHING_ACTION preProcess(OrderBook orderBook,OrderEntry replacementOrder) {
        populateExecutionData(replacementOrder);

        OrderBook.SIDE side = OrderBook.getSide(replacementOrder.getSide());
        BPlusTree<Long, OrderList> tree = getTree(side, orderBook, replacementOrder);

        long price = replacementOrder.getPrice();
        OrderList orderList = tree.get(price);
        if(orderList == null){
            return preProcessByOrderId(orderBook,side,replacementOrder);
        }else{
            return preProcessByPrice(orderList,orderBook,side,replacementOrder,price);
        }
    }

    private MATCHING_ACTION preProcessByPrice(OrderList orderList,OrderBook orderBook,OrderBook.SIDE side,OrderEntry replacementOrder,long price){
        Iterator<OrderListCursor> orderListIterator = orderList.iterator();
        while (orderListIterator.hasNext()) {
            OrderEntry currentOrder = orderListIterator.next().value;
            if (currentOrder.getOrderId() == replacementOrder.getOrderId()) {
                boolean isParkedOrder  = MatchingUtil.isParkedOrder(currentOrder);
                if(shouldRegressOrderBook(currentOrder,replacementOrder,isParkedOrder)){
                    orderListIterator.remove();
                    if (orderList.total() == 0) {
                        if(isParkedOrder) {
                            orderBook.removeParkedPrice(price,side);
                        }else{
                            orderBook.removePrice(price, side);
                        }
                    }

                    if(isParkedOrder){
                        return MATCHING_ACTION.PARK_ORDER;
                    }else {
                        return MATCHING_ACTION.AGGRESS_ORDER;
                    }
                } else {
                    amendCurrentOrder(currentOrder, replacementOrder, isParkedOrder);
                    break;
                }
            }
        }
        return MATCHING_ACTION.NO_ACTION;
    }

    //TODO: This might take a long time to find the order Id
    private MATCHING_ACTION preProcessByOrderId(OrderBook orderBook,OrderBook.SIDE side,OrderEntry replacementOrder){
        BPlusTree.BPlusTreeIterator iterator = orderBook.getPriceIterator(side);

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                OrderEntry currentOrder = orderListIterator.next().value;
                long price = currentOrder.getPrice();
                if (currentOrder.getOrderId() == replacementOrder.getOrderId()) {
                    boolean isParkedOrder  = MatchingUtil.isParkedOrder(currentOrder);
                    if(shouldRegressOrderBook(currentOrder,replacementOrder,isParkedOrder)){
                        orderListIterator.remove();
                        if (orderList.total() == 0) {
                            if(isParkedOrder) {
                                orderBook.removeParkedPrice(price,side);
                            }else{
                                orderBook.removePrice(price, side);
                            }
                        }

                        if(isParkedOrder){
                            return MATCHING_ACTION.PARK_ORDER;
                        }else {
                            return MATCHING_ACTION.AGGRESS_ORDER;
                        }
                    } else {
                        amendCurrentOrder(currentOrder, replacementOrder, isParkedOrder);
                        break;
                    }
                }
            }
        }

        return MATCHING_ACTION.NO_ACTION;
    }

    private void amendCurrentOrder(OrderEntry currentOrder,OrderEntry replacementOrder,boolean isParkedOrder){
        currentOrder.setQuantity(replacementOrder.getQuantity());
        if(currentOrder.getTimeInForce() == TimeInForce.GTD.getValue() ||
                currentOrder.getTimeInForce() == TimeInForce.GTT.getValue()){
            currentOrder.setExpireTime(replacementOrder.getExpireTime());
        }

        if(isParkedOrder){
            currentOrder.setPrice(replacementOrder.getPrice());
        }else{
            currentOrder.setMinExecutionSize(replacementOrder.getMinExecutionSize());
        }
    }

    private boolean shouldRegressOrderBook(OrderEntry currentOrder,OrderEntry replacementOrder,boolean isParkedOrder){
        if(!isParkedOrder && replacementOrder.getPrice() != currentOrder.getPrice()){
            return true;
        }

        if(isParkedOrder && replacementOrder.getStopPrice() != currentOrder.getStopPrice()){
            return true;
        }

        if(currentOrder.getQuantity() < replacementOrder.getQuantity()){
            return true;
        }

        return false;
    }

    private BPlusTree<Long, OrderList> getTree(OrderBook.SIDE side,OrderBook orderBook,OrderEntry orderEntry){
        if(MatchingUtil.isParkedOrder(orderEntry)) {
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
        executionReportData.setOrderId((int) orderEntry.getOrderId());
        executionReportData.setExecutionType(ExecutionTypeEnum.Amended);
    }
}
