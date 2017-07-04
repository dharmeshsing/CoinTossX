package gateway.client;

import aeron.AeronPublisher;
import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.NewOrderBuilder;
import sbe.builder.marketData.BestBidOfferBuilder;
import sbe.msg.*;
import sbe.msg.marketData.MessageTypeEnum;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.agrona.DirectBuffer;

import java.time.LocalDateTime;

/**
 * Created by dharmeshsing on 20/04/16.
 */
public class GatewayClientImplTest {

    @Test
    public void testSendReceiveMessage() throws Exception {
        MediaDriver driver = MediaDriver.launchEmbedded();
        AeronPublisher publisher = new AeronPublisher(driver.aeronDirectoryName());
        publisher.addPublication("udp://localhost:5563", 10);

        Thread.sleep(1000);

        Thread sub1 = new Thread(()-> {
            GatewayClientImpl marketDataGatewaySub1 = new GatewayClientImpl();
            TestListener testListener1 = new TestListener("one");
            marketDataGatewaySub1.addListener(testListener1);
            marketDataGatewaySub1.connectOutput("udp://localhost:5563", 10);
        });
        sub1.start();

        publisher.send(createNewOrder(1000,100,SideEnum.Buy,OrdTypeEnum.Limit));

        Thread.sleep(1000);
    }

    private DirectBuffer createNewOrder(int volume, long price,SideEnum side,OrdTypeEnum orderType){
        String clientOrderId = LocalDateTime.now().toString();
        clientOrderId = BuilderUtil.fill(clientOrderId, NewOrderEncoder.clientOrderIdLength());

        DirectBuffer directBuffer = new NewOrderBuilder().compID(1)
                .clientOrderId(clientOrderId.getBytes())
                .account("account123".getBytes())
                .capacity(CapacityEnum.Agency)
                .cancelOnDisconnect(CancelOnDisconnectEnum.DoNotCancel)
                .orderBook(OrderBookEnum.Regular)
                .securityId(1)
                .traderMnemonic("John             ".getBytes())
                .orderType(orderType)
                .timeInForce(TimeInForceEnum.Day)
                .expireTime("20150813-23:00:00".getBytes())
                .side(side)
                .orderQuantity(volume)
                .displayQuantity(volume)
                .minQuantity(volume)
                .limitPrice(price)
                .stopPrice(0)
                .build();

        return directBuffer;
    }


    @Test
    public void testMultiCast() throws Exception {

        MediaDriver driver = MediaDriver.launchEmbedded();
        AeronPublisher publisher = new AeronPublisher(driver.aeronDirectoryName());
        publisher.addPublication("aeron:udp?endpoint=224.0.1.1:40456", 10);

        Thread.sleep(1000);

        Thread sub1 = new Thread(()-> {
            GatewayClientImpl marketDataGatewaySub1 = new GatewayClientImpl();
            TestListener testListener1 = new TestListener("one");
            marketDataGatewaySub1.addListener(testListener1);
            marketDataGatewaySub1.connectOutput("aeron:udp?endpoint=224.0.1.1:40456", 10);
        });
        sub1.start();

        Thread sub2 = new Thread(()-> {
            GatewayClientImpl marketDataGatewaySub2 = new GatewayClientImpl();
            TestListener testListener2 = new TestListener("two");
            marketDataGatewaySub2.addListener(testListener2);
            marketDataGatewaySub2.connectOutput("aeron:udp?endpoint=224.0.1.1:40456", 10);
        });

        sub2.start();

        DirectBuffer buffer = new BestBidOfferBuilder().messageType(MessageTypeEnum.BestBidAsk)
                .instrumentId(1)
                .bid(90)
                .bidQuantity(100)
                .offer(120)
                .offerQuantity(100)
                .build();

        publisher.send(buffer);

        Thread.sleep(1000);

    }

    class TestListener extends AbstractGatewayListener{

        private String name;

        public TestListener(String name){
            this.name= name;
        }
        @Override
        public void updateBidAskPrice(long securityId,long bid, long bidQuantity, long offer, long offerQuantity) {
            System.out.println(bid + "," + bidQuantity + "," + offer + "," + offerQuantity);
            System.out.println("In listener " + name);
        }
    }

}