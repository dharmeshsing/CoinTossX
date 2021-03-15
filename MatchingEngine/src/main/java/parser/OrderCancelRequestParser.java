package parser;

import dao.TraderDAO;
import data.ExecutionReportData;
import leafNode.OrderEntry;
import sbe.msg.NewOrderDecoder;
import sbe.msg.OrderCancelRequestDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 13/08/15.
 */
public class OrderCancelRequestParser {
    private OrderCancelRequestDecoder orderCancelRequest = new OrderCancelRequestDecoder();
    private int securityId;
    private byte[] traderMnemonic = new byte[NewOrderDecoder.traderMnemonicLength()];

    public void decode(DirectBuffer buffer, OrderEntry orderEntry,int bufferOffset,int actingBlockLength,int actingVersion) throws UnsupportedEncodingException {
        orderCancelRequest.wrap(buffer, bufferOffset, actingBlockLength, actingVersion);

        orderEntry.setOrderId(orderCancelRequest.orderId());
        securityId = orderCancelRequest.securityId();
        String traderName = new  String(traderMnemonic, 0, orderCancelRequest.getTraderMnemonic(traderMnemonic, 0), NewOrderDecoder.traderMnemonicCharacterEncoding()).trim();
        orderEntry.setTrader(TraderDAO.getTrader(traderName));
        orderEntry.setSide((byte) orderCancelRequest.side().value());
        orderEntry.setPrice(orderCancelRequest.limitPrice().mantissa());

        populateExecutionData();
    }

    public int getSecurityId(){
        return securityId;
    }

    private void populateExecutionData(){
        ExecutionReportData executionReportData = ExecutionReportData.INSTANCE;
        orderCancelRequest.getClientOrderId(executionReportData.getClientOrderId(),0);
    }
}
