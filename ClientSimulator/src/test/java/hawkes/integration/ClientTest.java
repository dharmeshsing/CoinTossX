package hawkes.integration;

import com.carrotsearch.hppc.IntObjectMap;
import hawkes.Client;
import hawkes.ClientData;
import org.junit.Before;
import org.junit.Test;
import sbe.builder.AdminBuilder;
import sbe.msg.AdminTypeEnum;
import sbe.msg.OrdTypeEnum;
import sbe.msg.SideEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmeshsing on 29/03/16.
 */
public class ClientTest {

    private Client client;
    private boolean isGatewayUp;

    @Before
    public void setup() throws Exception {
        isGatewayUp = true;
        System.out.println();
        IntObjectMap<ClientData> clientData = ClientData.loadClientDataData(Paths.get("").toAbsolutePath().getParent() + "/data");
        client = new Client(clientData.get(1),null,1);

        //client.initMulticastMarketDataGatewaySub();
        client.loginToTradingGatewayPub();

        AdminBuilder adminBuilder = new AdminBuilder();
        DirectBuffer directBuffer = adminBuilder.compID(1).adminMessage(AdminTypeEnum.WarmUpComplete).build();
        client.getTradingGatewayPub().send(directBuffer);

        send(client, 1200, 25034, SideEnum.Buy, OrdTypeEnum.Limit);
        send(client, 1000, 25057, SideEnum.Sell, OrdTypeEnum.Limit);

        int count = 0;
        while(client.getBid() == 0 && client.getOffer() == 0 && count < 3){
            System.out.println("Waiting...");
            Thread.sleep(100);
            count++;
        }

        if(count == 3){
            isGatewayUp = false;
        }
        org.junit.Assume.assumeTrue(isGatewayUp);
    }

    private void send(Client client,int volume, long price,SideEnum side,OrdTypeEnum orderType){
        client.getTradingGatewayPub().send(client.createNewOrder(volume, price, side, orderType));
    }

    @Test
    public void testAggressiveBuyTrade() throws Exception {
        client.aggressiveBuyTrade();
        Thread.sleep(2000);

        assertEquals(0,client.getOffer());
    }

    @Test
    public void testAggressiveBuyTradeOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        client.aggressiveBuyTrade();
        Thread.sleep(2000);

        assertEquals(0,client.getOffer());
    }

    @Test
    public void testAggressiveBuyTradeBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        client.aggressiveBuyTrade();
        Thread.sleep(2000);

        assertEquals(0,client.getOffer());
    }

    @Test
    public void testAggressiveSellTrade() throws Exception {
        client.aggressiveSellTrade();
        Thread.sleep(2000);

        assertEquals(0,client.getBid());
    }

    @Test
    public void testAggressiveSellTradeBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        client.aggressiveSellTrade();
        Thread.sleep(2000);

        assertEquals(0,client.getBid());
    }

    @Test
    public void testAggressiveSellTradeOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        client.aggressiveSellTrade();
        Thread.sleep(2000);

        assertEquals(0,client.getBid());
    }



    @Test
    public void testAggressiveBuyQuotes() throws Exception {
        long bid = client.getBid();

        client.aggressiveBuyQuotes();
        Thread.sleep(2000);

        assertTrue(client.getBid() > bid);
    }

    @Test
    public void testAggressiveBuyQuotesBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long bid = client.getBid();

        client.aggressiveBuyQuotes();
        Thread.sleep(2000);

        assertTrue(client.getBid() > bid);
    }

    @Test
    public void testAggressiveBuyQuotesOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long bid = client.getBid();

        client.aggressiveBuyQuotes();
        Thread.sleep(2000);

        assertTrue(client.getBid() > bid);
    }

    @Test
    public void testAggressiveSellQuotes() throws Exception {
        long offer = client.getOffer();

        client.aggressiveSellQuotes();
        Thread.sleep(2000);

        assertTrue(client.getOffer() < offer);
    }

    @Test
    public void testAggressiveSellQuotesBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long offer = client.getOffer();

        client.aggressiveSellQuotes();
        Thread.sleep(2000);

        assertTrue(client.getOffer() < offer);
    }

    @Test
    public void testAggressiveSellQuotesOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long offer = client.getOffer();
        assertTrue(offer == 0);

        client.aggressiveSellQuotes();
        Thread.sleep(2000);

        assertTrue(client.getOffer() > offer);
    }

    @Test
    public void testPassiveBuyTrade() throws Exception {
        long offerQuantity = client.getOfferQuantity();

        client.passiveBuyTrade();
        Thread.sleep(2000);

        assertTrue(client.getOfferQuantity() < offerQuantity);
    }

    @Test
    public void testPassiveBuyTradeBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long offerQuantity = client.getOfferQuantity();

        client.passiveBuyTrade();
        Thread.sleep(2000);

        assertTrue(client.getOfferQuantity() < offerQuantity);
    }

    @Test
    public void testPassiveBuyTradeOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long offerQuantity = client.getOfferQuantity();

        client.passiveBuyTrade();
        Thread.sleep(2000);

        assertTrue(client.getOfferQuantity() == offerQuantity);
    }

    @Test
    public void testPassiveSellTrade() throws Exception {
        long bidQuantity = client.getBidQuantity();

        client.passiveSellTrade();
        Thread.sleep(2000);

        assertTrue(client.getBidQuantity() < bidQuantity);
    }

    @Test
    public void testPassiveSellTradeBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long bidQuantity = client.getBidQuantity();

        client.passiveSellTrade();
        Thread.sleep(2000);

        assertTrue(client.getBidQuantity() == bidQuantity);
    }

    @Test
    public void testPassiveSellTradeOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long bidQuantity = client.getBidQuantity();

        client.passiveSellTrade();
        Thread.sleep(2000);

        assertTrue(client.getBidQuantity() < bidQuantity);
    }

    @Test
    public void testPassiveBuyQuote() throws Exception {
        long bid = client.getBid();

        client.passiveBuyQuote();
        Thread.sleep(2000);

        assertTrue(client.getBid() == bid);
    }

    @Test
    public void testPassiveBuyQuoteBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long bid = client.getBid();
        assertTrue(bid == 0);

        client.passiveBuyQuote();
        Thread.sleep(2000);

        assertTrue(client.getBid() > bid);
    }

    @Test
    public void testPassiveBuyQuoteOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long bid = client.getBid();

        client.passiveBuyQuote();
        Thread.sleep(2000);

        assertTrue(client.getBid() == bid);
    }

    @Test
    public void testPassiveSellQuote() throws Exception {
        long offer = client.getOffer();

        client.passiveSellQuote();
        Thread.sleep(2000);

        assertTrue(client.getOffer() == offer);
    }

    @Test
    public void testPassiveSellQuoteBuyZero() throws Exception {
        send(client, 1200, 25034, SideEnum.Sell, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long offer = client.getOffer();

        client.passiveSellQuote();
        Thread.sleep(2000);

        assertTrue(client.getOffer() == offer);
    }

    @Test
    public void testPassiveSellQuoteOfferZero() throws Exception {
        send(client, 1000, 25057, SideEnum.Buy, OrdTypeEnum.Limit);
        Thread.sleep(2000);

        long offer = client.getOffer();
        assertTrue(offer == 0);

        client.passiveSellQuote();
        Thread.sleep(2000);

        assertTrue(client.getOffer() > offer);
    }


}
