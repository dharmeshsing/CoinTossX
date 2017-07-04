package sbe.order;

/**
 * Created by dharmeshsing on 15/03/01.
 */
public class OrderMessageTest {

   /* @Test
    public void testEncodeOrderMessage() throws UnsupportedEncodingException {
         ByteBuffer data = encodeOrderMessage();
         assertNotNull(data);
    }

    @Test
    public void testEncodeDecodeByteArray() throws UnsupportedEncodingException {
        ByteBuffer data = encodeOrderMessage();

        int bufferIndex = 0;
        Order order = new Order();
        MessageHeader messageHeader = new MessageHeader();
        DirectBuffer decodeDirectBuffer = new DirectBuffer(data);

        messageHeader.wrap(decodeDirectBuffer, bufferIndex, 0)
                .blockLength(order.sbeBlockLength())
                .templateId(order.sbeTemplateId())
                .schemaId(order.sbeSchemaId())
                .version(order.sbeSchemaVersion());

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.size();

        order.wrapForDecode(decodeDirectBuffer, bufferIndex, actingBlockLength, actingVersion);
        byte[] buffer = new byte[128];

        assertEquals(1,order.securityId());
        assertEquals(2,order.orderType());
        assertEquals(3,order.timeInForce());
        assertEquals(1,order.side());
        assertEquals(100,order.orderQuantity());
        assertEquals(100,order.displayQuantity());
        assertEquals(100,order.minQuantity());
        assertEquals(200,order.limitPrice(),0);
        assertEquals(150,order.stopPrice(),0);
        assertEquals(2,order.capacity());
        assertEquals(1,order.cancelOnDisconnect());
        assertEquals(1,order.orderBook());

        assertEquals("123",new String(buffer, 0, order.getClientOrderId(buffer,0,buffer.length), Order.clientOrderIdCharacterEncoding()));
        assertEquals("Test_main",new String(buffer, 0, order.getTraderMnemonic(buffer, 0, buffer.length), Order.traderMnemonicCharacterEncoding()));
        assertEquals("1234",new String(buffer, 0, order.getAccount(buffer, 0, buffer.length), Order.accountCharacterEncoding()));
        assertEquals("20141220-08:51:00",new String(buffer, 0, order.getExpireTime(buffer, 0, buffer.length), Order.expireTimeCharacterEncoding()));

        assertEquals(2,order.header().messageLength());
        assertEquals('D',order.header().messageType());

    }

    private ByteBuffer encodeOrderMessage() throws UnsupportedEncodingException {
        int srcOffset = 0;
        int bufferIndex = 0;
        Order order = new Order();
        MessageHeader messageHeader = new MessageHeader();
        ByteBuffer buffer = ByteBuffer.allocateDirect(106);
        DirectBuffer encodeBuffer = new DirectBuffer(buffer);

        messageHeader.wrap(encodeBuffer, bufferIndex, 0)
                .blockLength(order.sbeBlockLength())
                .templateId(order.sbeTemplateId())
                .schemaId(order.sbeSchemaId())
                .version(order.sbeSchemaVersion());

        bufferIndex += messageHeader.size();
        order.wrapForEncode(encodeBuffer, bufferIndex)
                .securityId(1)
                .orderType(2)
                .timeInForce(3)
                .side(1)
                .orderQuantity(100)
                .displayQuantity(100)
                .minQuantity(100)
                .limitPrice(200)
                .stopPrice(150)
                .capacity(2)
                .cancelOnDisconnect(1)
                .orderBook(1);


        byte[] clientOrderId  = "123".getBytes(Order.clientOrderIdCharacterEncoding());
        byte[] traderrMnemonic = "Test_main".getBytes(Order.traderMnemonicCharacterEncoding());
        byte[] account = "1234".getBytes(Order.accountCharacterEncoding());
        byte[] expireTime = "20141220-08:51:00".getBytes(Order.expireTimeCharacterEncoding());
        byte[] msgType = "D".getBytes("UTF-8");


        order.putClientOrderId(clientOrderId, srcOffset, clientOrderId.length);
        order.putTraderMnemonic(traderrMnemonic,srcOffset,traderrMnemonic.length);
        order.putAccount(account,srcOffset,account.length);
        order.putExpireTime(expireTime,srcOffset,expireTime.length);

        order.header().messageLength(2)
                .messageType(msgType[0]);

        return buffer;

    }*/

}
