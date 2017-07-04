package sbe.builder.marketData;

import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.UnitHeaderEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class UnitHeaderBuilder {
    private int bufferIndex;
    private UnitHeaderEncoder unitHeader;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int messageCount;
    private byte marketDataGroup;
    private int sequenceNumber;

    public static int BUFFER_SIZE = 15;

    public UnitHeaderBuilder(){
        unitHeader = new UnitHeaderEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public UnitHeaderBuilder messageCount(int value){
        this.messageCount = value;
        return this;
    }

    public UnitHeaderBuilder marketDataGroup(byte value){
        this.marketDataGroup = value;
        return this;
    }

    public UnitHeaderBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(unitHeader.sbeBlockLength())
                .templateId(unitHeader.sbeTemplateId())
                .schemaId(unitHeader.sbeSchemaId())
                .version(unitHeader.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        unitHeader.wrap(encodeBuffer, bufferIndex);

        unitHeader.messageCount(messageCount)
                  .marketDataGroup(marketDataGroup)
                  .sequenceNumber(sequenceNumber);

        return encodeBuffer;
    }

}
