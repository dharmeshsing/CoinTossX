package gateway.marketdata;

/**
 * Created by dharmeshsing on 2015/01/17.
 */
public interface MarketDataGateway {
    void initialize();
    void start();
    void stop();
    boolean status();
}
