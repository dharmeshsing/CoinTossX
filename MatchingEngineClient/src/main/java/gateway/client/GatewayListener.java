package gateway.client;

import sbe.msg.AdminTypeEnum;
import sbe.msg.marketData.SessionChangedReasonEnum;
import sbe.msg.marketData.TradingSessionEnum;
import sbe.reader.VWAPReader;

public interface GatewayListener {
    void updateBidAskPrice(long securityId,long bid,long bidQuantity,long offer,long offerQuantity);
    void processAdminMessage(int clientId,long securityId,AdminTypeEnum adminTypeEnum);
    void symbolStatus(long securityId, SessionChangedReasonEnum sessionChangedReason, TradingSessionEnum newTradingSession);
    void readVWAP(VWAPReader vwapReader);
}
