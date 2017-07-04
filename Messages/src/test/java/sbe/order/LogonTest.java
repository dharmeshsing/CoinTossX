package sbe.order;

/**
 * Created by dharmeshsing on 1/08/15.
 */
public class LogonTest {

   /* @Test
    public void testEncodeLogon() throws UnsupportedEncodingException {
        ByteBuffer data = encodeLogonMessage();
        assertNotNull(data);
    }

    @Test
    public void testEncodeDecodeLogon() throws UnsupportedEncodingException {
        ByteBuffer data = encodeLogonMessage();

        int bufferIndex = 0;
        MessageHeader messageHeader = new MessageHeader();
        DirectBuffer decodeDirectBuffer = new DirectBuffer(data);

        messageHeader = messageHeader.wrap(decodeDirectBuffer, bufferIndex, 0);
        Logon logon = (Logon) MessageTestUtil.getTemplate(messageHeader.templateId());

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.size();

        logon.wrapForDecode(decodeDirectBuffer, bufferIndex, actingBlockLength, actingVersion);
        byte[] buffer = new byte[128];

        assertEquals(1, logon.compID());
        assertEquals("password12", new String(buffer, 0, logon.getPassword(buffer, 0), Logon.passwordCharacterEncoding()));
        assertEquals("1234567890", new String(buffer, 0, logon.getNewPassword(buffer, 0), Logon.newPasswordCharacterEncoding()));

    }

    private ByteBuffer encodeLogonMessage() throws UnsupportedEncodingException {
        int bufferIndex = 0;
        Logon logon = new Logon();
        MessageHeader messageHeader = new MessageHeader();
        ByteBuffer buffer = ByteBuffer.allocateDirect(106);
        DirectBuffer encodeBuffer = new DirectBuffer(buffer);

        messageHeader.wrap(encodeBuffer, bufferIndex, 0)
                .blockLength(logon.sbeBlockLength())
                .templateId(logon.sbeTemplateId())
                .schemaId(logon.sbeSchemaId())
                .version(logon.sbeSchemaVersion());

        bufferIndex += messageHeader.size();
        logon.wrapForEncode(encodeBuffer, bufferIndex);

        logon.compID(1);
        logon.putPassword("password12".getBytes(), 0);
        logon.putNewPassword("1234567890".getBytes(), 0);

        return buffer;
    }

    @Test
    public void testEncodeDecodeUnusedLengthLogon() throws UnsupportedEncodingException {
        ByteBuffer data = encodeUnusedLengthLogonMessage();

        int bufferIndex = 0;
        MessageHeader messageHeader = new MessageHeader();
        DirectBuffer decodeDirectBuffer = new DirectBuffer(data);

        messageHeader = messageHeader.wrap(decodeDirectBuffer, bufferIndex, 0);
        Logon logon = (Logon)MessageTestUtil.getTemplate(messageHeader.templateId());

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.size();

        logon.wrapForDecode(decodeDirectBuffer, bufferIndex, actingBlockLength, actingVersion);
        byte[] buffer = new byte[128];

        assertEquals(1, logon.compID());
        assertEquals("password1", new String(buffer, 0, logon.getPassword(buffer, 0), Logon.passwordCharacterEncoding()).trim());
        assertEquals("123456789", new String(buffer, 0, logon.getNewPassword(buffer, 0), Logon.newPasswordCharacterEncoding()).trim());

    }

    private ByteBuffer encodeUnusedLengthLogonMessage() throws UnsupportedEncodingException {
        int bufferIndex = 0;
        Logon logon = new Logon();
        MessageHeader messageHeader = new MessageHeader();
        ByteBuffer buffer = ByteBuffer.allocateDirect(106);
        DirectBuffer encodeBuffer = new DirectBuffer(buffer);

        messageHeader.wrap(encodeBuffer, bufferIndex, 0)
                .blockLength(logon.sbeBlockLength())
                .templateId(logon.sbeTemplateId())
                .schemaId(logon.sbeSchemaId())
                .version(logon.sbeSchemaVersion());

        bufferIndex += messageHeader.size();
        logon.wrapForEncode(encodeBuffer, bufferIndex);

        //logon.putCompID("12345 ".getBytes(), 0);
        logon.compID(1);
        logon.putPassword("password1 ".getBytes(), 0);
        logon.putNewPassword("123456789 ".getBytes(), 0);

        return buffer;
    }*/
}
