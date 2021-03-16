package sbe.reader;

import sbe.msg.ExecutionReportDecoder;
import sbe.msg.MessageHeaderDecoder;
import sbe.msg.PriceDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class ExecutionReportReader {
    private StringBuilder sb;
    private int bufferIndex;
    private ExecutionReportDecoder executionReport;
    private MessageHeaderDecoder messageHeader;
    private byte[] executionId;
    private byte[] clientOrderId;
    private byte[] trader;
    private byte[] account;

    public ExecutionReportReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        executionReport = new ExecutionReportDecoder();
        executionId = new byte[ExecutionReportDecoder.executionIDLength()];
        clientOrderId = new byte[ExecutionReportDecoder.clientOrderIdLength()];
        trader = new byte[ExecutionReportDecoder.traderMnemonicLength()];
        account = new byte[ExecutionReportDecoder.accountLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        executionReport.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("PartitionId=" + executionReport.partitionId());
        sb.append("SequenceNumber=" + executionReport.sequenceNumber());
        sb.append("ExecutionId=" + new String(executionId, 0, executionReport.getExecutionID(executionId, 0), ExecutionReportDecoder.executionIDCharacterEncoding()));
        sb.append("ClientOrderId=" + new String(clientOrderId, 0, executionReport.getClientOrderId(clientOrderId, 0), ExecutionReportDecoder.clientOrderIdCharacterEncoding()));
        sb.append("OrderId=" + executionReport.orderId());
        sb.append("ExecutionType=" + executionReport.executionType());
        sb.append("OrderStatus=" + executionReport.orderStatus());
        sb.append("RejectCode=" + executionReport.rejectCode());

        ExecutionReportDecoder.FillsGroupDecoder fillsGroupDecoder = executionReport.fillsGroup();
        while(fillsGroupDecoder.hasNext()){
            ExecutionReportDecoder.FillsGroupDecoder group = fillsGroupDecoder.next();
            PriceDecoder priceDecoder = group.fillPrice();
            sb.append("ExecutedPrice=" + priceDecoder.mantissa());
            sb.append("ExecutedQuantity=" + group.fillQty());
        }

        sb.append("LeavesQuantity=" + executionReport.leavesQuantity());
        sb.append("Container=" + executionReport.container());
        sb.append("SecurityId=" + executionReport.securityId());
        sb.append("Side=" + executionReport.side());
        sb.append("TraderMnemonic=" + new String(trader, 0, executionReport.getTraderMnemonic(trader, 0), ExecutionReportDecoder.traderMnemonicCharacterEncoding()));

        sb.append("Account=" + new String(account, 0, executionReport.getAccount(account, 0), ExecutionReportDecoder.accountCharacterEncoding()));
        sb.append("IsMarketOpsRequest=" + executionReport.isMarketOpsRequest());
        sb.append("TransactTime=" + executionReport.transactTime());
        sb.append("OrderBook=" + executionReport.orderBook());

        return sb;
    }

//    public void readBuffer(DirectBuffer buffer) throws UnsupportedEncodingException {
//        sb.delete(0, sb.capacity());
//        bufferIndex = 0;
//        messageHeader = messageHeader.wrap(buffer, bufferIndex);
//
//        int actingBlockLength = messageHeader.blockLength();
//        int actingVersion = messageHeader.version();
//        bufferIndex += messageHeader.encodedLength();
//
//        executionReport.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);
//
//        value.put("PartitionId",String.valueOf(executionReport.partitionId()).trim());
//        value.put("SequenceNumber",String.valueOf(executionReport.sequenceNumber()).trim());
//        value.put("ExecutionId",new String(executionId, 0, executionReport.getExecutionID(executionId, 0), ExecutionReportDecoder.executionIDCharacterEncoding()).trim());
//        value.put("ClientOrderId",new String(clientOrderId, 0, executionReport.getClientOrderId(clientOrderId, 0), ExecutionReportDecoder.clientOrderIdCharacterEncoding()).trim());
//        value.put("OrderId",String.valueOf(executionReport.orderId()).trim());
//        value.put("ExecutionType",executionReport.executionType().toString());
//        value.put("OrderStatus",executionReport.orderStatus().toString().trim());
//        value.put("RejectCode",executionReport.rejectCode().toString().trim());
//
//        return value;
//    }
}
