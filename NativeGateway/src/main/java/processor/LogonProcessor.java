package processor;

import gateway.incoming.Client;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;
import org.apache.commons.lang3.CharUtils;
import sbe.msg.*;

import java.nio.ByteBuffer;

public class LogonProcessor {
    private byte[] passWrdBytes = new byte[LogonDecoder.passwordLength()];
    private LogonResponseEncoder logonResopnse = new LogonResponseEncoder();
    private MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private UnsafeBuffer responseBuffer = new UnsafeBuffer(ByteBuffer.allocate(512));

    public DirectBuffer process(LogonDecoder logon,MessageHeaderDecoder messageHeaderDecoder,DirectBuffer directBuffer,Client client,boolean isLoggedIn){

        if(isLoggedIn){
            buildResponse(RejectCode.ConcurrentLoginLimitReached,client.getCompID());
        }else {
            RejectCode rejectCode = validate(logon, messageHeaderDecoder, directBuffer);
            buildResponse(rejectCode,client.getCompID());
            if (rejectCode == RejectCode.LoginSuccessful) {
                client.setLoggedIn(true);
            }
        }

        return responseBuffer;
    }

    private RejectCode validate(LogonDecoder logon,MessageHeaderDecoder messageHeaderDecoder,DirectBuffer directBuffer){
        int actingBlockLength = messageHeaderDecoder.blockLength();
        int actingVersion = messageHeaderDecoder.version();
        int bufferIndex = messageHeaderDecoder.encodedLength();

        logon.wrap(directBuffer, bufferIndex, actingBlockLength, actingVersion);

        if(logon.compID() == 0){
            return RejectCode.InvalidCompIDOrPassword;
        }

        logon.getPassword(passWrdBytes, 0);
        for(int i=0; i<passWrdBytes.length; i++){
            if(passWrdBytes[i] == 0){
                return RejectCode.InvalidCompIDOrPassword;
            }

            if(!CharUtils.isAsciiPrintable((char)passWrdBytes[i])){
                return RejectCode.InvalidCompIDOrPassword;
            }
        }

        return RejectCode.LoginSuccessful;
    }

    public UnsafeBuffer buildResponse(RejectCode rejectCode, int compId){
        int bufferIndex = 0;
        messageHeaderEncoder.wrap(responseBuffer, bufferIndex)
                .blockLength(logonResopnse.sbeBlockLength())
                .templateId(logonResopnse.sbeTemplateId())
                .schemaId(logonResopnse.sbeSchemaId())
                .version(logonResopnse.sbeSchemaVersion())
                .compID(compId);

        bufferIndex += messageHeaderEncoder.encodedLength();
        logonResopnse.wrap(responseBuffer, bufferIndex);
        logonResopnse.rejectCode(rejectCode);
        logonResopnse.passwordExpiry(-1); //TODO: Implement logic

        return responseBuffer;
    }
}
