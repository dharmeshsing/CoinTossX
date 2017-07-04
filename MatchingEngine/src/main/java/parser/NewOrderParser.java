package parser;

import dao.TraderDAO;
import data.ExecutionReportData;
import leafNode.OrderEntry;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import sbe.msg.NewOrderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 13/08/15.
 */
public class NewOrderParser {
    private NewOrderDecoder newOrder = new NewOrderDecoder();
    private DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss");
    private int securityId;
    private byte[] traderMnemonic = new byte[NewOrderDecoder.traderMnemonicLength()];
    private byte[] expireTime = new byte[NewOrderDecoder.expireTimeLength()];

    public void decode(DirectBuffer buffer, OrderEntry orderEntry,int bufferOffset,int actingBlockLength,int actingVersion) throws UnsupportedEncodingException {
        newOrder.wrap(buffer, bufferOffset, actingBlockLength, actingVersion);

        securityId = newOrder.securityId();
        String traderName = new  String(traderMnemonic, 0, newOrder.getTraderMnemonic(traderMnemonic, 0), NewOrderDecoder.traderMnemonicCharacterEncoding()).trim();
        orderEntry.setTrader(TraderDAO.getTrader(traderName));

        orderEntry.setType((byte) newOrder.orderType().value());
        orderEntry.setTimeInForce((byte) newOrder.timeInForce().value());

        String expireTimeText = new  String(expireTime, 0, newOrder.getExpireTime(expireTime, 0), NewOrderDecoder.expireTimeCharacterEncoding());
        long eTime = dateTimeFormatter.parseMillis(expireTimeText);
        orderEntry.setExpireTime(eTime);

        orderEntry.setSide((byte) newOrder.side().value());
        orderEntry.setQuantity(newOrder.orderQuantity());
        orderEntry.setDisplayQuantity(newOrder.displayQuantity());
        orderEntry.setMinExecutionSize(newOrder.minQuantity());
        orderEntry.setPrice(newOrder.limitPrice().mantissa());
        orderEntry.setStopPrice(newOrder.stopPrice().mantissa());

        populateExecutionData();

    }

    public int getSecurityId(){
        return securityId;
    }

    private void populateExecutionData(){
        newOrder.getClientOrderId(ExecutionReportData.INSTANCE.getClientOrderId(),0);
    }
}
