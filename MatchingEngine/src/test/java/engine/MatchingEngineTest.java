package engine;

import common.MessageGenerator;
import org.junit.Test;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.mockito.Mockito.mock;

/**
 * Created by dharmeshsing on 2014/12/29.
 */
public class MatchingEngineTest {

    private static MatchingEngine matchingEngine;

   /* @Test
    public void testReceiveIncomingOrder() throws InterruptedException, UnsupportedEncodingException {
        matchingEngine = new MatchingEngine();
        matchingEngine.start();

        ByteBuffer order = OrderMessageGenerator.createTestOrder();

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.connect("tcp://localhost:5563");

        Thread.sleep(1000);

        int sendResult = publisher.sendByteBuffer(order, 0);
        assertEquals(true,sendResult);

        publisher.close();
        matchingEngine.stop();
    }*/

    @Test
    public void testOrderCancelRequest() throws Exception {
        MatchingEngine matchingEngine = mock(MatchingEngine.class);
        DirectBuffer buffer = MessageGenerator.buildOrderCancelRequest();

        //matchingEngine.onFragment(buffer,0,buffer.capacity(),null);
    }


}
