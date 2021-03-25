package data;

import com.carrotsearch.hppc.LongIntHashMap;
import crossing.CrossingProcessor;
import dao.TraderDAO;
import leafNode.OrderEntry;
import org.joda.time.Instant;
import sbe.builder.BuilderUtil;
import sbe.builder.ExecutionReportBuilder;
import sbe.builder.OrderViewBuilder;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;

public enum ExecutionReportData {
    INSTANCE;

    private int compID;
    private byte[] clientOrderId = new byte[ExecutionReportDecoder.clientOrderIdLength()];
    private int orderId;
    private ExecutionTypeEnum executionType;
    private OrderStatusEnum orderStatus;
    private RejectCode rejectCode = RejectCode.NULL_VAL;
    private long executedPrice;
    private LongIntHashMap fillGroups;
    private ContainerEnum container = ContainerEnum.Main;

    private ExecutionReportBuilder reportBuilder;
    private OrderViewBuilder orderViewBuilder;

    ExecutionReportData(){
        this.fillGroups = new LongIntHashMap();
        this.reportBuilder = new ExecutionReportBuilder();
        this.orderViewBuilder = new OrderViewBuilder();
    }

    public void reset(){
        compID = 0;
        executedPrice = 0;
        fillGroups.clear();
        rejectCode = RejectCode.NULL_VAL;
        reportBuilder.reset();
        orderViewBuilder.reset();
    }

    public void addFillGroup(long price, int quantity){
        fillGroups.put(price, quantity);
    }

    public LongIntHashMap getFillGroup(){
        return fillGroups;
    }

    public byte[] getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(byte[] clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ExecutionTypeEnum getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ExecutionTypeEnum executionType) {
        this.executionType = executionType;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public RejectCode getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(RejectCode rejectCode) {
        this.rejectCode = rejectCode;
    }

    public long getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(long executedPrice) {
        this.executedPrice = executedPrice;
    }

    public ContainerEnum getContainer() {
        return container;
    }

    public void setContainer(ContainerEnum container) {
        this.container = container;
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public DirectBuffer buildExecutionReport(OrderEntry aggOrder,int securityId){
        String execId = BuilderUtil.fill("Exec" + Instant.now().getMillis(), ExecutionReportEncoder.executionIDLength());
        String acc= BuilderUtil.fill("test123", ExecutionReportEncoder.accountLength());

        return reportBuilder.compID(getCompID())
                .partitionId((short)0)
                .sequenceNumber(CrossingProcessor.sequenceNumber.incrementAndGet())
                .executionId(execId.getBytes())
                .clientOrderId(getClientOrderId())
                .orderId(getOrderId())
                .executionType(getExecutionType())
                .orderStatus(OrderStatusEnum.get(aggOrder.getOrderStatus()))
                .rejectCode(getRejectCode())
                .addAllFillGroup(getFillGroup())
                .leavesQuantity(aggOrder.getQuantity())
                .container(getContainer())
                .securityId(securityId)
                .side(SideEnum.get(aggOrder.getSide()))
                .traderMnemonic(TraderDAO.getTraderById(aggOrder.getTrader()))
                .account(acc.getBytes())
                .isMarketOpsRequest(IsMarketOpsRequestEnum.No)
                .transactTime(Instant.now().getMillis())
                .orderBook(OrderBookEnum.Regular)
                .build();
    }

    public void buildOrderView(OrderEntry aggOrder, long securityId){
        orderViewBuilder.compID(getCompID())
                        .orderId((int) aggOrder.getOrderId())
                        .clientOrderId(String.valueOf(aggOrder.getClientOrderId()).getBytes())
                        .orderQuantity(aggOrder.getQuantity())
                        .price(aggOrder.getPrice())
                        .side(aggOrder.getSide() == 1 ? SideEnum.Buy : SideEnum.Sell)
                        .submittedTime(java.time.Instant.now().toEpochMilli())
                        .securityId((int)securityId);
    }

    public DirectBuffer getOrderView(){
        return orderViewBuilder.build();
    }
}
