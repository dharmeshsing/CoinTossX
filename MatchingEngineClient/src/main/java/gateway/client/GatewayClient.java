package gateway.client;


import uk.co.real_logic.agrona.DirectBuffer;

/**
 * Created by dharmeshsing on 2015/01/18.
 */
public interface GatewayClient {

    void addListener(GatewayListener subscriber);
    void connectInput(String inputURL,int inputStreamId);
    void connectOutput(String outputURL,int outputStreamId);
    void disconnectInput();
    void disconnectOutput();
    void send(DirectBuffer msg);
}
