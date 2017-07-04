package hawkes;

import gateway.client.AbstractGatewayListener;
import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;

/**
 * Created by dharmeshsing on 5/03/16.
 */
public class TradingGatewaySubscriber extends AbstractGatewayListener implements Runnable {

    private GatewayClient tradingGatewaySub;
    private String url;
    private int streamId;

    public TradingGatewaySubscriber(String url,int streamId){
        this.url = url;
        this.streamId = streamId;
        init();
    }

    private void init(){
        tradingGatewaySub = new GatewayClientImpl();
        tradingGatewaySub.addListener(this);
    }

    public void close(){
        tradingGatewaySub.disconnectOutput();
    }

    @Override
    public void run() {
        tradingGatewaySub.connectOutput(url, streamId);
    }

}
