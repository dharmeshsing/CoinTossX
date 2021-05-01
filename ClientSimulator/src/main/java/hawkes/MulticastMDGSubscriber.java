package hawkes;

import gateway.client.AbstractGatewayListener;
import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;
import sbe.msg.marketData.SessionChangedReasonEnum;
import sbe.msg.marketData.TradingSessionEnum;

import java.time.LocalDateTime;

/**
 * Created by dharmeshsing on 5/03/16.
 */
public class MulticastMDGSubscriber extends AbstractGatewayListener implements Runnable {

    private GatewayClient marketDataGatewaySub;
    private String url;
    private int streamId;
    private Client client;
    private NonBlockingSemaphore semaphore;

    public MulticastMDGSubscriber(String url, int streamId, Client client, NonBlockingSemaphore semaphore){
        this.url = url;
        this.streamId = streamId;
        this.client = client;
        this.semaphore = semaphore;
        init();
    }

    private void init(){
        marketDataGatewaySub = new GatewayClientImpl();
        marketDataGatewaySub.addListener(this);
    }

    public void close(){
        marketDataGatewaySub.disconnectOutput();
    }


    @Override
    public void updateBidAskPrice(long securityId,long bid, long bidQuantity, long offer, long offerQuantity) {
        try {
            if(client.getSecurityId() == securityId) {

                client.setBid(bid);
                client.setBidQuantity(bidQuantity);

                client.setOffer(offer);
                client.setOfferQuantity(offerQuantity);

                semaphore.release();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void symbolStatus(long securityId, SessionChangedReasonEnum sessionChangedReason, TradingSessionEnum newTradingSession, long staticPriceReference, long dynamicPriceReference) {
        if(client.getSecurityId() == securityId) {
            System.out.println("New session is " + newTradingSession.name());

            if (newTradingSession.equals(TradingSessionEnum.VolatilityAuctionCall) ||
                    newTradingSession.equals(TradingSessionEnum.IntraDayAuctionCall)) {
                client.setAuction(true);
                System.out.println("Auction started at " + LocalDateTime.now());
            } else {
                client.setAuction(false);
                System.out.println("Auction ended at " + LocalDateTime.now());
            }
        }
    }

    @Override
    public void run() {
        marketDataGatewaySub.connectOutput(url, streamId);
    }
}
