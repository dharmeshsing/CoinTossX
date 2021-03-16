package gateway.client;

import sbe.msg.AdminTypeEnum;
import sbe.msg.marketData.SessionChangedReasonEnum;
import sbe.msg.marketData.TradingSessionEnum;
import sbe.reader.VWAPReader;

public class AbstractGatewayListener implements GatewayListener {
    @Override
    public void updateBidAskPrice(long securityId, long bid, long bidQuantity, long offer, long offerQuantity) {

    }

    @Override
    public void processAdminMessage(int clientId,long securityId,AdminTypeEnum adminTypeEnum) {

    }

    @Override
    public void symbolStatus(long securityId, SessionChangedReasonEnum sessionChangedReason, TradingSessionEnum newTradingSession) {

    }

    @Override
    public void readVWAP(VWAPReader vwapReader) {

    }
}
