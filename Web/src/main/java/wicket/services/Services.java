package wicket.services;

import com.googlecode.wickedcharts.highcharts.options.Options;
import dao.OffHeapStorage;
import gateway.client.GatewayClientImpl;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import vo.ClientVO;
import vo.StockVO;
import wicket.dataProvider.ClientDataProvider;
import wicket.dataProvider.StockDataProvider;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by dharmeshsing on 19/04/16.
 */
public interface Services {
    ClientDataProvider getClientDataProvider();
    String getScriptPath();
    StockDataProvider getStockDataProvider();
    String getDataPath();
    void loadPropertiesFromFile(String propertiesFile)  throws IOException;
    Properties getProperties();
    TradingSessionEnum getTradingSession(int securityId);

    ClientVO getAdminClient();
    void sendSnapShotRequest(DirectBuffer buffer);
    DirectBuffer getSnapShotRequest(int securityId);
    void changeTradingSession(TradingSessionEnum tradingSessionEnum);
    void stop();
    GatewayClientImpl getTradingGatewayPub();
    StockVO getStockVO(int securityId);
    void saveLOBDataToFile();

    void startSimulation() throws IOException;
    void stopSimulation();
    void warmpSimulation() throws IOException;
    void stopWarmup();

    boolean isSimulationStarted();
    boolean isWarmupStarted();
    boolean isWarmUpComplete();
    boolean isSimulationIsComplete();
    Options getChart(int securityId,String stockCode) throws Exception;
    void updateClientSimulationStatus();
    void updateLabelClass();
    void updateTradingSession();
    void addClient(ClientVO clientVO) throws Exception;
    OffHeapStorage getOffHeapStorage();

    /** Fixed rate**/
    void setTradingSessionEnum();
    void updateHawkesSimulation();
}
