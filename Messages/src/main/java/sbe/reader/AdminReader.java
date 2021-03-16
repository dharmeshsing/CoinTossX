package sbe.reader;

import sbe.msg.AdminDecoder;
import sbe.msg.AdminTypeEnum;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

public class AdminReader {
    private int bufferIndex;
    private AdminDecoder admin;
    private MessageHeaderDecoder messageHeader;
    private int compId;
    private int securityId;
    private AdminTypeEnum adminTypeEnum;

    public AdminReader(){
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        admin = new AdminDecoder();
    }

    public void read(DirectBuffer buffer){
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        compId = messageHeader.compID();
        bufferIndex += messageHeader.encodedLength();

        admin.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);
        adminTypeEnum = admin.adminMessage();
        securityId = admin.securityId();
    }

    public int getCompId() {
        return compId;
    }

    public AdminTypeEnum getAdminTypeEnum() {
        return adminTypeEnum;
    }

    public int getSecurityId() {
        return securityId;
    }
}
