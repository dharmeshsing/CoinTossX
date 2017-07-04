package gateway.incoming;

/**
 * Created by dharmeshsing on 2014/12/15.
 */
public interface TradingGateway {
    void initialize();
    void start();
    boolean stop();
    boolean status();
}
