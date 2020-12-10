import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;
import org.apache.commons.math3.distribution.NormalDistribution;
import sbe.builder.AdminBuilder;
import sbe.builder.BuilderUtil;
import sbe.builder.LogonBuilder;
import sbe.builder.NewOrderBuilder;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;

public class Main{
    public static void main (String[] args){
        // Client
        String url = "udp://localhost:5000";
        int streamId = 10;
        int compId = 1;
        String password = "test111111";
        
        // Connect to the Trading Gateway
        GatewayClientImpl tradingGatewayPub = new GatewayClientImpl(); 
        tradingGatewayPub.connectInput(url,streamId);
        
        // Login to the Trading Gateway
        LogonBuilder logonBuilder = new LogonBuilder();         
        DirectBuffer buffer = logonBuilder.compID(compId)
            .password(password.getBytes())  
            .newPassword(password.getBytes())
            .build();
        
        tradingGatewayPub.send(buffer);

        NewOrderBuilder newOrderBuilder = new NewOrderBuilder();

        DirectBuffer directBuffer = newOrderBuilder.compID(compId)
            .clientOrderId("1234".getBytes())
            .account("account123".getBytes())
            .capacity(CapacityEnum.Agency)
            .cancelOnDisconnect(CancelOnDisconnectEnum.DoNotCancel)
            .orderBook(OrderBookEnum.Regular)
            .securityId(1)
            .traderMnemonic("John".getBytes())
            .orderType(OrdTypeEnum.Limit)
            .timeInForce(TimeInForceEnum.Day)
            .expireTime("20150813-23:00:00".getBytes())
            .side(SideEnum.Buy)
            .orderQuantity((int) 100)
            .displayQuantity((int) 100)
            .minQuantity(0)
            .limitPrice((long) 300)
            .stopPrice(0)
            .build();

        // Send new order to Trading Gateway
        tradingGatewayPub.send(directBuffer);
    }
}