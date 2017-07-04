package data;

import crossing.CrossingProcessor;
import sbe.builder.BusinessRejectBuilder;
import sbe.msg.BusinessRejectEnum;
import sbe.msg.ExecutionReportDecoder;
import sbe.msg.RejectCode;
import uk.co.real_logic.agrona.DirectBuffer;

/**
 * Created by dharmeshsing on 15/08/15.
 */
public enum BusinessRejectReportData {
    INSTANCE;

    private int compID;
    private int orderId;
    private byte[] clientOrderId = new byte[ExecutionReportDecoder.clientOrderIdLength()];
    private RejectCode rejectCode = RejectCode.NULL_VAL;

    private BusinessRejectBuilder businessReject = new BusinessRejectBuilder();

    public void reset(){
        compID = 0;
        rejectCode = RejectCode.NULL_VAL;
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public byte[] getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(byte[] clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public RejectCode getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(RejectCode rejectCode) {
        this.rejectCode = rejectCode;
    }

    public DirectBuffer buildBusinessRejectReport(BusinessRejectEnum rejectEnum){

        return businessReject.partitionId((short)0)
                .compID(compID)
                .sequenceNumber(CrossingProcessor.sequenceNumber.incrementAndGet())
                .businessRejectEnum(rejectEnum)
                .clientOrderId(clientOrderId)
                .orderId(0)
                .transactTime(System.nanoTime())
                .build();
    }
}
