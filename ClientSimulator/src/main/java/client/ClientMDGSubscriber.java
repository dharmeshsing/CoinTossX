package client;

import gateway.client.AbstractGatewayListener;
import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;
import sbe.builder.LOBBuilder;
import sbe.msg.AdminTypeEnum;
import sbe.msg.SideEnum;
import sbe.reader.LOBReader;
import sbe.reader.VWAPReader;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ClientMDGSubscriber extends AbstractGatewayListener implements Runnable {

    private GatewayClient marketDataGatewaySub;
    private String url;
    private int streamId;
    private NonBlockingSemaphore semaphore;
    private SideEnum sideEnum;
    private long vwap;
    private int securityId;
    private boolean stop;
    ArrayList<String> lob = new ArrayList<String>();
    LOBBuilder.Order lobFlyweight = new LOBBuilder.Order();

    public ClientMDGSubscriber(String url, int streamId, NonBlockingSemaphore semaphore, int securityId) {
        this.url = url;
        this.streamId = streamId;
        this.semaphore = semaphore;
        this.securityId = securityId;
        init();
    }

    private void init() {
        marketDataGatewaySub = new GatewayClientImpl();
        marketDataGatewaySub.addListener(this);
    }

    public void close(){
        marketDataGatewaySub.disconnectOutput();
    }

    @Override
    public void run() {
        marketDataGatewaySub.connectOutput(url, streamId);
    }

    @Override
    public void readVWAP(VWAPReader vwapReader) {
        if(vwapReader.getSecurityId() == securityId) {
            if(sideEnum == SideEnum.Buy){
                vwap = vwapReader.getBidVWAP();
            } else {
                vwap = vwapReader.getOfferVWAP();
            }
        }
        semaphore.release();
    }

    @Override
    public void readLOB(LOBReader lobReader) {
        lob.clear();
        if(lobReader.getSecurityId() == securityId) {
            try {
                while(lobReader.hasNext()) {
                    lobReader.next(lobFlyweight);
                    lob.add(lobFlyweight.getClientOrderId().trim() + "," + lobFlyweight.getSide() + "," + lobFlyweight.getOrderQuantity() + "," + lobFlyweight.getPrice());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        semaphore.release();
    }

    @Override
    public void processAdminMessage(int clientId,long securityid,AdminTypeEnum adminTypeEnum) {
        System.out.println(adminTypeEnum + " - " + clientId);
        if(adminTypeEnum.equals(AdminTypeEnum.EndMessage)) {
            System.out.println("Received end message from client " + clientId);
            setStop(true);
        }
    }

    public void setSideEnum(SideEnum sideEnum) {
        this.sideEnum = sideEnum;
    }

    public long getVwap() { return vwap; }

    public ArrayList<String> getLob() { return new ArrayList<String>(lob); }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
