package sbe.builder;

import com.carrotsearch.hppc.LongIntHashMap;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class ExecutionReportBuilder {
    private int bufferIndex;
    private ExecutionReportEncoder executionReport;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private short partitionId;
    private int sequenceNumber;
    private UnsafeBuffer executionId;
    private UnsafeBuffer clientOrderId;
    private int orderId;
    private ExecutionTypeEnum executionType;
    private OrderStatusEnum orderStatus;
    private RejectCode rejectCode;
    private int leavesQuantity;
    private ContainerEnum container;
    private int securityId;
    private SideEnum side;
    private UnsafeBuffer traderMnemonic;
    private UnsafeBuffer account;
    private IsMarketOpsRequestEnum isMarketOpsRequest;
    private long transactTime;
    private OrderBookEnum orderBook;
    private LongIntHashMap fillGroups;

    public static int BUFFER_SIZE = 4096;

    public ExecutionReportBuilder(){
        executionReport = new ExecutionReportEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        executionId = new UnsafeBuffer(ByteBuffer.allocateDirect(ExecutionReportEncoder.executionIDLength()));
        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(ExecutionReportEncoder.clientOrderIdLength()));
        traderMnemonic = new UnsafeBuffer(ByteBuffer.allocateDirect(ExecutionReportEncoder.traderMnemonicLength()));
        account = new UnsafeBuffer(ByteBuffer.allocateDirect(ExecutionReportEncoder.accountLength()));
        fillGroups = new LongIntHashMap();
    }

    public void reset(){
        fillGroups.clear();
    }

    public ExecutionReportBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public ExecutionReportBuilder partitionId(short value){
        this.partitionId = value;
        return this;
    }

    public ExecutionReportBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }

    public ExecutionReportBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }

    public ExecutionReportBuilder orderId(int value){
        this.orderId = value;
        return this;
    }

    public ExecutionReportBuilder executionType(ExecutionTypeEnum value){
        this.executionType = value;
        return this;
    }

    public ExecutionReportBuilder orderStatus(OrderStatusEnum value){
        this.orderStatus = value;
        return this;
    }

    public ExecutionReportBuilder rejectCode(RejectCode value){
        this.rejectCode = value;
        return this;
    }

    public ExecutionReportBuilder addFillGroup(long price, int quantity){
        this.fillGroups.put(price,quantity);
        return this;
    }

    public ExecutionReportBuilder addAllFillGroup(LongIntHashMap fillGroups){
        this.fillGroups.putAll(fillGroups);
        return this;
    }

    public ExecutionReportBuilder leavesQuantity(int value){
        this.leavesQuantity = value;
        return this;
    }

    public ExecutionReportBuilder container(ContainerEnum value){
        this.container = value;
        return this;
    }

    public ExecutionReportBuilder securityId(int value){
        this.securityId = value;
        return this;
    }

    public ExecutionReportBuilder side(SideEnum value){
        this.side = value;
        return this;
    }

    public ExecutionReportBuilder traderMnemonic(byte[] value){
        this.traderMnemonic.wrap(value);
        return this;
    }

    public ExecutionReportBuilder traderMnemonic(String value){
        String newValue = BuilderUtil.fill(value,ExecutionReportEncoder.traderMnemonicLength());
        this.traderMnemonic.wrap(newValue.getBytes());
        return this;
    }

    public ExecutionReportBuilder account(byte[] value){
        this.account.wrap(value);
        return this;
    }

    public ExecutionReportBuilder executionId(byte[] value){
        this.executionId.wrap(value);
        return this;
    }

    public ExecutionReportBuilder isMarketOpsRequest(IsMarketOpsRequestEnum value){
        this.isMarketOpsRequest = value;
        return this;
    }

    public ExecutionReportBuilder orderBook(OrderBookEnum value){
        this.orderBook = value;
        return this;
    }

    public ExecutionReportBuilder transactTime(long value){
        this.transactTime = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(executionReport.sbeBlockLength())
                .templateId(executionReport.sbeTemplateId())
                .schemaId(executionReport.sbeSchemaId())
                .version(executionReport.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        executionReport.wrap(encodeBuffer, bufferIndex)
                .partitionId(partitionId)
                .sequenceNumber(sequenceNumber)
                .putExecutionID(executionId.byteArray(), 0)
                .putClientOrderId(clientOrderId.byteArray(), 0)
                .orderId(orderId)
                .executionType(executionType)
                .orderStatus(orderStatus)
                .rejectCode(rejectCode);


        int size = fillGroups.size();
        if(size > 0) {
            long[] prices = fillGroups.keys;
            int[] quantities = fillGroups.values;
            ExecutionReportEncoder.FillsGroupEncoder fillsGroup = executionReport.fillsGroupCount(size);
            for (int i = 0; i < size; i++) {
                ExecutionReportEncoder.FillsGroupEncoder group = fillsGroup.next();
                group.fillPrice().mantissa(prices[i]);
                group.fillQty(quantities[i]);

            }
        }

        executionReport.leavesQuantity(leavesQuantity)
                       .container(container)
                       .securityId(securityId)
                       .side(side)
                       .putTraderMnemonic(traderMnemonic.byteArray(),0)
                       .putAccount(account.byteArray(),0)
                       .isMarketOpsRequest(isMarketOpsRequest)
                       .transactTime(transactTime)
                       .orderBook(orderBook);

        return encodeBuffer;
    }
}
