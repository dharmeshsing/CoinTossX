package gateway.incoming;

public interface TradingGateway {
    void initialize();
    void start();
    boolean stop();
    boolean status();
}
