package gateway.marketdata;

public interface MarketDataGateway {
    void initialize();
    void start();
    void stop();
    boolean status();
}
