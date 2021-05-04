package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.NewsEncoder;
import sbe.msg.UrgencyEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class NewsMessageBuilder {
    private int bufferIndex;
    private NewsEncoder news;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;


    private int compID;
    private short partitionId;
    private int sequenceNumber;
    private UnsafeBuffer origTime;
    private UrgencyEnum urgency;
    private UnsafeBuffer headLine;
    private UnsafeBuffer text;
    private UnsafeBuffer instruments;

    public static int BUFFER_SIZE = 976;

    public NewsMessageBuilder(){
        news = new NewsEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        origTime = new UnsafeBuffer(ByteBuffer.allocateDirect(NewsEncoder.origTimeLength()));
        headLine = new UnsafeBuffer(ByteBuffer.allocateDirect(NewsEncoder.headLineLength()));
        text = new UnsafeBuffer(ByteBuffer.allocateDirect(NewsEncoder.textLength()));
        instruments = new UnsafeBuffer(ByteBuffer.allocateDirect(NewsEncoder.instrumentsLength()));
    }

    public NewsMessageBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public NewsMessageBuilder partitionId(short value){
        this.partitionId = value;
        return this;
    }

    public NewsMessageBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }

    public NewsMessageBuilder origTime(byte[] value){
        this.origTime.wrap(value);
        return this;
    }

    public NewsMessageBuilder urgency(UrgencyEnum value){
        this.urgency = value;
        return this;
    }

    public NewsMessageBuilder headLine(byte[] value){
        this.headLine.wrap(value);
        return this;
    }

    public NewsMessageBuilder text(byte[] value){
        this.text.wrap(value);
        return this;
    }

    public NewsMessageBuilder instruments(byte[] value){
        this.instruments.wrap(value);
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(news.sbeBlockLength())
                .templateId(news.sbeTemplateId())
                .schemaId(news.sbeSchemaId())
                .version(news.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        news.wrap(encodeBuffer, bufferIndex)
                .partitionId(partitionId)
                .sequenceNumber(sequenceNumber)
                .putOrigTime(origTime.byteArray(),0)
                .urgency(urgency)
                .putHeadLine(headLine.byteArray(),0)
                .putText(text.byteArray(),0)
                .putInstruments(instruments.byteArray(),0);

        return encodeBuffer;
    }

}
